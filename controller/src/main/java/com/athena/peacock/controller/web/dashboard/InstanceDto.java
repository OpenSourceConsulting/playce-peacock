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
 * Sang-cheon Park	2014. 9. 19.		First Draft.
 */
package com.athena.peacock.controller.web.dashboard;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class InstanceDto {
	
	private String instanceName;
	private String ipAddress;
	private String vmStatus;
	private String agentStatus;
	
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
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	/**
	 * @return the vmStatus
	 */
	public String getVmStatus() {
		return vmStatus;
	}
	/**
	 * @param vmStatus the vmStatus to set
	 */
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	/**
	 * @return the agentStatus
	 */
	public String getAgentStatus() {
		return agentStatus;
	}
	/**
	 * @param agentStatus the agentStatus to set
	 */
	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}
}
//end of InstanceDto.java