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
 * Sang-cheon Park	2015. 8. 10.		First Draft.
 */
package com.athena.peacock.controller.web.kvm;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.libvirt.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.common.core.action.ShellAction;
import com.athena.peacock.common.core.action.support.PropertyUtil;
import com.athena.peacock.common.core.action.support.TargetHost;
import com.athena.peacock.common.core.command.Command;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.kvm.dto.DomainDto;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.machine.MachineService;
import com.athena.peacock.controller.web.user.UserController;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * This is a controller for KVM virsh CLI.
 * KVM virsh CLI를 이용한 작업을 수행하는 컨트롤러
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("/kvm")
public class KVMController {

    protected final Logger logger = LoggerFactory.getLogger(KVMController.class);
    
	@Inject
	@Named("kvmService")
	private KVMService kvmService;
	
	@Inject
	@Named("machineService")
	private MachineService machineService;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 Virtual Machine 목록을 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/list")
	public @ResponseBody GridJsonResponse getVMList(GridJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		
		if (kvmService.getHypervisorClient(dto.getHypervisorId()) == null) {
			kvmService.init();
		}
		
		try {
			int page = ((dto.getStart() - 1) / 100) + 1;
			
			List<Domain> doaminList = kvmService.getDomainList(dto.getHypervisorId(), dto.getName(), page);
			jsonRes.setTotal(doaminList.size());
			jsonRes.setList(doaminList);
			jsonRes.setMsg("VM 목록이 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 목록 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 특정 Virtual Machine을 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/info")
	public @ResponseBody SimpleJsonResponse getVMInfo(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(kvmService.getDomain(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM 정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 정보 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 특정 Virtual Machine의 네트워크 인터페이스 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/nics")
	public @ResponseBody GridJsonResponse getVMNics(GridJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setList(kvmService.getInterfaces(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM의 네트워크 정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM의 네트워크 정보 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 특정 Virtual Machine의 Disk 정보 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/disks")
	public @ResponseBody GridJsonResponse getVMDisks(GridJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setList(kvmService.getDisks(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM의 디스크 정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM의 디스크 정보 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * KVM의 템플릿을 이용하여 신규 VM을 생성한다.
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/create")
	public @ResponseBody SimpleJsonResponse createVirtualMachine(HttpServletRequest request, SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getName(), "name must not be null.");
		//Assert.notNull(dto.getCluster(), "cluster must not be null.");
		//Assert.notNull(dto.getTemplate(), "template must not be null.");
		//Assert.isTrue(dto.getSockets() != 0, "sockets must not be 0.");
		
		try {
			// RHEV REST API를 호출하여 VM을 생성하고 결과를 리턴받는다.
			Domain domain = kvmService.createDomain(dto.getHypervisorId(), dto);
			
			// SSH Key File이 존재할 경우 해당 VM 생성 결과인 vm id로 디렉토리를 생성하고 파일을 저장한다.
            if (dto.getKeyFile() != null && dto.getKeyFile().getSize() > 0 ) {
            	String defaultPath = PropertyUtil.getProperty("upload.dir") + File.separator + domain.getUUIDString() + File.separator;
            	File keyFile = new File(defaultPath + dto.getKeyFile().getOriginalFilename());
                if (!keyFile.exists()) {
                    if (!keyFile.mkdirs()) {
                        throw new Exception("Fail to create a directory for attached file [" + keyFile + "]");
                    }
                }
                
                keyFile.deleteOnExit();
                dto.getKeyFile().transferTo(keyFile);
                dto.setSshKeyFile(keyFile.getAbsolutePath());
            }
            
            // machine_additional_info_tbl에 부가 정보를 저장한다.
            boolean isExist = false;
            MachineDto machine = new MachineDto();

            machine.setMachineId(domain.getUUIDString());
            if (StringUtils.isNotEmpty(dto.getIpAddress())) {
                machine.setIpAddress(dto.getIpAddress());
                machine.setNetmask(dto.getNetmask());
                machine.setGateway(dto.getGateway());
                machine.setNameServer(dto.getNameServer());
            	isExist = true;
            }
            if (StringUtils.isNotEmpty(dto.getSshPort())) {
                machine.setSshPort(dto.getSshPort());
                machine.setSshUsername(dto.getSshUsername());
                machine.setSshPassword(dto.getSshPassword());
                machine.setSshKeyFile(dto.getSshKeyFile());
            	isExist = true;
            }
            if (StringUtils.isNotEmpty(dto.getHostName())) {
            	machine.setHostName(dto.getHostName());
            	isExist = true;
            }
            
            if (isExist) {
    			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
    			if (userDto != null) {
    				machine.setRegUserId(userDto.getUserId());
    				machine.setUpdUserId(userDto.getUserId());
    			}
    			
            	machineService.insertAdditionalInfo(machine);
            }
            
			jsonRes.setMsg("VM 생성이 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 생성 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 입력받은 domainName을 사용하여 KVM의 VM을 시작시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/start")
	public @ResponseBody SimpleJsonResponse startVirtualMachine(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(kvmService.startDomain(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM 시작이 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 시작 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 입력받은 domainName을 사용하여 KVM의 VM을 중지시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/stop")
	public @ResponseBody SimpleJsonResponse stopVirtualMachine(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		final String vmId = dto.getVmId();
		
		new Thread() {
			public void run() {
				try {
					// VM 중지 전 Agent를 중지시킨다.
					ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
					cmdMsg.setAgentId(vmId);
					cmdMsg.setBlocking(true);

					int sequence = 0;
					Command command = new Command("STOP_AGENT");
					
					ShellAction action = new ShellAction(sequence++);
					
					action.setCommand("service");
					action.addArguments("peacock-agent stop");

					command.addAction(action);
					
					cmdMsg.addCommand(command);

					PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
					peacockTransmitter.sendMessage(datagram);
				} catch (Exception e) {
					// ignore this exception
					// Agent가 설치되지 않은 VM일 경우 에러가 발생할 수 있고, 이미 Agent가 종료되어 연결된 Channel이 없을 수 있다.
				}
			};
		}.start();
		
		Thread.sleep(1000);
		
		try {
			jsonRes.setData(kvmService.stopDomain(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM 중지가 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 중지 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 입력받은 domainName을 사용하여 KVM의 VM을 셧다운시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/shutdown")
	public @ResponseBody SimpleJsonResponse shutdownVirtualMachine(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		final String vmId = dto.getVmId();
		
		new Thread() {
			public void run() {
				try {
					// VM 중지 전 Agent를 중지시킨다.
					ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
					cmdMsg.setAgentId(vmId);
					cmdMsg.setBlocking(true);

					int sequence = 0;
					Command command = new Command("STOP_AGENT");
					
					ShellAction action = new ShellAction(sequence++);
					
					action.setCommand("service");
					action.addArguments("peacock-agent stop");

					command.addAction(action);
					
					cmdMsg.addCommand(command);

					PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
					peacockTransmitter.sendMessage(datagram);
				} catch (Exception e) {
					// ignore this exception
					// Agent가 설치되지 않은 VM일 경우 에러가 발생할 수 있고, 이미 Agent가 종료되어 연결된 Channel이 없을 수 있다.
				}
			};
		}.start();
		
		Thread.sleep(1000);
		
		try {
			jsonRes.setData(kvmService.shutdownDomain(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM 종료가 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 종료 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 입력받은 domainName을 사용하여 KVM의 VM을 제거시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/remove")
	public @ResponseBody SimpleJsonResponse removeVirtualMachine(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(kvmService.removeDomain(dto.getHypervisorId(), dto.getVmId()));
			
			// VM이 정상적으로 삭제된 경우 DB의 machine_tbl에서도 해당 VM을 제거한다.
			machineService.deleteMachine(dto.getVmId());
			
			jsonRes.setMsg("VM 삭제가 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM 삭제 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 입력받은 domainName을 사용하여 KVM의 VM을 export 시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/export")
	public @ResponseBody SimpleJsonResponse exportVirtualMachine(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		/*
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getDomainName(), "domainName must not be null.");
		
		try {
			jsonRes.setData(kvmService.exportVirtualMachine(dto.getHypervisorId(), dto.getDomainName()));
			jsonRes.setMsg("VM Export가 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM Export 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		*/
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 입력받은 domainName을 사용하여 KVM의 VM을 템플릿으로 만든다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/makeTemplate")
	public @ResponseBody SimpleJsonResponse makeTemplate(SimpleJsonResponse jsonRes, DomainDto dto) throws Exception {
		/*
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getDomainName(), "name must not be null.");
		Assert.notNull(dto.getDomainName(), "domainName must not be null.");
		
		try {
			jsonRes.setData(kvmService.makeTemplate(dto.getHypervisorId(), dto.getDomainName(), dto.getDomainName(), dto.getDescription()));
			jsonRes.setMsg("VM을 이용한 템플릿 생성이 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM을 이용한 템플릿 생성 요청 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		*/
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 특정 VM Instance로 SSH Command를 호출한다.
	 * </pre>
	 * @param jsonRes
	 * @param targetHost
	 * @param command
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/backup")
	public @ResponseBody SimpleJsonResponse backupVirtualMachine(SimpleJsonResponse jsonRes, TargetHost targetHost, String command) throws Exception {
		/*
		Assert.isTrue(StringUtils.isNotEmpty(targetHost.getHost()), "host must not be null.");
		Assert.isTrue(targetHost.getPort() != null, "port must not be null.");
		Assert.isTrue(targetHost.getPort() != 0, "port must not be 0.");
		Assert.isTrue(StringUtils.isNotEmpty(targetHost.getUsername()), "username must not be null.");
		Assert.isTrue(StringUtils.isNotEmpty(targetHost.getPassword()), "password must not be null.");
		
		jsonRes.setData(SshExecUtil.executeCommand(targetHost, command));
		jsonRes.setMsg("VM Backup is starting. Please check your console");
		*/
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 Template 목록을 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/templates")
	public @ResponseBody GridJsonResponse getTemplateList(GridJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		
		if (kvmService.getHypervisorClient(dto.getHypervisorId()) == null) {
			kvmService.init();
		}
		
		try {
			int page = ((dto.getStart() - 1) / 100) + 1;
			
			List<TemplateDto> templateDtoList = kvmService.getTemplateList(dto.getHypervisorId(), dto.getName(), page);
			jsonRes.setTotal(templateDtoList.size());
			jsonRes.setList(templateDtoList);
			jsonRes.setMsg("템플릿 목록이 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("템플릿 목록 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	*/
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 특정 Template 정보를 조회한다.
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/templates/info")
	public @ResponseBody SimpleJsonResponse getTemplateInfo(SimpleJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setData(kvmService.getTemplate(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("템플릿 정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("템플릿 정보 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	*/
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 특정 Template의 네트워크 인터페이스 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/templates/nics")
	public @ResponseBody GridJsonResponse getTemplateNics(GridJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setList(kvmService.getTemplateNics(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("템플릿의 네트워크 정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("템플릿의 네트워크 정보 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	*/
	
	/**
	 * <pre>
	 * 지정된 KVM(hypervisorId)에 해당하는 특정 Template의 Disk 정보 조회
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/templates/disks")
	public @ResponseBody GridJsonResponse getTemplateDisks(GridJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setList(kvmService.getTemplateDisks(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("템플릿의 디스크 정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("템플릿의 디스크 정보 조회 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	*/
	
	/**
	 * <pre>
	 * 입력받은 templateId를 사용하여 템플릿을 제거한다. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/templates/remove")
	public @ResponseBody SimpleJsonResponse removeTemplate(SimpleJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setData(kvmService.removeTemplate(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("템플릿 삭제가 정상적으로 요청되었습니다. 잠시만 기다려주십시오.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("템플릿 삭제 요청 중 에러가 발생하였습니다.<br/>템플릿을 참조하는 VM이 있는지 확인하십시오.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	*/
}
//end of KVMController.java