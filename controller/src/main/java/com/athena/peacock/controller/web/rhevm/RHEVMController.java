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
 * Ji-Woong Choi	2013. 11. 01.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.athena.peacock.common.core.util.SshExecUtil;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.machine.MachineService;
import com.athena.peacock.controller.web.rhevm.dto.TemplateDto;
import com.athena.peacock.controller.web.rhevm.dto.VMDto;
import com.athena.peacock.controller.web.user.UserController;
import com.athena.peacock.controller.web.user.UserDto;
import com.redhat.rhevm.api.model.VM;


/**
 * <pre>
 * This is a controller for RHEV-M API.
 * RHEV-M APIë¥??´ìš©???‘ì—…???˜í–‰?˜ëŠ” ì»¨íŠ¸ë¡¤ëŸ¬
 * </pre>
 * @author Ji-Woong Choi
 * @version 1.0
 */
@Controller
@RequestMapping("/rhevm")
public class RHEVMController {

    protected final Logger LOGGER = LoggerFactory.getLogger(RHEVMController.class);
    
	@Inject
	@Named("rhevmService")
	private RHEVMService rhevmService;
	
	@Inject
	@Named("machineService")
	private MachineService machineService;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” Virtual Machine ëª©ë¡??ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/list")
	public @ResponseBody GridJsonResponse getVMList(GridJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		
		if (rhevmService.getRHEVMRestTemplate(dto.getHypervisorId()) == null) {
			rhevmService.init();
		}
		
		try {
			int page = ((dto.getStart() - 1) / 100) + 1;
			
			List<VMDto> vmDtoList = rhevmService.getVirtualList(dto.getHypervisorId(), dto.getName(), page);
			jsonRes.setTotal(vmDtoList.size());
			jsonRes.setList(vmDtoList);
			jsonRes.setMsg("VM ëª©ë¡???•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ëª©ë¡ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” ?¹ì • Virtual Machine??ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/info")
	public @ResponseBody SimpleJsonResponse getVMInfo(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.getVirtualMachine(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM ?•ë³´ê°??•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ?•ë³´ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” ?¹ì • Virtual Machine???¤íŠ¸?Œí¬ ?¸í„°?˜ì´??ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/nics")
	public @ResponseBody GridJsonResponse getVMNics(GridJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setList(rhevmService.getVMNics(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM???¤íŠ¸?Œí¬ ?•ë³´ê°??•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM???¤íŠ¸?Œí¬ ?•ë³´ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” ?¹ì • Virtual Machine??Disk ?•ë³´ ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/disks")
	public @ResponseBody GridJsonResponse getVMDisks(GridJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setList(rhevmService.getVMDisks(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM???”ìŠ¤???•ë³´ê°??•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM???”ìŠ¤???•ë³´ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” Template ëª©ë¡??ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/templates")
	public @ResponseBody GridJsonResponse getTemplateList(GridJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		
		if (rhevmService.getRHEVMRestTemplate(dto.getHypervisorId()) == null) {
			rhevmService.init();
		}
		
		try {
			int page = ((dto.getStart() - 1) / 100) + 1;
			
			List<TemplateDto> templateDtoList = rhevmService.getTemplateList(dto.getHypervisorId(), dto.getName(), page);
			jsonRes.setTotal(templateDtoList.size());
			jsonRes.setList(templateDtoList);
			jsonRes.setMsg("?œí”Œë¦?ëª©ë¡???•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("?œí”Œë¦?ëª©ë¡ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” ?¹ì • Template ?•ë³´ë¥?ì¡°íšŒ?œë‹¤.
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/templates/info")
	public @ResponseBody SimpleJsonResponse getTemplateInfo(SimpleJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.getTemplate(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("?œí”Œë¦??•ë³´ê°??•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("?œí”Œë¦??•ë³´ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” ?¹ì • Template???¤íŠ¸?Œí¬ ?¸í„°?˜ì´??ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/templates/nics")
	public @ResponseBody GridJsonResponse getTemplateNics(GridJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setList(rhevmService.getTemplateNics(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("?œí”Œë¦¿ì˜ ?¤íŠ¸?Œí¬ ?•ë³´ê°??•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("?œí”Œë¦¿ì˜ ?¤íŠ¸?Œí¬ ?•ë³´ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” ?¹ì • Template??Disk ?•ë³´ ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/templates/disks")
	public @ResponseBody GridJsonResponse getTemplateDisks(GridJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setList(rhevmService.getTemplateDisks(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("?œí”Œë¦¿ì˜ ?”ìŠ¤???•ë³´ê°??•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("?œí”Œë¦¿ì˜ ?”ìŠ¤???•ë³´ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” Data Center ëª©ë¡ ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/datacenter")
	public @ResponseBody GridJsonResponse getDataCenter(GridJsonResponse jsonRes, Integer hypervisorId) throws Exception {
		Assert.notNull(hypervisorId, "hypervisorId must not be null.");
		
		try {
			jsonRes.setList(rhevmService.getDataCenter(hypervisorId));
			jsonRes.setMsg("Data Center ëª©ë¡???•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Data Center ëª©ë¡ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ì§? •??RHEV-M(hypervisorId)???´ë‹¹?˜ëŠ” Host Cluster ëª©ë¡ ì¡°íšŒ
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/hostcluster")
	public @ResponseBody GridJsonResponse getHostCluster(GridJsonResponse jsonRes, Integer hypervisorId, String dataCenterId) throws Exception {
		Assert.notNull(hypervisorId, "hypervisorId must not be null.");
		Assert.notNull(dataCenterId, "dataCenterId must not be null.");
		
		try {
			jsonRes.setList(rhevmService.getHostCluster(hypervisorId, dataCenterId));
			jsonRes.setMsg("Data Center ëª©ë¡???•ìƒ?ìœ¼ë¡?ì¡°íšŒ?˜ì—ˆ?µë‹ˆ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Data Center ëª©ë¡ ì¡°íšŒ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * RHEV-M???œí”Œë¦¿ì„ ?´ìš©?˜ì—¬ ? ê·œ VM???ì„±?œë‹¤.
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/create")
	public @ResponseBody SimpleJsonResponse createVirtualMachine(HttpServletRequest request, SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getName(), "name must not be null.");
		Assert.notNull(dto.getCluster(), "cluster must not be null.");
		Assert.notNull(dto.getTemplate(), "template must not be null.");
		Assert.isTrue(dto.getSockets() != 0, "sockets must not be 0.");
		
		try {
			// RHEV REST APIë¥??¸ì¶œ?˜ì—¬ VM???ì„±?˜ê³  ê²°ê³¼ë¥?ë¦¬í„´ë°›ëŠ”??
			VM vm = rhevmService.createVirtualMachine(dto.getHypervisorId(), dto);
			
			// SSH Key File??ì¡´ì¬??ê²½ìš° ?´ë‹¹ VM ?ì„± ê²°ê³¼??vm idë¡??”ë ‰? ë¦¬ë¥??ì„±?˜ê³  ?Œì¼????¥?œë‹¤.
            if (dto.getKeyFile() != null && dto.getKeyFile().getSize() > 0 ) {
            	String defaultPath = PropertyUtil.getProperty("upload.dir") + File.separator + vm.getId() + File.separator;
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
            
            // machine_additional_info_tbl??ë¶?? ?•ë³´ë¥???¥?œë‹¤.
            boolean isExist = false;
            MachineDto machine = new MachineDto();

            machine.setMachineId(vm.getId());
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
            
			jsonRes.setMsg("VM ?ì„±???•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ?ì„± ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? vmIdë¥??¬ìš©?˜ì—¬ RHEV-M??VM???œì‘?œí‚¨?? 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/start")
	public @ResponseBody SimpleJsonResponse startVirtualMachine(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.startVirtualMachine(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM ?œì‘???•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ?œì‘ ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? vmIdë¥??¬ìš©?˜ì—¬ RHEV-M??VM??ì¤‘ì??œí‚¨?? 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/stop")
	public @ResponseBody SimpleJsonResponse stopVirtualMachine(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		final String vmId = dto.getVmId();
		
		new Thread() {
			public void run() {
				try {
					// VM ì¤‘ì? ??Agentë¥?ì¤‘ì??œí‚¨??
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
					LOGGER.error("Peacock Error", e);
					// Agentê°??¤ì¹˜?˜ì? ?Šì? VM??ê²½ìš° ?ëŸ¬ê°?ë°œìƒ?????ˆê³ , ?´ë? Agentê°?ì¢…ë£Œ?˜ì–´ ?°ê²°??Channel???†ì„ ???ˆë‹¤.
				}
			};
		}.start();
		
		Thread.sleep(1000);
		
		try {
			jsonRes.setData(rhevmService.stopVirtualMachine(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM ì¤‘ì?ê°??•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ì¤‘ì? ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? vmIdë¥??¬ìš©?˜ì—¬ RHEV-M??VM???§ë‹¤?´ì‹œ?¨ë‹¤. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/shutdown")
	public @ResponseBody SimpleJsonResponse shutdownVirtualMachine(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		final String vmId = dto.getVmId();
		
		new Thread() {
			public void run() {
				try {
					// VM ì¤‘ì? ??Agentë¥?ì¤‘ì??œí‚¨??
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
					LOGGER.error("Peacock Error", e);
					// Agentê°??¤ì¹˜?˜ì? ?Šì? VM??ê²½ìš° ?ëŸ¬ê°?ë°œìƒ?????ˆê³ , ?´ë? Agentê°?ì¢…ë£Œ?˜ì–´ ?°ê²°??Channel???†ì„ ???ˆë‹¤.
				}
			};
		}.start();
		
		Thread.sleep(1000);
		
		try {
			jsonRes.setData(rhevmService.shutdownVirtualMachine(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM ì¢…ë£Œê°??•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ì¢…ë£Œ ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? vmIdë¥??¬ìš©?˜ì—¬ RHEV-M??VM???œê±°?œí‚¨?? 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/remove")
	public @ResponseBody SimpleJsonResponse removeVirtualMachine(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.removeVirtualMachine(dto.getHypervisorId(), dto.getVmId()));
			
			// VM???•ìƒ?ìœ¼ë¡??? œ??ê²½ìš° DB??machine_tbl?ì„œ???´ë‹¹ VM???œê±°?œë‹¤.
			machineService.deleteMachine(dto.getVmId());
			
			jsonRes.setMsg("VM ?? œê°??•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM ?? œ ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? vmIdë¥??¬ìš©?˜ì—¬ RHEV-M??VM??export ?œí‚¨?? 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/export")
	public @ResponseBody SimpleJsonResponse exportVirtualMachine(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.exportVirtualMachine(dto.getHypervisorId(), dto.getVmId()));
			jsonRes.setMsg("VM Exportê°??•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM Export ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? vmIdë¥??¬ìš©?˜ì—¬ RHEV-M??VM???œí”Œë¦¿ìœ¼ë¡?ë§Œë“ ?? 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/makeTemplate")
	public @ResponseBody SimpleJsonResponse makeTemplate(SimpleJsonResponse jsonRes, VMDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getName(), "name must not be null.");
		Assert.notNull(dto.getVmId(), "vmId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.makeTemplate(dto.getHypervisorId(), dto.getName(), dto.getVmId(), dto.getDescription()));
			jsonRes.setMsg("VM???´ìš©???œí”Œë¦??ì„±???•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("VM???´ìš©???œí”Œë¦??ì„± ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?¹ì • VM Instanceë¡?SSH Commandë¥??¸ì¶œ?œë‹¤.
	 * </pre>
	 * @param jsonRes
	 * @param targetHost
	 * @param command
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vms/backup")
	public @ResponseBody SimpleJsonResponse backupVirtualMachine(SimpleJsonResponse jsonRes, TargetHost targetHost, String command) throws Exception {
		Assert.isTrue(StringUtils.isNotEmpty(targetHost.getHost()), "host must not be null.");
		Assert.isTrue(targetHost.getPort() != null, "port must not be null.");
		Assert.isTrue(targetHost.getPort() != 0, "port must not be 0.");
		Assert.isTrue(StringUtils.isNotEmpty(targetHost.getUsername()), "username must not be null.");
		Assert.isTrue(StringUtils.isNotEmpty(targetHost.getPassword()), "password must not be null.");
		
		jsonRes.setData(SshExecUtil.executeCommand(targetHost, command));
		jsonRes.setMsg("VM Backup is starting. Please check your console");
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * ?…ë ¥ë°›ì? templateIdë¥??¬ìš©?˜ì—¬ ?œí”Œë¦¿ì„ ?œê±°?œë‹¤. 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/templates/remove")
	public @ResponseBody SimpleJsonResponse removeTemplate(SimpleJsonResponse jsonRes, TemplateDto dto) throws Exception {
		Assert.notNull(dto.getHypervisorId(), "hypervisorId must not be null.");
		Assert.notNull(dto.getTemplateId(), "templateId must not be null.");
		
		try {
			jsonRes.setData(rhevmService.removeTemplate(dto.getHypervisorId(), dto.getTemplateId()));
			jsonRes.setMsg("?œí”Œë¦??? œê°??•ìƒ?ìœ¼ë¡??”ì²­?˜ì—ˆ?µë‹ˆ?? ? ì‹œë§?ê¸°ë‹¤?¤ì£¼??‹œ??");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("?œí”Œë¦??? œ ?”ì²­ ì¤??ëŸ¬ê°?ë°œìƒ?˜ì??µë‹ˆ??<br/>?œí”Œë¦¿ì„ ì°¸ì¡°?˜ëŠ” VM???ˆëŠ”ì§??•ì¸?˜ì‹­?œì˜¤.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	
}
