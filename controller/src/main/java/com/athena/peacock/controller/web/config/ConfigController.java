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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

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
	
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, ConfigDto config) throws Exception {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		
		jsonRes.setTotal(configService.getConfigListCnt(config));
		jsonRes.setList(configService.getConfigList(config));
		
		return jsonRes;
	}
	
	@RequestMapping("/repo_list")
	public @ResponseBody GridJsonResponse repoList(GridJsonResponse jsonRes, ConfigRepoDto configRepo) throws Exception {
		Assert.notNull(configRepo.getSoftwareId(), "softwareId can not be null.");
		
		jsonRes.setTotal(configRepoService.getConfigRepoListCnt(configRepo));
		jsonRes.setList(configRepoService.getConfigRepoList(configRepo));
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	public @ResponseBody SimpleJsonResponse update(SimpleJsonResponse jsonRes, ConfigDto config) {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getConfigFileId(), "configFileId can not be null.");
		
		try {
			configService.updateConfig(config);
			jsonRes.setMsg("Update success.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Update fail.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/getConfig")
	public @ResponseBody DtoJsonResponse getConfig(DtoJsonResponse jsonRes, ConfigDto config) throws Exception {
		Assert.notNull(config.getMachineId(), "machineId can not be null.");
		Assert.notNull(config.getSoftwareId(), "softwareId can not be null.");
		Assert.notNull(config.getConfigFileId(), "configFileId can not be null.");
		
		jsonRes.setData(configService.getConfig(config));
		
		return jsonRes;
	}

}
//end of ConfigController.java