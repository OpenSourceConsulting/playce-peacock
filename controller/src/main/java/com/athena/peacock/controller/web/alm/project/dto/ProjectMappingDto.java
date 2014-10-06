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
 * Sang-cheon Park	2013. 8. 19.		First Draft.
 */
package com.athena.peacock.controller.web.alm.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * <pre>
 * Project Mapping Dto
 * </pre>
 * 
 * @author Dave
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectMappingDto {

	private static final long serialVersionUID = -1083153050593982734L;

	private String projectCode;
	private int mappingType;
	private String mappingCode;
	private String mappingExecution;
	private String mappingPermission;
	private String createTime;
	private String startTime;
	private String endTime;
	private String status;
	private String exitCode;
	private String exitMessage;
	private String lastUpdated;

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public int getMappingType() {
		return mappingType;
	}

	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}

	public String getMappingCode() {
		return mappingCode;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	public String getMappingPermission() {
		return mappingPermission;
	}

	public void setMappingPermission(String mappingPermission) {
		this.mappingPermission = mappingPermission;
	}

	public String getMappingExecution() {
		return mappingExecution;
	}

	public void setMappingExecution(String mappingExecution) {
		this.mappingExecution = mappingExecution;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExitCode() {
		return exitCode;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	public String getExitMessage() {
		return exitMessage;
	}

	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
// end of User.java