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
package com.athena.peacock.controller.web.kvm.dto;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DomainDto extends KVMBaseDto {

	private int seq;
	private String type;
	private String status;
	private long memory;
	private int vcpu;
	private String osArch;
	private String osMachine;
	private String osType;
	private String sourceFile;
	
//	private String startTime;
//	private String creationTime;
	
	// for create a domain
	private NetworkDto networkDto;
	private DiskDto diskDto;
	
	// machine_additional_info_tbl 관련 필드
	private String hostName;
	private String ipAddress;
	private String netmask;
	private String gateway;
	private String nameServer;
	private String applyYn;
	private String sshPort;
	private String sshUsername;
	private String sshPassword;
	private String sshKeyFile;
    private CommonsMultipartFile keyFile;
    
    // paging 관련
    private int start;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public int getVcpu() {
		return vcpu;
	}

	public void setVcpu(int vcpu) {
		this.vcpu = vcpu;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public String getOsMachine() {
		return osMachine;
	}

	public void setOsMachine(String osMachine) {
		this.osMachine = osMachine;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public NetworkDto getNetworkDto() {
		if (networkDto == null) {
			networkDto = new NetworkDto();
		}
		
		return networkDto;
	}

	public void setNetworkDto(NetworkDto networkDto) {
		this.networkDto = networkDto;
	}

	public DiskDto getDiskDto() {
		if (diskDto == null) {
			diskDto = new DiskDto();
		}
		
		return diskDto;
	}

	public void setDiskDto(DiskDto diskDto) {
		this.diskDto = diskDto;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getNameServer() {
		return nameServer;
	}

	public void setNameServer(String nameServer) {
		this.nameServer = nameServer;
	}

	public String getApplyYn() {
		return applyYn;
	}

	public void setApplyYn(String applyYn) {
		this.applyYn = applyYn;
	}

	public String getSshPort() {
		return sshPort;
	}

	public void setSshPort(String sshPort) {
		this.sshPort = sshPort;
	}

	public String getSshUsername() {
		return sshUsername;
	}

	public void setSshUsername(String sshUsername) {
		this.sshUsername = sshUsername;
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}

	public String getSshKeyFile() {
		return sshKeyFile;
	}

	public void setSshKeyFile(String sshKeyFile) {
		this.sshKeyFile = sshKeyFile;
	}

	public CommonsMultipartFile getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(CommonsMultipartFile keyFile) {
		this.keyFile = keyFile;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
//end of DomainDto.java