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
 * Sang-cheon Park	2013. 8. 25.		First Draft.
 */
package com.athena.peacock.controller.web.machine;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MachineDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private String machineId;
	private String machineMacAddr;
	private String isVm;
	private String osName;
	private String osVer;
	private String osArch;
	private String cpuClock;
	private String cpuNum;
	private String memSize;
	private String ipAddr;
	private String hostName;
	private String sshPort;
	private String sshUsername;
	private String sshPassword;
	private String sshKeyFile;
	private String deleteYn;
	
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
	 * @return the machineMacAddr
	 */
	public String getMachineMacAddr() {
		return machineMacAddr;
	}

	/**
	 * @param machineMacAddr the machineMacAddr to set
	 */
	public void setMachineMacAddr(String machineMacAddr) {
		this.machineMacAddr = machineMacAddr;
	}

	/**
	 * @return the isVm
	 */
	public String getIsVm() {
		return isVm;
	}

	/**
	 * @param isVm the isVm to set
	 */
	public void setIsVm(String isVm) {
		this.isVm = isVm;
	}

	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * @param osName the osName to set
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}

	/**
	 * @return the osVer
	 */
	public String getOsVer() {
		return osVer;
	}

	/**
	 * @param osVer the osVer to set
	 */
	public void setOsVer(String osVer) {
		this.osVer = osVer;
	}

	/**
	 * @return the osArch
	 */
	public String getOsArch() {
		return osArch;
	}

	/**
	 * @param osArch the osArch to set
	 */
	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	/**
	 * @return the cpuClock
	 */
	public String getCpuClock() {
		return cpuClock;
	}

	/**
	 * @param cpuClock the cpuClock to set
	 */
	public void setCpuClock(String cpuClock) {
		this.cpuClock = cpuClock;
	}

	/**
	 * @return the cpuNum
	 */
	public String getCpuNum() {
		return cpuNum;
	}

	/**
	 * @param cpuNum the cpuNum to set
	 */
	public void setCpuNum(String cpuNum) {
		this.cpuNum = cpuNum;
	}

	/**
	 * @return the memSize
	 */
	public String getMemSize() {
		return memSize;
	}

	/**
	 * @param memSize the memSize to set
	 */
	public void setMemSize(String memSize) {
		this.memSize = memSize;
	}

	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * @param ipAddr the ipAddr to set
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the sshPort
	 */
	public String getSshPort() {
		return sshPort;
	}

	/**
	 * @param sshPort the sshPort to set
	 */
	public void setSshPort(String sshPort) {
		this.sshPort = sshPort;
	}

	/**
	 * @return the sshUsername
	 */
	public String getSshUsername() {
		return sshUsername;
	}

	/**
	 * @param sshUsername the sshUsername to set
	 */
	public void setSshUsername(String sshUsername) {
		this.sshUsername = sshUsername;
	}

	/**
	 * @return the sshPassword
	 */
	public String getSshPassword() {
		return sshPassword;
	}

	/**
	 * @param sshPassword the sshPassword to set
	 */
	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}

	/**
	 * @return the sshKeyFile
	 */
	public String getSshKeyFile() {
		return sshKeyFile;
	}

	/**
	 * @param sshKeyFile the sshKeyFile to set
	 */
	public void setSshKeyFile(String sshKeyFile) {
		this.sshKeyFile = sshKeyFile;
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
}
//end of MachineDto.java