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
package com.athena.peacock.controller.web.software;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class SoftwareDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private String machineId;
	private Integer softwareId;
	private Integer installSeq;
	private String installLocation;
	private String installStat;
	private String installLog;
	private String serviceStopCmd;
	private String serviceStartCmd;
	private String description;
	private String deleteYn;

	// SoftwareRepoDto 에 존재하는 컬럼으로 ExtJS에서 Dto -> Dto 를 표시할 수 없어 추가된 필드임.
	/** 선택된 Agent에 해당 소프트웨어가 설치 되어 있는지의 여부 */
	private String installYn;
	private String softwareName;
	private String softwareVersion;
	private String softwareVendor;
	
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
	 * @return the installLocation
	 */
	public String getInstallLocation() {
		return installLocation;
	}
	
	/**
	 * @param installLocation the installLocation to set
	 */
	public void setInstallLocation(String installLocation) {
		this.installLocation = installLocation;
	}

	/**
	 * @return the installStat
	 */
	public String getInstallStat() {
		return installStat;
	}

	/**
	 * @param installStat the installStat to set
	 */
	public void setInstallStat(String installStat) {
		this.installStat = installStat;
	}

	/**
	 * @return the installLog
	 */
	public String getInstallLog() {
		return installLog;
	}

	/**
	 * @param installLog the installLog to set
	 */
	public void setInstallLog(String installLog) {
		this.installLog = installLog;
	}
	
	/**
	 * @return the serviceStopCmd
	 */
	public String getServiceStopCmd() {
		return serviceStopCmd;
	}

	/**
	 * @param serviceStopCmd the serviceStopCmd to set
	 */
	public void setServiceStopCmd(String serviceStopCmd) {
		this.serviceStopCmd = serviceStopCmd;
	}

	/**
	 * @return the serviceStartCmd
	 */
	public String getServiceStartCmd() {
		return serviceStartCmd;
	}

	/**
	 * @param serviceStartCmd the serviceStartCmd to set
	 */
	public void setServiceStartCmd(String serviceStartCmd) {
		this.serviceStartCmd = serviceStartCmd;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the installYn
	 */
	public String getInstallYn() {
		return installYn;
	}

	/**
	 * @param installYn the installYn to set
	 */
	public void setInstallYn(String installYn) {
		this.installYn = installYn;
	}

	/**
	 * @return the softwareName
	 */
	public String getSoftwareName() {
		return softwareName;
	}

	/**
	 * @param softwareName the softwareName to set
	 */
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the softwareVendor
	 */
	public String getSoftwareVendor() {
		return softwareVendor;
	}

	/**
	 * @param softwareVendor the softwareVendor to set
	 */
	public void setSoftwareVendor(String softwareVendor) {
		this.softwareVendor = softwareVendor;
	}
}
//end of SoftwareDto.java