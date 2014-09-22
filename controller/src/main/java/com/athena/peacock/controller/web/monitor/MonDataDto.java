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
package com.athena.peacock.controller.web.monitor;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MonDataDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private String machineId;
	private String monFactorId;
	private int monDataId;
	private String monDataValue;
	private String timeRange = "30m";
	private String period = "1m";

	private String instanceName;
	private Integer hypervisorId;
	
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
	 * @return the monFactorId
	 */
	public String getMonFactorId() {
		return monFactorId;
	}

	/**
	 * @param monFactorId the monFactorId to set
	 */
	public void setMonFactorId(String monFactorId) {
		this.monFactorId = monFactorId;
	}

	/**
	 * @return the monDataId
	 */
	public int getMonDataId() {
		return monDataId;
	}

	/**
	 * @param monDataId the monDataId to set
	 */
	public void setMonDataId(int monDataId) {
		this.monDataId = monDataId;
	}

	/**
	 * @return the monDataValue
	 */
	public String getMonDataValue() {
		return monDataValue;
	}

	/**
	 * @param monDataValue the monDataValue to set
	 */
	public void setMonDataValue(String monDataValue) {
		this.monDataValue = monDataValue;
	}

	/**
	 * @return the timeRange
	 */
	public String getTimeRange() {
		return timeRange;
	}

	/**
	 * @param timeRange the timeRange to set
	 */
	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * @param instanceName the instanceName to set
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * @return the hypervisorId
	 */
	public Integer getHypervisorId() {
		return hypervisorId;
	}

	/**
	 * @param hypervisorId the hypervisorId to set
	 */
	public void setHypervisorId(Integer hypervisorId) {
		this.hypervisorId = hypervisorId;
	}
	
}
//end of MonDataDto.java