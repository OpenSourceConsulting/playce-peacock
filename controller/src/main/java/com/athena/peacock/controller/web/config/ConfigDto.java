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
public class ConfigDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private String machineId;
	private Integer softwareId;
	private Integer installSeq;
	private Integer configFileId;
	private String configFileLocation;
	private String configFileName;
	private String configFileContents;
	private String deleteYn;
	private String autoRestart = "Y";

	/**
	 * @return the machineId
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
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
	 * @return the installSeq
	 */
	public Integer getInstallSeq() {
		return installSeq;
	}

	/**
	 * @param installSeq the installSeq to set
	 */
	public void setInstallSeq(Integer installSeq) {
		this.installSeq = installSeq;
	}

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
	 * @return the configFileLocation
	 */
	public String getConfigFileLocation() {
		return configFileLocation;
	}

	/**
	 * @param configFileLocation the configFileLocation to set
	 */
	public void setConfigFileLocation(String configFileLocation) {
		this.configFileLocation = configFileLocation;
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
	 * @return the configFileContents
	 */
	public String getConfigFileContents() {
		return configFileContents;
	}

	/**
	 * @param configFileContents the configFileContents to set
	 */
	public void setConfigFileContents(String configFileContents) {
		this.configFileContents = configFileContents;
	}

	/**
	 * @return the deleteYn
	 */
	public String getDeleteYn() {
		return deleteYn;
	}

	/**
	 * @param deleteYn the deleteYn to set
	 */
	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}

	/**
	 * @return the autoRestart
	 */
	public String getAutoRestart() {
		return autoRestart;
	}

	/**
	 * @param autoRestart the autoRestart to set
	 */
	public void setAutoRestart(String autoRestart) {
		this.autoRestart = autoRestart;
	}

	/**
	 * @return the configDisplayFileId
	 */
	public String getConfigDisplayFileId() {
		return configFileId + "(" + String.valueOf(getRegDt()) + ")";
	}

}
//end of ConfigDto.java