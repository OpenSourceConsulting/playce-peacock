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
 * Sang-cheon Park	2013. 10. 16.		First Draft.
 */
package com.athena.peacock.controller.web.config;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.common.core.action.FileWriteAction;
import com.athena.peacock.common.core.action.ShellAction;
import com.athena.peacock.common.core.command.Command;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.common.netty.message.ProvisioningResponseMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.software.SoftwareDto;
import com.athena.peacock.controller.web.software.SoftwareService;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    protected final Logger logger = LoggerFactory.getLogger(ConfigController.class);

	@Inject
	@Named("configService")
	private ConfigService configService;

	@Inject
	@Named("configRepoService")
	private ConfigRepoService configRepoService;
	
	@Inject
	@Named("softwareService")
	private SoftwareService softwareService;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	/**
	 * <pre>
	 * 선택된 소프트웨어의 config 파일 목록을 조회한다.
	 * </pre>
	 * @param config
	 * @return
	 */
	@RequestMapping("/getConfigFileNames")
	public @ResponseBody List<ConfigDto> getConfigFileNames(ConfigDto config) {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getInstallSeq(), "installSeq can not be null.");
		
		return configService.getConfigFileNames(config);
	}
	
	/**
	 * <pre>
	 * 선택된 config 파일에 대한 버전 목록을 조회한다.
	 * config_tbl의 CONFIG_FILE_CONTENTS 컬럼 타입이 TEXT로 전체 목록 조회 시 성능이 저하될 수 있으므로
	 * CONFIG_FILE_ID와 REG_DT만 조회한다.
	 * </pre>
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getConfigFileVersions")
	public @ResponseBody GridJsonResponse getConfigFileVersions(GridJsonResponse jsonRes, @RequestParam(required=false, value="compare") String compare, ConfigDto config) throws Exception {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getInstallSeq(), "installSeq can not be null.");
		Assert.notNull(config.getConfigFileName(), "configFileName can not be null.");

		List<ConfigDto> configList = configService.getConfigFileVersions(config);
		
		if(compare != null){
			ConfigDto version = new ConfigDto();
			version.setConfigFileId(ConfigDto.SYS_CONFIG);
			version.setConfigFileName("System Config");
			configList.add(0, version);
		}
		
		jsonRes.setTotal(configList.size());
		jsonRes.setList(configList);
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 선택된 config 파일의 특정 버전에 대한 상세 내용을 조회한다.
	 * </pre>
	 * @param jsonRes
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getConfigDetail")
	public @ResponseBody DtoJsonResponse getConfig(DtoJsonResponse jsonRes, ConfigDto config) throws Exception {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getInstallSeq(), "installSeq can not be null.");
		Assert.notNull(config.getConfigFileId(), "configFileId can not be null.");
		
		jsonRes.setData(configService.getConfig(config));
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * 선택된 config 파일에 대해 시스템에 설정되어 있는 내용을 조회한다.
	 * </pre>
	 * @param jsonRes
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getSystemConfig")
	public @ResponseBody DtoJsonResponse getSystemConfig(DtoJsonResponse jsonRes, ConfigDto config) throws Exception {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getConfigFileLocation(), "configFileLocation can not be null.");
		Assert.notNull(config.getConfigFileName(), "configFileName can not be null.");
		
		try {
			ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
			cmdMsg.setAgentId(config.getMachineId());
			cmdMsg.setBlocking(true);

			Command command = new Command("Get Config File Contents");
			int sequence = 0;
			
			ShellAction action = new ShellAction(sequence++);
			
			action.setCommand("cat");
			action.addArguments(config.getConfigFileLocation() + "/" + config.getConfigFileName());

			command.addAction(action);
			
			cmdMsg.addCommand(command);

			PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
			ProvisioningResponseMessage response = peacockTransmitter.sendMessage(datagram);
			
			jsonRes.setData(response.getResults().get(0));
		} catch (Exception e) {
			String message = config.getConfigFileName() + " 조회 중 에러가 발생하였습니다.";
			
			if (e.getMessage() != null && e.getMessage().equals("Channel is null.")) {
				message += "<br/>Instance와의 연결을 확인하십시오.";
			}
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg(message);
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/updateConfig")
	public @ResponseBody SimpleJsonResponse updateConfig(SimpleJsonResponse jsonRes, ConfigDto config) {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getInstallSeq(), "installSeq can not be null.");
		Assert.notNull(config.getConfigFileId(), "configFileId can not be null.");
		
		try {
			ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
			cmdMsg.setAgentId(config.getMachineId());
			cmdMsg.setBlocking(true);
			
			if (config.getConfigFileLocation() == null || config.getConfigFileLocation().equals("")) {
				ConfigDto c = configService.getConfig(config);
				config.setConfigFileLocation(c.getConfigFileLocation());
			}

			Command command = new Command("Save Config File Contents");
			int sequence = 0;
			
			FileWriteAction action = new FileWriteAction(sequence++);
			action.setFileName(config.getConfigFileLocation() + "/" + config.getConfigFileName());
			action.setContents(config.getConfigFileContents());
			
			command.addAction(action);
			
			cmdMsg.addCommand(command);

			PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
			peacockTransmitter.sendMessage(datagram);

			if (config.getAutoRestart().toUpperCase().equals("Y")) {
				// softwareId에 해당하는 software 정보를 조회한다.
				SoftwareDto software = new SoftwareDto();
				software.setMachineId(config.getMachineId());
				software.setSoftwareId(config.getSoftwareId());
				software.setInstallSeq(config.getInstallSeq());
				software = softwareService.getSoftware(software);
				
				String stopCmd = software.getServiceStopCmd();
				String startCmd = software.getServiceStartCmd();
				
				String workingDir = null;
				String cmd = null;
				String args = null;
				
				command = new Command("Restart Service");
				sequence = 0;
				ShellAction s_action = null;

				/**
				 * Stop Service
				 */
				if (stopCmd.split(",")[0].split(":").length == 2) {
					workingDir = stopCmd.split(",")[0].split(":")[1];
				}
				if (stopCmd.split(",")[1].split(":").length == 2) {
					cmd = stopCmd.split(",")[1].split(":")[1];
				}
				if (stopCmd.split(",")[2].split(":").length == 2) {
					args = stopCmd.split(",")[2].split(":")[1];
				}

				s_action = new ShellAction(sequence++);
				if (workingDir != null) {
					s_action.setWorkingDiretory(workingDir);
				}
				s_action.setCommand(cmd);
				if (args != null) {
					s_action.addArguments(args);
				}
				command.addAction(s_action);

				s_action = new ShellAction(sequence++);
				s_action.setCommand("sleep");
				s_action.addArguments("5");
				command.addAction(s_action);

				/**
				 *  Start Service
				 */
				if (startCmd.split(",")[0].split(":").length == 2) {
					workingDir = startCmd.split(",")[0].split(":")[1];
				}
				if (startCmd.split(",")[1].split(":").length == 2) {
					cmd = startCmd.split(",")[1].split(":")[1];
				}
				if (startCmd.split(",")[2].split(":").length == 2) {
					args = startCmd.split(",")[2].split(":")[1];
				}

				s_action = new ShellAction(sequence++);
				if (workingDir != null) {
					s_action.setWorkingDiretory(workingDir);
				}
				s_action.setCommand(cmd);
				if (args != null) {
					s_action.addArguments(args);
				}
				command.addAction(s_action);
				
				cmdMsg.addCommand(command);

				datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
				peacockTransmitter.sendMessage(datagram);
			}
			
			jsonRes.setMsg(config.getConfigFileName() + " 파일이 정상적으로 수정되었습니다.");
		} catch (Exception e) {
			String message = config.getConfigFileName() + " 저장 중 에러가 발생하였습니다.";
			
			if (e.getMessage() != null && e.getMessage().equals("Channel is null.")) {
				message += "<br/>Instance와의 연결을 확인하십시오.";
			}
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg(message);
			
			logger.error("Unhandled Expeption has occurred. ", e);
			
			return jsonRes;
		}

		try {
			configService.updateConfig(config);
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("DB 저장 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/diff")
	public String diff(Model model, ConfigDto config) throws Exception {
		
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getInstallSeq(), "installSeq can not be null.");
		Assert.notNull(config.getConfigFileId(), "configFileId can not be null.");
		
		Assert.notNull(config.getCompareVersion(), "compareVersion can not be null.");
		
		model.addAttribute("config1", configService.getConfig(config).getConfigFileContents());
		
		
		if(ConfigDto.SYS_CONFIG == config.getCompareVersion()){
			
			DtoJsonResponse res = getSystemConfig(new DtoJsonResponse(), config);
			model.addAttribute("config2", res.getData());
		}else{
			
			config.setConfigFileId(Integer.valueOf(config.getCompareVersion()));
			model.addAttribute("config2", configService.getConfig(config).getConfigFileContents());
		}
		
		
		return "SoftwareConfigDiff";
	}

}
//end of ConfigController.java