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
 * Ji-Woong Choi	2013. 10. 23.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm.dto;

import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * <pre>
 * RHEV VM 목록
 * </pre>
 * @author Ji-Woong Choi
 * @version 1.0
 */

public class VMDto extends RHEVBaseDto {
	private String type;
	private String status;
	private String dataCenter;
	private String domain;
	private String cluster;
	private String host;
	private String os;
	private String template;
	private String memory;
	private int sockets;
	private int cores;
	private String priority;
	private String origin;
	private String display;
	private String ipAddr;
	private String boot;
	private String haEnabled;
	private Integer haPriority;
	
	private String startTime;
	private String creationTime;
	
	// machine_additional_info_tbl 관련 필드
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
	public String getDataCenter() {
		return dataCenter;
	}
	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getCluster() {
		return cluster;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public int getSockets() {
		return sockets;
	}
	public void setSockets(int sockets) {
		this.sockets = sockets;
	}
	public int getCores() {
		return cores;
	}
	public void setCores(int cores) {
		this.cores = cores;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getBoot() {
		return boot;
	}
	public void setBoot(String boot) {
		this.boot = boot;
	}
	public String getHaEnabled() {
		return haEnabled;
	}
	public void setHaEnabled(String haEnabled) {
		this.haEnabled = haEnabled;
	}
	public Integer getHaPriority() {
		return haPriority;
	}
	public void setHaPriority(Integer haPriority) {
		this.haPriority = haPriority;
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
