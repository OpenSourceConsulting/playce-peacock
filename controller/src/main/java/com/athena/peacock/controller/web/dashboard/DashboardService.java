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
import com.athena.peacock.controller.web.machine.MachineDao;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.monitor.MonDataDao;
import com.athena.peacock.controller.web.rhevm.RHEVApi;
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

	public static final String DASH_BOARD = "DASH_BOARD";
	
    protected final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    
	@Inject
	@Named("machineDao")
	private MachineDao machineDao;
    
	@Inject
	@Named("monDataDao")
	private MonDataDao monDataDao;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;
    
    public DashboardDto getDashboardInfo() throws Exception {
		DashboardDto dto = new DashboardDto();
		
		List<RHEVMRestTemplate> rhevmTemplateList = RHEVMRestTemplateManager.getAllTemplates();
		
		for (RHEVMRestTemplate restTemplate : rhevmTemplateList) {
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
			
			// Agent 목록 조회
			MachineDto machine = new MachineDto();
			machine.setStart(1);
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
			}
			
			dto.getVmTotalCnt().put(restTemplate.getRhevmName(), total);
			dto.getVmUpCnt().put(restTemplate.getRhevmName(), active);
			dto.getVmList().put(restTemplate.getRhevmName(), instanceList);
			dto.getTemplateList().put(restTemplate.getRhevmName(), templateList);
			dto.getTemplateTotalCnt().put(restTemplate.getRhevmName(), templateTotalCnt);
			dto.getAgentList().put(restTemplate.getRhevmName(), agentList);
			dto.getAgentTotalCnt().put(restTemplate.getRhevmName(), agentTotalCnt);
			dto.getAgentRunningCnt().put(restTemplate.getRhevmName(), agentRunningCnt);
			
			// critical alarm list
			monDataDao.getAlarmList(restTemplate.getHypervisorId(), dto);
/*
			Map<String, AlarmDto> alarmList = monDataDao.getAlarmList(restTemplate.getHypervisorId(), dto);
			
			int cpuAlarmCnt = 0;
			int memoryAlarmCnt = 0;
			int diskAlarmCnt = 0;
			
			AlarmDto alarm = null;
			Iterator<AlarmDto> iter = alarmList.values().iterator();
			while (iter.hasNext()) {
				alarm = iter.next();
				
				if (alarm.getCpu()) {
					cpuAlarmCnt++;
				}
				if (alarm.getMemory()) {
					memoryAlarmCnt++;
				}
				if(alarm.getDisk()) {
					diskAlarmCnt++;
				}
			}
			
			dto.getCpuCriticalCnt().put(restTemplate.getRhevmName(), cpuAlarmCnt);
			dto.getMemoryCriticalCnt().put(restTemplate.getRhevmName(), memoryAlarmCnt);
			dto.getDiskCriticalCnt().put(restTemplate.getRhevmName(), diskAlarmCnt);
			
			// warning alarm list
			alarmList = monDataDao.getWarningList(restTemplate.getHypervisorId());

			cpuAlarmCnt = 0;
			memoryAlarmCnt = 0;
			diskAlarmCnt = 0;
			
			alarm = null;
			iter = alarmList.values().iterator();
			while (iter.hasNext()) {
				alarm = iter.next();
				
				if (alarm.getCpu()) {
					cpuAlarmCnt++;
				}
				if (alarm.getMemory()) {
					memoryAlarmCnt++;
				}
				if(alarm.getDisk()) {
					diskAlarmCnt++;
				}
			}
			dto.getCpuWarningCnt().put(restTemplate.getRhevmName(), cpuAlarmCnt);
			dto.getMemoryWarningCnt().put(restTemplate.getRhevmName(), memoryAlarmCnt);
			dto.getDiskWarningCnt().put(restTemplate.getRhevmName(), diskAlarmCnt)
*/
		}
		
		// project cnt
		
		
		// svn cnt
		
		
		// jenking cnt
		
		
		// httpd cnt
		
		
		// tomcat cnt
		
		
		// jboss cnt
		
		
		// cpu top 5
		
		
		// memory top 5
		
		
		// disk top 5
		
		
		return dto;
    }
}
//end of DashboardService.java