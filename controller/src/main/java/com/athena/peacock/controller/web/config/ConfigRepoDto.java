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

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ConfigRepoDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Integer configFileId;
	private Integer softwareId;
	private String configFileSourceLocation;
	private String configFileTargetLocation;
	private String configFileName;
	private String properties;

	/**
	 * @return the configFileId
	 */
	public Integer getConfigFileId() {
		return configFileId;
	}

	/**
	 * @param configFileId the configFileId to set
	 */
	public void setConfigFileId(Integer configFileId) {
		this.configFileId = configFileId;
	}

	/**
	 * @return the softwareId
	 */
	public Integer getSoftwareId() {
		return softwareId;
	}

	/**
	 * @param softwareId the softwareId to set
	 */
	public void setSoftwareId(Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * @return the configFileSourceLocation
	 */
	public String getConfigFileSourceLocation() {
		return configFileSourceLocation;
	}

	/**
	 * @param configFileSourceLocation the configFileSourceLocation to set
	 */
	public void setConfigFileSourceLocation(String configFileSourceLocation) {
		this.configFileSourceLocation = configFileSourceLocation;
	}

	/**
	 * @return the configFileTargetLocation
	 */
	public String getConfigFileTargetLocation() {
		return configFileTargetLocation;
	}

	/**
	 * @param configFileTargetLocation the configFileTargetLocation to set
	 */
	public void setConfigFileTargetLocation(String configFileTargetLocation) {
		this.configFileTargetLocation = configFileTargetLocation;
	}

	/**
	 * @return the configFileName
	 */
	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * @param configFileName the configFileName to set
	 */
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	/**
	 * @return the properties
	 */
	public String getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}
}
//end of ConfigRepoDto.java