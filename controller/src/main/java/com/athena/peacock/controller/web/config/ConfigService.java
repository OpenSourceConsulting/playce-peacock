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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("configService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class ConfigService {
    
	@Inject
	@Named("configDao")
	private ConfigDao configDao;

	public List<ConfigDto> getConfigFileNames(ConfigDto config) {
		return configDao.getConfigFileNames(config);
	}

	public List<ConfigDto> getConfigFileVersions(ConfigDto config) {
		return configDao.getConfigFileVersions(config);
	}
	
	public ConfigDto getConfig(ConfigDto config) throws Exception {
		return configDao.getConfig(config);
	}
	
	public void insertConfigList(List<ConfigDto> configList) throws Exception {
		for (ConfigDto config : configList) {
			configDao.insertConfig(config);
		}
	}
	
	public void insertConfig(ConfigDto config) throws Exception {
		configDao.insertConfig(config);
	}
	
	public void updateConfig(ConfigDto config) throws Exception {
		configDao.deleteConfig(config);
		
		config.setDeleteYn("N");
		configDao.insertConfig(config);
	}
	
	public void deleteConfig(ConfigDto config) throws Exception {
		configDao.deleteConfig(config);
	}

	public int getConfigListCnt(ConfigDto config) throws Exception {
		return configDao.getConfigListCnt(config);
	}
	
	public List<ConfigDto> getConfigList(ConfigDto config) throws Exception {
		return configDao.getConfigList(config);
	}

}
//end of ConfigService.java