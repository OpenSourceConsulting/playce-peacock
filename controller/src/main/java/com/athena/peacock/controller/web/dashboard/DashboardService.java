/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2014. 9. 22.		First Draft.
 */
package com.athena.peacock.controller.web.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.athena.peacock.controller.common.component.RHEVMRestTemplate;
import com.athena.peacock.controller.common.component.RHEVMRestTemplateManager;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.alm.AlmDashboardComponent;
import com.athena.peacock.controller.web.hypervisor.HypervisorDto;
import com.athena.peacock.controller.web.hypervisor.HypervisorService;
import com.athena.peacock.controller.web.machine.MachineDao;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.monitor.MonDataDao;
import com.athena.peacock.controller.web.rhevm.RHEVApi;
import com.athena.peacock.controller.web.software.SoftwareDao;
import com.athena.peacock.controller.web.software.SoftwareDto;
import com.athena.peacock.controller.web.software.SoftwareRepoDao;
import com.athena.peacock.controller.web.software.SoftwareRepoDto;
import com.redhat.rhevm.api.model.API;
import com.redhat.rhevm.api.model.IP;
import com.redhat.rhevm.api.model.Template;
import com.redhat.rhevm.api.model.Templates;
import com.redhat.rhevm.api.model.VM;
import com.redhat.rhevm.api.model.VMs;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("dashboardService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class DashboardService {
	
    protected final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    
	@Inject
	@Named("machineDao")
	private MachineDao machineDao;
    
	@Inject
	@Named("monDataDao")
	private MonDataDao monDataDao;
	
	@Inject
	@Named("softwareRepoDao")
	private SoftwareRepoDao softwareRepoDao;
    
	@Inject
	@Named("softwareDao")
	private SoftwareDao softwareDao;

	@Inject
	@Named("almDashboardComponent")
	AlmDashboardComponent almDashboardComponent;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	@Inject
	@Named("hypervisorService")
	private HypervisorService hypervisorService;

	private DashboardDto dashboardDto;
	private String status = "INIT";
    
    /**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	public synchronized void refreshDashboardInfo() throws Exception {
    	status = "GATHERING";
    	
    	try {
			DashboardDto dto = new DashboardDto();
	
			List<RHEVMRestTemplate> rhevmTemplateList = RHEVMRestTemplateManager.getAllTemplates();
			
			if (rhevmTemplateList == null || rhevmTemplateList.size() == 0) {
				List<HypervisorDto> hypervisorList = hypervisorService.getHypervisorList();
				RHEVMRestTemplateManager.resetRHEVMRestTemplate(hypervisorList);
				rhevmTemplateList = RHEVMRestTemplateManager.getAllTemplates();
			}
			
			logger.debug("[DASHBOARD] rhevmTemplateList.size() : {}", rhevmTemplateList.size());
			
			for (RHEVMRestTemplate restTemplate : rhevmTemplateList) {
				dto.addRhevmNames(restTemplate.getRhevmName());
				
				API api = restTemplate.submit(RHEVApi.API, HttpMethod.GET, API.class);
				
				int total = (int) (long) api.getSummary().getVMs().getTotal();
				int active = (int) (long) api.getSummary().getVMs().getActive();
				
				int totalPage = ((total - 1) / 100) + 1;
				
				// Virtual Machine 전체 목록 조회
				List<InstanceDto> instanceList = new ArrayList<InstanceDto>();
				InstanceDto instanceDto = null;
				VMs vms = null;
				for (int page = 1; page <= totalPage; page++) {
					vms = restTemplate.submit(RHEVApi.VMS + "?search=page+" + page, HttpMethod.GET, VMs.class);
					
					for (VM vm : vms.getVMs()) {
						instanceDto = new InstanceDto();
						instanceDto.setInstanceName(vm.getName());
						instanceDto.setVmStatus(vm.getStatus().getState());
						
						if (vm.getGuestInfo() != null ) {
							IP ip = vm.getGuestInfo().getIps().getIPs().get(0);
							if (ip != null) {
								instanceDto.setIpAddress(ip.getAddress());
							}
						}
						
						instanceList.add(instanceDto);
					}
				}
				
				logger.debug("[DASHBOARD] {}'s Virtual Machine 전체 목록 조회 완료 : {}", restTemplate.getRhevmName(), instanceList.size());
				
				// Template 전체 목록 조회
				Templates templates = restTemplate.submit(RHEVApi.TEMPLATES, HttpMethod.GET, Templates.class);
				List<TemplateDto> templateList = new ArrayList<TemplateDto>();
				TemplateDto templateDto = null;
				for (Template template : templates.getTemplates()) {
					templateDto = new TemplateDto();
					templateDto.setTemplateName(template.getName());
					templateDto.setDescription(template.getDescription());
					templateList.add(templateDto);
				}
				
				int templateTotalCnt = templateList.size();
				
				logger.debug("[DASHBOARD] {}'s Template 전체 목록 조회 완료 : {}", restTemplate.getRhevmName(), templateList.size());
				
				// Agent 목록 조회
				MachineDto machine = new MachineDto();
				machine.setHypervisorId(restTemplate.getHypervisorId());
				machine.setStart(0);
				machine.setLimit(2000);
				List<MachineDto> machineList = machineDao.getMachineList(machine);
				
				int agentTotalCnt = machineList.size();
				int agentRunningCnt = 0;
				List<InstanceDto> agentList = new ArrayList<InstanceDto>();
				for (int i = 0; i < machineList.size(); i++) {
					machine = machineList.get(i);
					instanceDto = new InstanceDto();
					instanceDto.setInstanceName(machine.getDisplayName());
					instanceDto.setIpAddress(machine.getIpAddr());
					
					if (peacockTransmitter.isActive(machine.getMachineId()) == true) {
						agentRunningCnt++;
						instanceDto.setAgentStatus("Running");
					} else {
						instanceDto.setAgentStatus("Down");
					}
					
					agentList.add(instanceDto);
				}
				
				logger.debug("[DASHBOARD] {}'s Agent 전체 목록 조회 완료 : {}", restTemplate.getRhevmName(), agentList.size());
				
				dto.getVmTotalCnt().put(restTemplate.getRhevmName(), total);
				dto.getVmUpCnt().put(restTemplate.getRhevmName(), active);
				dto.getVmList().put(restTemplate.getRhevmName(), instanceList);
				dto.getTemplateList().put(restTemplate.getRhevmName(), templateList);
				dto.getTemplateTotalCnt().put(restTemplate.getRhevmName(), templateTotalCnt);
				dto.getAgentList().put(restTemplate.getRhevmName(), agentList);
				dto.getAgentTotalCnt().put(restTemplate.getRhevmName(), agentTotalCnt);
				dto.getAgentRunningCnt().put(restTemplate.getRhevmName(), agentRunningCnt);
	
				logger.debug("[DASHBOARD] {}'s critical / alarm list, cpu / memory / disk top 5 list 조회 시작", restTemplate.getRhevmName());
				
				// critical / alarm list, cpu / memory / disk top 5 list
				monDataDao.getAlarmList(restTemplate.getHypervisorId(), restTemplate.getRhevmName(), dto);
				
				logger.debug("[DASHBOARD] {}'s critical / alarm list, cpu / memory / disk top 5 list 조회 완료", restTemplate.getRhevmName());
			}
			
			/*
			// project cnt
			try {
				dto.setProjectCnt(almDashboardComponent.getProjectCnt());
			} catch (Exception e) {
				logger.error("Unhandled exception has occurred while invoke getProjectCnt() : ", e);
				dto.setProjectCnt(0);
			}
			
			// svn cnt
			try {
				dto.setSvnCnt(almDashboardComponent.getSvnCnt());
			} catch (Exception e) {
				logger.error("Unhandled exception has occurred while invoke getSvnCnt() : ", e);
				dto.setSvnCnt(0);
			}
			
			// jenkins cnt
			try {
				dto.setJenkinsCnt(almDashboardComponent.getJenkinsCnt());
			} catch (Exception e) {
				logger.error("Unhandled exception has occurred while invoke getJenkinsCnt() : ", e);
				dto.setJenkinsCnt(0);
			}
			*/
			
			// httpd, tomcat, jboss cnt
			SoftwareRepoDto softwareRepo = new SoftwareRepoDto();
			softwareRepo.setStart(0);
			softwareRepo.setLimit(100);
			List<SoftwareRepoDto> softwareRepoList = softwareRepoDao.getSoftwareRepoList(softwareRepo);
			
			int httpdCnt = 0;
			int tomcatCnt = 0;
			int jbossCnt = 0;
			SoftwareDto software = null;
			for (SoftwareRepoDto repo : softwareRepoList) {
				if (repo.getSoftwareName().toLowerCase().indexOf("httpd") >= 0) {
					software = new SoftwareDto();
					software.setSoftwareId(repo.getSoftwareId());
					software.setDeleteYn("N");
					httpdCnt += softwareDao.getSoftwareListCnt(software);
				} else if (repo.getSoftwareName().toLowerCase().indexOf("tomcat") >= 0) {
					software = new SoftwareDto();
					software.setSoftwareId(repo.getSoftwareId());
					software.setDeleteYn("N");
					tomcatCnt += softwareDao.getSoftwareListCnt(software);
				} else if (repo.getSoftwareName().toLowerCase().indexOf("jboss") >= 0) {
					software = new SoftwareDto();
					software.setSoftwareId(repo.getSoftwareId());
					software.setDeleteYn("N");
					jbossCnt += softwareDao.getSoftwareListCnt(software);
				}
			}
			
			dto.setHttpdCnt(httpdCnt);
			dto.setTomcatCnt(tomcatCnt);
			dto.setJbossCnt(jbossCnt);
			
			dashboardDto = dto;
    	} finally {
        	status = "DONE";
    	}
    }
    
    public DashboardDto getDashboardInfo() throws Exception {
    	if (dashboardDto != null) {
    		return dashboardDto;
    	}
    	
    	if (status.equals("INIT")) {
    		refreshDashboardInfo();
    	} else if (status.equals("GATHERING")) {
    		int cnt = 0;
    		while (cnt++ < 10) {
    			Thread.sleep(500);
    			
    			if (status.equals("GATHERING")) {
    				continue;
    			} else {
    				break;
    			}
    		}
    	}
		
		return dashboardDto;
    }
}
//end of DashboardService.java