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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.controller.common.component.RHEVMRestTemplate;
import com.athena.peacock.controller.common.component.RHEVMRestTemplateManager;
import com.athena.peacock.controller.web.rhevm.domain.Disk;
import com.athena.peacock.controller.web.rhevm.domain.Disks;
import com.athena.peacock.controller.web.rhevm.domain.NIC;
import com.athena.peacock.controller.web.rhevm.domain.Nics;
import com.athena.peacock.controller.web.rhevm.dto.DiskDto;
import com.athena.peacock.controller.web.rhevm.dto.NetworkDto;
import com.athena.peacock.controller.web.rhevm.dto.TemplateDto;
import com.athena.peacock.controller.web.rhevm.dto.VMDto;
import com.redhat.rhevm.api.model.Action;
import com.redhat.rhevm.api.model.Boot;
import com.redhat.rhevm.api.model.Cluster;
import com.redhat.rhevm.api.model.DataCenter;
import com.redhat.rhevm.api.model.Host;
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
	
	private RHEVMRestTemplate getRHEVMRestTemplate(int hypervisorId) {
		return RHEVMRestTemplateManager.getRHEVMRestTemplate(hypervisorId);
	}

	/**
	 * RHEV에 생성되어 있는 가상머신의 목록을 조회한다.
	 * @return 가상 머신 목록
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<VMDto> getVirtualList(int hypervisorId, String name) throws RestClientException, Exception {
		List<VMDto> vmDtoList = new ArrayList<VMDto>();
		
		String url = RHEVApi.VMS;
		
		if (!StringUtils.isEmpty(name)) {
			url =  url + "?search=" + name;
		}
		
		VMs vms = getRHEVMRestTemplate(hypervisorId).submit(url, HttpMethod.GET, VMs.class);
		List<VM> vmList = vms.getVMs();
		
		for( VM vm : vmList) {
			vmDtoList.add(makeDto(hypervisorId, vm));
		}
		return vmDtoList;
	}
	
	/**
	 * RHEV에 생성된 가상 머신 템플릿 목록을 조회한다.
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public List<TemplateDto> getTemplateList(int hypervisorId, String name)  throws RestClientException, Exception {
		List<TemplateDto> templateDtoList = new ArrayList<TemplateDto>();
		
		String url = RHEVApi.TEMPLATES;
		
		if (!StringUtils.isEmpty(name)) {
			url =  url + "?search=" + name;
		}
		
		Templates templates = getRHEVMRestTemplate(hypervisorId).submit(url, HttpMethod.GET, Templates.class);
		List<Template> templateList = templates.getTemplates();
		
		for( Template template : templateList) {
			templateDtoList.add(makeDto(hypervisorId, template));
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
		
		return makeDto(hypervisorId, template);
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
		return makeDto(hypervisorId, vm);
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
	public Action removeVirtualMachine(int hypervisorId, String vmId) throws Exception {
		String callUrl = RHEVApi.VMS + "/" + vmId;
		Action action = new Action();
		action.setForce(true);
		action = getRHEVMRestTemplate(hypervisorId).submit(callUrl,  HttpMethod.DELETE, action, "action", Action.class);
		return action;
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
	 * RHEV Template 데이터를 추출하여 DTO로 저장한다.
	 * @param template
	 * @return
	 */
	private TemplateDto makeDto(int hypervisorId, Template template) {
		TemplateDto dto = new TemplateDto();
		
		dto.setTemplateId(template.getId());
		dto.setName(template.getName());
		dto.setDescription(template.getDescription());
		dto.setStatus(template.getStatus().getState());
		dto.setType(template.getType());
		dto.setOs(template.getOs().getType());
		
		dto.setDisplay(template.getDisplay().getType());
		dto.setCreationTime(template.getCreationTime().toString());
		
		dto.setMemory((template.getMemory()/1024/1024) + "");
		dto.setSockets(template.getCpu().getTopology().getSockets());
		dto.setCores(template.getCpu().getTopology().getCores());
		
		dto.setHaEnabled(Boolean.toString(template.getHighAvailability().isEnabled()));
		dto.setHaPriority(template.getHighAvailability().getPriority());
		
		// Optional information
		
		try {
			String clusterUrl = template.getCluster().getHref();
			Cluster cluster = getRHEVMRestTemplate(hypervisorId).submit(clusterUrl,  HttpMethod.GET, Cluster.class);
			if(cluster != null) dto.setCluster(cluster.getName());
			
			String dcUrl = cluster.getDataCenter().getHref();
			DataCenter dc = getRHEVMRestTemplate(hypervisorId).submit(dcUrl,  HttpMethod.GET, DataCenter.class);
			if( dc != null) dto.setDataCenter(dc.getName());
		
		} catch (Exception e) {
			e.printStackTrace();
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
		dto.setLinked(Boolean.toString(nic.isLinked()));
		dto.setPlugged(Boolean.toString(nic.isPlugged()));
		dto.setActive(Boolean.toString(nic.isActive()));
		
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
		dto.setVirtualSize((disk.getSize()/1024/1024) + "");
		dto.setActualSize((disk.getActualSize()/1024/1024) + "");
		dto.setBootable(Boolean.toString(disk.isBootable()));
		dto.setSharable(Boolean.toString(disk.isShareable()));
		dto.setInterface(disk.getInterface());
		dto.setStatus(disk.getStatus().getState());

		return dto;
	}

	/**
	 * Internal하게 VM으로부터 필요한 데이터를 추출하도록 한다. 
	 * @param vm
	 * @return
	 */
	private VMDto makeDto(int hypervisorId, VM vm) {
		VMDto dto = new VMDto();
		dto.setVmId(vm.getId());
		dto.setName(vm.getName());
		dto.setDescription(vm.getDescription());
		dto.setType(vm.getType());

		dto.setOs(vm.getOs().getType());
		dto.setMemory((vm.getMemory()/1024/1024) + "");
		dto.setSocket(vm.getCpu().getTopology().getSockets());
		dto.setCores(vm.getCpu().getTopology().getCores());
		dto.setOrigin(vm.getOrigin());
		dto.setPriority(vm.getHighAvailability().getPriority().toString());
		dto.setDisplay(vm.getDisplay().getType());
		dto.setStatus(vm.getStatus().getState());
		dto.setCreationTime(vm.getCreationTime().toString());
		dto.setHaEnabled(Boolean.toString(vm.getHighAvailability().isEnabled()));
		dto.setHaPriority(vm.getHighAvailability().getPriority());
		
		if(vm.getStartTime() != null) dto.setStartTime(vm.getStartTime().toString());
		
		// Optional information
		try {
			// Getting IP Address
			if( vm.getGuestInfo() != null ) {
				IP ip = vm.getGuestInfo().getIps().getIPs().get(0);
				if( ip != null) dto.setIpAddr(ip.getAddress());
			}
			
			String clusterUrl = vm.getCluster().getHref();
			Cluster cluster = getRHEVMRestTemplate(hypervisorId).submit(clusterUrl, HttpMethod.GET, Cluster.class);
			if(cluster != null) dto.setCluster(cluster.getName());
			
			String dcUrl = cluster.getDataCenter().getHref();
			DataCenter dc = getRHEVMRestTemplate(hypervisorId).submit(dcUrl, HttpMethod.GET, DataCenter.class);
			if( dc != null) dto.setDataCenter(dc.getName());
			
			if( vm.getHost() != null ) {
				String hostUrl = vm.getHost().getHref();
				Host host = getRHEVMRestTemplate(hypervisorId).submit(hostUrl, HttpMethod.GET, Host.class);
				if( host != null ) dto.setHost(host.getName());
			}
			
			String templateUrl = vm.getTemplate().getHref();
			Template template = getRHEVMRestTemplate(hypervisorId).submit(templateUrl, HttpMethod.GET, Template.class);
			if( template != null ) dto.setTemplate(template.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

}
