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
 * Ji-Woong Choi	2013. 11.  04.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm.dto;

/**
 * <pre>
 * 여러가지 RHEVM 서버들의 정보를 포함하는 클래스
 * </pre>
 * @author Ji-Woong Choi(mailto:jchoi@osci.kr)
 * @version 1.0
 */
public class TemplateDto extends RHEVBaseDto {
	private String templateId;
	private String type;
	private String status;
	private String memory;
	private int sockets;
	private int cores;
	private String creationTime;
	private String origin;
	private String display;
	private String dataCenter;
	private String cluster;
	private String os;
	private String haEnabled;
	private Integer haPriority;
	
	// paging 관련
	private int start;
	
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
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
	public String getDataCenter() {
		return dataCenter;
	}
	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}
	public String getCluster() {
		return cluster;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
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
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
}