/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Ji-Woong Choi	2013. 10. 23.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.Commandline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.common.core.util.XMLGregorialCalendarUtil;
import com.athena.peacock.controller.common.component.RHEVMRestTemplate;
import com.athena.peacock.controller.common.component.RHEVMRestTemplateManager;
import com.athena.peacock.controller.web.rhevm.domain.Disk;
import com.athena.peacock.controller.web.rhevm.domain.Disks;
import com.athena.peacock.controller.web.rhevm.domain.NIC;
import com.athena.peacock.controller.web.rhevm.domain.Nics;
import com.athena.peacock.controller.web.rhevm.dto.ClusterDto;
import com.athena.peacock.controller.web.rhevm.dto.DataCenterDto;
import com.athena.peacock.controller.web.rhevm.dto.DiskDto;
import com.athena.peacock.controller.web.rhevm.dto.NetworkDto;
import com.athena.peacock.controller.web.rhevm.dto.TemplateDto;
import com.athena.peacock.controller.web.rhevm.dto.VMDto;
import com.redhat.rhevm.api.model.API;
import com.redhat.rhevm.api.model.Action;
import com.redhat.rhevm.api.model.Boot;
import com.redhat.rhevm.api.model.CPU;
import com.redhat.rhevm.api.model.Cluster;
import com.redhat.rhevm.api.model.Clusters;
import com.redhat.rhevm.api.model.CpuTopology;
import com.redhat.rhevm.api.model.DataCenter;
import com.redhat.rhevm.api.model.DataCenters;
import com.redhat.rhevm.api.model.Host;
import com.redhat.rhevm.api.model.Hosts;
import com.redhat.rhevm.api.model.IP;
import com.redhat.rhevm.api.model.Network;
import com.redhat.rhevm.api.model.OperatingSystem;
import com.redhat.rhevm.api.model.Template;
import com.redhat.rhevm.api.model.Templates;
import com.redhat.rhevm.api.model.VM;
import com.redhat.rhevm.api.model.VMs;

/**
 * <pre>
 * RHEV-M Service Class
 * </pre>
 * @author Ji-Woong Choi
 * @version 1.0
 */
@Service("rhevmService")
public class RHEVMService {
	
	protected final Logger logger = LoggerFactory.getLogger(RHEVMService.class);

	@Inject
	@Named("rhevmRestTemplateManager")
	private RHEVMRestTemplateManager manager;
	
	public RHEVMRestTemplate getRHEVMRestTemplate(int hypervisorId) {
		return RHEVMRestTemplateManager.getRHEVMRestTemplate(hypervisorId);
	}
	
	public void init() {
		try {
			manager.afterPropertiesSet();
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred while initializing RHEVMRestTemplates.", e);
		}
	}
	
	/**
	 * <pre>
	 * VM's total / active count를 조회한다.
	 * </pre>
	 * @param hypervisorId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public API getAPI(int hypervisorId) throws RestClientException, Exception {
		return getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.API, HttpMethod.GET, API.class);
	}

	/**
	 * RHEV에 생성되어 있는 가상머신의 목록을 조회한다.
	 * @return 가상 머신 목록
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<VMDto> getVirtualList(int hypervisorId, String name, int page) throws RestClientException, Exception {
		List<VMDto> vmDtoList = new ArrayList<VMDto>();
		
		String url = RHEVApi.VMS;
		
		if (!StringUtils.isEmpty(name)) {
			url =  url + "?search=" + name + "+page+" + page;
		} else {
			url = url + "?search=page+" + page;
		}
		
		List<Cluster> clusterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.CLUSTERS, HttpMethod.GET, Clusters.class).getClusters();
		List<Host> hostList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.HOSTS, HttpMethod.GET, Hosts.class).getHosts();
		
		VMs vms = getRHEVMRestTemplate(hypervisorId).submit(url, HttpMethod.GET, VMs.class);
		List<VM> vmList = vms.getVMs();
		
		int seq = ((page - 1) * 100) + 1;
		for( VM vm : vmList) {
			vmDtoList.add(makeDto(hypervisorId, vm, clusterList, hostList, seq++));
		}
		
		return vmDtoList;
	}
	
	/**
	 * RHEV에 생성된 가상 머신 템플릿 목록을 조회한다.
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<TemplateDto> getTemplateList(int hypervisorId, String name, int page)  throws RestClientException, Exception {
		List<TemplateDto> templateDtoList = new ArrayList<TemplateDto>();
		
		String url = RHEVApi.TEMPLATES;
		
		if (!StringUtils.isEmpty(name)) {
			url =  url + "?search=" + name + "+page+" + page;
		} else {
			url = url + "?search=page+" + page;
		}
		
		List<DataCenter> dataCenterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.DATA_CENTERS, HttpMethod.GET, DataCenters.class).getDataCenters();
		List<Cluster> clusterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.CLUSTERS, HttpMethod.GET, Clusters.class).getClusters();
		
		Templates templates = getRHEVMRestTemplate(hypervisorId).submit(url, HttpMethod.GET, Templates.class);
		List<Template> templateList = templates.getTemplates();

		int seq = ((page - 1) * 100) + 1;
		for( Template template : templateList) {
			templateDtoList.add(makeDto(hypervisorId, template, dataCenterList, clusterList, seq++));
		}
		
		return templateDtoList;
	}
	
	/**
	 * RHEV에 생성된 가상 머신 템플릿을 조회한다.
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public TemplateDto getTemplate(int hypervisorId, String templateId)  throws RestClientException, Exception {
		String templateUrl =  RHEVApi.TEMPLATES + "/" + templateId;
		Template template = getRHEVMRestTemplate(hypervisorId).submit(templateUrl, HttpMethod.GET, Template.class);
		
		return makeDto(hypervisorId, template, null, null, 1);
	}
	
	/**
	 * 템플릿과 가상 머신 이름을 사용하여 새로운 가상 머신을 생성한다.
	 * @param vmName
	 * @param templateId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public VM createVirtualMachine(int hypervisorId, String vmName, String templateId) throws RestClientException, Exception {
		logger.debug("vmName : {}, templateId : {}", vmName, templateId);
		
		Template template = new Template();
		template.setId(templateId);

		VM vm = new VM();
		vm.setName(vmName);
		vm.setTemplate(template);
		
		return createVirtualMachine(hypervisorId, vm);
	}
	
	/**
	 * VMDto 정보를 활용하여 새로운 가상 머신을 생성한다.
	 * @param vmName
	 * @param templateId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public VM createVirtualMachine(int hypervisorId, VMDto dto) throws RestClientException, Exception {
		logger.debug("dto : {}", dto);

		String templateUrl =  RHEVApi.TEMPLATES + "/" + dto.getTemplate();
		Template template = getRHEVMRestTemplate(hypervisorId).submit(templateUrl, HttpMethod.GET, Template.class);
		
		Cluster cluster = new Cluster();
		cluster.setName(dto.getCluster());
		
		Template requestTemplate  = new Template();
		requestTemplate.setName(template.getName());
		
		OperatingSystem os = new OperatingSystem();
		Boot boot = new Boot();
		boot.setDev(template.getOs().getBoot().get(0).getDev());
		os.getBoot().add(boot);
		
		CpuTopology topology = new CpuTopology();
		topology.setSockets(dto.getSockets());
		topology.setCores(1);
		
		CPU cpu = new CPU();
		cpu.setTopology(topology);
		
		Long memory = Long.parseLong(dto.getMemory()) * 1024 * 1024;
		
		VM vm = new VM();
		vm.setName(dto.getName());
		vm.setDescription(dto.getDescription());
		vm.setTemplate(requestTemplate);
		vm.setCluster(cluster);
		vm.setCpu(cpu);
		vm.setOs(os);
		vm.setMemory(memory);
		
		return getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.VMS, HttpMethod.POST, vm, "vm", VM.class);
	}
	
	/**
	 * VM 정보를 활용하여 새로운 가상 머신을 생성한다.
	 * @param vm
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public VM createVirtualMachine(int hypervisorId, VM vm) throws RestClientException, Exception {
		
		// Retrieve origin objects
		String templateUrl =  RHEVApi.TEMPLATES + "/" + vm.getTemplate().getId();
		Template template = getRHEVMRestTemplate(hypervisorId).submit(templateUrl, HttpMethod.GET, Template.class);
		
		String clusterUrl = template.getCluster().getHref();
		Cluster cluster = getRHEVMRestTemplate(hypervisorId).submit(clusterUrl,  HttpMethod.GET, Cluster.class);
		
		// Make request paramater
		Cluster requestCluster = new Cluster();
		requestCluster.setName(cluster.getName());
		
		Template requestTemplate  = new Template();
		requestTemplate.setName(template.getName());
		
		OperatingSystem os = new OperatingSystem();
		Boot boot = new Boot();
		boot.setDev(template.getOs().getBoot().get(0).getDev());
		os.getBoot().add(boot);
		
		vm.setCluster(requestCluster);
		vm.setTemplate(requestTemplate);
		vm.setOs(os);
		vm.setMemory(template.getMemory());
	
		return getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.VMS, HttpMethod.POST, vm, "vm", VM.class);
	}
	
	/**
	 * 지정된 RHEV-M(hypervisorId)에 해당하는 특정 Virtual Machine의 네트워크 인터페이스 조회
	 * @return 가상 머신 목록
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<NetworkDto> getVMNics(int hypervisorId, String vmId) throws RestClientException, Exception {
		List<NetworkDto> networkDtoList = new ArrayList<NetworkDto>();
		String callUrl = RHEVApi.VMS + "/" + vmId + "/nics";
		Nics nics = getRHEVMRestTemplate(hypervisorId).submit(callUrl, HttpMethod.GET, Nics.class);

		List<NIC> nicList = nics.getNics();
		
		for( NIC nic : nicList) {
			networkDtoList.add(makeDto(hypervisorId, nic));
		}
		return networkDtoList;
	}
	
	/**
	 * 지정된 RHEV-M(hypervisorId)에 해당하는 특정 Virtual Machine의 Disk 정보 조회
	 * @return 가상 머신 목록
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<DiskDto> getVMDisks(int hypervisorId, String vmId) throws RestClientException, Exception {
		List<DiskDto> diskDtoList = new ArrayList<DiskDto>();
		String callUrl = RHEVApi.VMS + "/" + vmId + "/disks";
		Disks disks = getRHEVMRestTemplate(hypervisorId).submit(callUrl, HttpMethod.GET, Disks.class);

		List<Disk> diskList = disks.getDisks();
		
		for (Disk disk : diskList) {
			diskDtoList.add(makeDto(disk));
		}
		
		return diskDtoList;
	}
	
	/**
	 * 지정된 RHEV-M(hypervisorId)에 해당하는 특정 Template의 네트워크 인터페이스 조회
	 * @return 가상 머신 목록
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<NetworkDto> getTemplateNics(int hypervisorId, String templateId) throws RestClientException, Exception {
		List<NetworkDto> networkDtoList = new ArrayList<NetworkDto>();
		String callUrl = RHEVApi.TEMPLATES + "/" + templateId + "/nics";
		Nics nics = getRHEVMRestTemplate(hypervisorId).submit(callUrl, HttpMethod.GET, Nics.class);

		List<NIC> nicList = nics.getNics();
		
		for( NIC nic : nicList) {
			networkDtoList.add(makeDto(hypervisorId, nic));
		}
		return networkDtoList;
	}
	
	/**
	 * 지정된 RHEV-M(hypervisorId)에 해당하는 특정 Template의 Disk 정보 조회
	 * @return 가상 머신 목록
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<DiskDto> getTemplateDisks(int hypervisorId, String templateId) throws RestClientException, Exception {
		List<DiskDto> diskDtoList = new ArrayList<DiskDto>();
		String callUrl = RHEVApi.TEMPLATES + "/" + templateId + "/disks";
		Disks disks = getRHEVMRestTemplate(hypervisorId).submit(callUrl, HttpMethod.GET, Disks.class);

		List<Disk> diskList = disks.getDisks();
		
		for (Disk disk : diskList) {
			diskDtoList.add(makeDto(disk));
		}
		
		return diskDtoList;
	}
		
	/**
	 * 특정 가상 머신을 조회한다.
	 * @param vmId
	 * @return
	 * @throws Exception
	 */
	public VMDto getVirtualMachine(int hypervisorId, String vmId) throws Exception {
		String callUrl = RHEVApi.VMS + "/" + vmId;
		VM vm = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.GET, VM.class);
		return makeDto(hypervisorId, vm, null, null, 1);
	}

	/**
	 * 특정 가상 머신을 시작시킨다.
	 * @param vmId
	 * @return
	 * @throws Exception
	 */
	public Action startVirtualMachine(int hypervisorId, String vmId) throws Exception {
		String callUrl = RHEVApi.VMS + "/" + vmId + "/start";
		Action action = new Action();
		action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.POST, action, "action", Action.class);
		return action;
	}
	
	/**
	 * 특정 가상 머신을 중지시킨다.
	 * @param vmId
	 * @return
	 * @throws Exception
	 */
	public Action stopVirtualMachine(int hypervisorId, String vmId) throws Exception {
		String callUrl = RHEVApi.VMS + "/" + vmId + "/stop";
		Action action = new Action();
		action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.POST, action, "action", Action.class);
		return action;
	}
	
	/**
	 * 특정 가산 머신을 셧다운 시킨다.
	 * @param vmId
	 * @return
	 * @throws Exception
	 */
	public Action shutdownVirtualMachine(int hypervisorId, String vmId) throws Exception {
		String callUrl = RHEVApi.VMS + "/" + vmId + "/shutdown";
		Action action = new Action();
		action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.POST, action, "action", Action.class);
		return action;
	}
	
	/**
	 * 특정 가상 머신을 강제로 제거한다.
	 * @param vmId
	 * @return
	 * @throws Exception
	 */
	public Object removeVirtualMachine(int hypervisorId, String vmId) throws Exception {
		Object result = null;
		try {
			String callUrl = RHEVApi.VMS + "/" + vmId;
			Action action = new Action();
			action.setForce(true);
			action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.DELETE, action, "action", Action.class);
			result = action;
		} catch (Exception e) {
			if (e instanceof RestClientException) {
				// VM 삭제 API 호출 시 400 Bad Request 에러가 발생할 경우, curl 명령을 직접 실행한다.
				String auth = getRHEVMRestTemplate(hypervisorId).getCredential();
				String url = getRHEVMRestTemplate(hypervisorId).getUrl(RHEVApi.VMS + "/" + vmId);
				
				/*
				// curl 실행파일이 Path에 정의되어 있지 않은 경우
				File executable = new File("/usr/bin/curl");
				Commandline commandLine = new Commandline();
				commandLine.setExecutable(executable.getAbsolutePath());
				/*/
				// curl 실행파일이 Path에 정의되어 있는 경우
				Commandline commandLine = new Commandline();
				commandLine.setExecutable("curl");
				//*/
				
				/** change working directory if necessary */
				//commandLine.setWorkingDirectory("/");
				
				/** invoke createArg() and setValue() one by one for each arguments */
				//commandLine.createArg().setValue("-X");
				//commandLine.createArg().setValue("GET");
				// ...

				/** invoke createArg() and setLine() for entire arguments */
				commandLine.createArg().setLine("--insecure -H \"Content-Type: application/xml\" -H \"Accept: application/xml\" -H \"Authorization: " + auth + "\" -X DELETE -d \"<action><force>true</force></action>\" \"" + url + "\"");
				
				/** verify command string */
				logger.debug("CURL Command : [{}]", commandLine.toString());
				
				/** also enable StringWriter, PrintWriter, WriterStreamConsumer and etc. */
				StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();

				int returnCode = CommandLineUtils.executeCommandLine(commandLine, consumer, consumer, Integer.MAX_VALUE);

				result = consumer.getOutput();
				
				if (returnCode == 0) {
					// success
					logger.debug("Succeed : [{}]", consumer.getOutput());
				} else {
					// fail
					logger.debug("Failed : [{}]", consumer.getOutput());
					throw new Exception(consumer.getOutput());
				}
			} else {
				throw e;
			}
		}
		
		return result;
	}

	/**
	 * <pre>
	 * 특정 가상 머신을 export 시킨다.
	 * </pre>
	 * @param hypervisorId
	 * @param vmId
	 * @return
	 */
	public Action exportVirtualMachine(Integer hypervisorId, String vmId) throws Exception {
		String callUrl = RHEVApi.VMS + "/" + vmId + "/export";
		Action action = new Action();
		action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.POST, action, "action", Action.class);
		return action;
	}

	/**
	 * <pre>
	 * 선택된 VM을 이용하여 템플릿을 생성한다.
	 * </pre>
	 * @param hypervisorId
	 * @param templateName
	 * @param vmId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public Template makeTemplate(int hypervisorId, String templateName, String vmId, String description) throws RestClientException, Exception {
		VM vm = new VM();
		vm.setId(vmId);

		Template template = new Template();
		template.setName(templateName);
		template.setVm(vm);
		template.setDescription(description);		
		
		return getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.TEMPLATES, HttpMethod.POST, template, "template", Template.class);
	}

	/**
	 * <pre>
	 * 특정 템플릿을 제거한다.
	 * </pre>
	 * @param hypervisorId
	 * @param templateId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public Action removeTemplate(Integer hypervisorId, String templateId) throws RestClientException, Exception {
		String callUrl = RHEVApi.TEMPLATES + "/" + templateId;
		Action action = new Action();
		action.setForce(true);
		action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.DELETE, action, "action", Action.class);
		return action;
	}

	/**
	 * <pre>
	 * VM 생성 시 사용되는 Data Center 목록 조회
	 * </pre>
	 * @param hypervisorId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<DataCenterDto> getDataCenter(Integer hypervisorId) throws RestClientException, Exception {
		List<DataCenterDto> dataCenterDtoList = new ArrayList<DataCenterDto>();
		DataCenterDto dataCenterDto = null;
		
		List<DataCenter> dataCenterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.DATA_CENTERS, HttpMethod.GET, DataCenters.class).getDataCenters();
		
		// up 상태인 data center만 추출한다.
		for (DataCenter dc : dataCenterList) {
			if (dc.getStatus().getState().toLowerCase().equals("up")) {
				dataCenterDto = new DataCenterDto();
				dataCenterDto.setId(dc.getId());
				dataCenterDto.setName(dc.getName());
				dataCenterDto.setDescription(dc.getDescription());
				dataCenterDto.setStatus(dc.getStatus().getState());
				dataCenterDto.setHypervisorId(hypervisorId);
				
				dataCenterDtoList.add(dataCenterDto);
			}
		}
		
		return dataCenterDtoList;
	}

	/**
	 * <pre>
	 * VM 생성 시 사용되는 Host Cluster 목록 조회
	 * </pre>
	 * @param hypervisorId
	 * @param dataCenterId
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<ClusterDto> getHostCluster(Integer hypervisorId, String dataCenterId) throws RestClientException, Exception {
		List<ClusterDto> clusterDtoList = new ArrayList<ClusterDto>();
		ClusterDto clusterDto = null;
		
		List<Cluster> clusterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.CLUSTERS, HttpMethod.GET, Clusters.class).getClusters();

		// 지정된 data center에 속한 cluster만 추출한다.
		for (Cluster c : clusterList) {
			if (c.getDataCenter().getId().equals(dataCenterId)) {
				clusterDto = new ClusterDto();
				clusterDto.setId(c.getId());
				clusterDto.setName(c.getName());
				clusterDto.setDescription(c.getDescription());
				clusterDto.setHypervisorId(hypervisorId);
				
				clusterDtoList.add(clusterDto);
			}
		}
		
		return clusterDtoList;
	}
	
	/**
	 * RHEV Template 데이터를 추출하여 DTO로 저장한다.
	 * @param template
	 * @return
	 */
	private TemplateDto makeDto(int hypervisorId, Template template, List<DataCenter> dataCenterList, List<Cluster> clusterList, int seq) {
		TemplateDto dto = new TemplateDto();
		
		dto.setSeq(seq);
		dto.setTemplateId(template.getId());
		dto.setName(template.getName());
		dto.setDescription(template.getDescription());
		dto.setStatus(template.getStatus().getState());
		dto.setType(template.getType());
		dto.setOs(template.getOs().getType());
		
		dto.setDisplay(template.getDisplay().getType());
		dto.setCreationTime(XMLGregorialCalendarUtil.convertXmlGregorianCalendarToFormattedString(template.getCreationTime().toString()));
		
		dto.setMemory((template.getMemory()/1024/1024) + "");
		dto.setSockets(template.getCpu().getTopology().getSockets());
		dto.setCores(template.getCpu().getTopology().getCores());
		
		dto.setHaEnabled(Boolean.toString(template.getHighAvailability().isEnabled()));
		dto.setHaPriority(template.getHighAvailability().getPriority());
		
		// Optional information
		
		try {
			String clusterUrl = template.getCluster().getHref();
			Cluster cluster = null;
			
			if (clusterList != null) {
				for (int i = 0; i < clusterList.size(); i++) {
					cluster = clusterList.get(i);

					if (cluster.getHref().equals(clusterUrl)) {
						dto.setCluster(cluster.getName());
						break;
					}
				}
			} else {
				cluster = getRHEVMRestTemplate(hypervisorId).submit(clusterUrl, HttpMethod.GET, Cluster.class);
				
				if(cluster != null) {
					dto.setCluster(cluster.getName());
				}
			}
			
			String dcUrl = cluster.getDataCenter().getHref();
			DataCenter dc = null;

			if (dataCenterList != null) {
				for (int i = 0; i < dataCenterList.size(); i++) {
					dc = dataCenterList.get(i);

					if (dc.getHref().equals(dcUrl)) {
						dto.setDataCenter(dc.getName());
						break;
					}
				}
			} else {
				dc = getRHEVMRestTemplate(hypervisorId).submit(dcUrl, HttpMethod.GET, DataCenter.class);
				
				if( dc != null) {
					dto.setDataCenter(dc.getName());
				}
			}
		
			// dataCenterList가 null이고 clusterList가 null일 경우 전체 DataCenter 및 각 DataCenter에 대한 HostCluster 정보를 함께 조회한다.
			if (dataCenterList == null && clusterList == null) {
				dataCenterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.DATA_CENTERS, HttpMethod.GET, DataCenters.class).getDataCenters();
				
				Map<String, List<String>> clusterMap = new TreeMap<String, List<String>>();
				List<String> clusterNameList = null;
				
				String dataCenterId = null;
				String dataCenterName = null;
				// up 상태인 data center만 추출한다.
				for (DataCenter dataCenter : dataCenterList) {
					if (dataCenter.getStatus().getState().toLowerCase().equals("up")) {
						dataCenterId = dataCenter.getId();
						dataCenterName = dataCenter.getName();
						
						clusterList = getRHEVMRestTemplate(hypervisorId).submit(RHEVApi.CLUSTERS, HttpMethod.GET, Clusters.class).getClusters();

						clusterNameList = new ArrayList<String>();
						
						// 지정된 data center에 속한 cluster만 추출한다.
						for (Cluster c : clusterList) {
							if (c.getDataCenter().getId().equals(dataCenterId)) {
								clusterNameList.add(c.getName());
							}
						}
						
						if (clusterNameList.size() > 0) {
							clusterMap.put(dataCenterName, clusterNameList);
						}
					}
				}
				
				if (clusterMap.size() > 0) {
					dto.setClusterMap(clusterMap);
				}
			}
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred while make templateDto.", e);
		}
		
		return dto;
	}
	
	/**
	 * NIC으로부터 값을 추출하여 NetworkDto로 맵핑시킨다.
	 * 
	 * @param nic
	 * @return
	 */
	private NetworkDto makeDto(int hypervisorId, NIC nic) {
		NetworkDto dto = new NetworkDto();
		
		dto.setName(nic.getName());
		dto.setType(nic.getInterface());
		
		if (nic.isLinked() != null) { 
			dto.setLinked(Boolean.toString(nic.isLinked()));
		} else {
			dto.setLinked("N/A");
		}
		
		if (nic.isPlugged() != null) {
			dto.setPlugged(Boolean.toString(nic.isPlugged()));
		} else {
			dto.setPlugged("N/A");
		}
		
		if (nic.isActive() != null) {
			dto.setActive(Boolean.toString(nic.isActive()));
		} else {
			dto.setActive("N/A");
		}
		
		if (nic.getMac() != null) {
			dto.setMacAddress(nic.getMac().getAddress());
		}
		
		try {
			String networkHref = nic.getNetwork().getHref();
			Network network = getRHEVMRestTemplate(hypervisorId).submit(networkHref, HttpMethod.GET, Network.class);
			if (network != null) {
				dto.setNetworkName(network.getName());
			}
		} catch (Exception e) {
			logger.error("Unhandler Exception has occurred : ", e);
		}
		
		return dto;
	}
	
	/**
	 * Disk로부터 값을 추출하여 DiskDto로 맵핑시킨다.
	 * 
	 * @param disk
	 * @return
	 */
	private DiskDto makeDto(Disk disk) {
		DiskDto dto = new DiskDto();
		
		dto.setName(disk.getName());
		dto.setActive(disk.getActive());
		dto.setInterface(disk.getInterface());
		
		if (disk.getSize() != null) {
			dto.setVirtualSize((disk.getSize()/1024/1024) + "");
		} else {
			dto.setVirtualSize("N/A");
		}
		
		if (disk.getActualSize() != null) {
			dto.setActualSize((disk.getActualSize()/1024/1024) + "");
		} else {
			dto.setActualSize("N/A");
		}
		
		if (disk.isBootable() != null) {
			dto.setBootable(Boolean.toString(disk.isBootable()));
		} else {
			dto.setBootable("N/A");
		}
		
		if (disk.isShareable() != null) {
			dto.setSharable(Boolean.toString(disk.isShareable()));
		} else {
			dto.setSharable("N/A");
		}
		
		if (disk.getStatus() != null) {
			dto.setStatus(disk.getStatus().getState());
		} else {
			dto.setStatus("N/A");
		}

		return dto;
	}

	/**
	 * Internal하게 VM으로부터 필요한 데이터를 추출하도록 한다. 
	 * @param vm
	 * @return
	 */
	private VMDto makeDto(int hypervisorId, VM vm, List<Cluster> clusterList, List<Host> hostList, int seq) {
		VMDto dto = new VMDto();
		dto.setSeq(seq);
		dto.setVmId(vm.getId());
		dto.setName(vm.getName());
		dto.setDescription(vm.getDescription());
		dto.setType(vm.getType());

		dto.setOs(vm.getOs().getType());
		dto.setMemory((vm.getMemory()/1024/1024) + "");
		dto.setSockets(vm.getCpu().getTopology().getSockets());
		dto.setCores(vm.getCpu().getTopology().getCores());
		dto.setOrigin(vm.getOrigin());
		dto.setPriority(vm.getHighAvailability().getPriority().toString());
		dto.setDisplay(vm.getDisplay().getType());
		dto.setStatus(vm.getStatus().getState());
		dto.setCreationTime(XMLGregorialCalendarUtil.convertXmlGregorianCalendarToFormattedString(vm.getCreationTime().toString()));
		dto.setHaEnabled(Boolean.toString(vm.getHighAvailability().isEnabled()));
		dto.setHaPriority(vm.getHighAvailability().getPriority());
		
		if(vm.getStartTime() != null) {
			dto.setStartTime(XMLGregorialCalendarUtil.convertXmlGregorianCalendarToFormattedString(vm.getStartTime().toString()));
		}
		
		// Optional information
		try {
			// Getting IP Address
			if( vm.getGuestInfo() != null ) {
				IP ip = vm.getGuestInfo().getIps().getIPs().get(0);
				if( ip != null) dto.setIpAddr(ip.getAddress());
			}
			
			String clusterUrl = vm.getCluster().getHref();
			Cluster cluster = null;
			if (clusterList != null) {
				for (int i = 0; i < clusterList.size(); i++) {
					cluster = clusterList.get(i);

					if (cluster.getHref().equals(clusterUrl)) {
						dto.setCluster(cluster.getName());
						break;
					}
				}
			} else {
				cluster = getRHEVMRestTemplate(hypervisorId).submit(clusterUrl, HttpMethod.GET, Cluster.class);
				if(cluster != null) dto.setCluster(cluster.getName());
			}
			
			// DataCenter는 표시되는 곳이 없음. 단건 조회일 경우에만 추출한다.
			if (clusterList == null) {
				String dcUrl = cluster.getDataCenter().getHref();
				DataCenter dc = getRHEVMRestTemplate(hypervisorId).submit(dcUrl, HttpMethod.GET, DataCenter.class);
				if( dc != null) dto.setDataCenter(dc.getName());
			}
			
			if( vm.getHost() != null ) {
				String hostUrl = vm.getHost().getHref();
				Host host = null;
				
				if (hostList != null) {
					for (int i = 0; i < hostList.size(); i++) {
						host = hostList.get(i);
						
						if (host.getHref().equals(hostUrl)) {
							dto.setHost(host.getName());
							break;
						}
					}
				} else {
					host = getRHEVMRestTemplate(hypervisorId).submit(hostUrl, HttpMethod.GET, Host.class);
					if( host != null ) dto.setHost(host.getName());
				}
			}
			
			// Template 정보는 상세 조회시에만 표시됨. 다건 조회일 경우는 Skip
			if (clusterList == null) {
				String templateUrl = vm.getTemplate().getHref();
				Template template = getRHEVMRestTemplate(hypervisorId).submit(templateUrl, HttpMethod.GET, Template.class);
				if( template != null ) dto.setTemplate(template.getName());
			}
			
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred while make vmDto.", e);
		}
		
		return dto;
	}

}
