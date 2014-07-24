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
 * Sang-cheon Park	2013. 10. 29.		First Draft.
 */
package com.athena.peacock.controller.web.lb;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class LoadBalancerDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private Integer loadBalancerId;
	private String machineId;
	private String lbName;
	private String lbDnsName;
	private Integer autoScalingId;
	
	// machine_tbl의 IP_ADDR 조회 용
	private String ipAddr;
	private String backupYn = "N";
	
	private String includeAll = "false";

	/**
	 * @return the loadBalancerId
	 */
	public Integer getLoadBalancerId() {
		return loadBalancerId;
	}

	/**
	 * @param loadBalancerId the loadBalancerId to set
	 */
	public void setLoadBalancerId(Integer loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}

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
	 * @return the lbName
	 */
	public String getLbName() {
		return lbName;
	}

	/**
	 * @param lbName the lbName to set
	 */
	public void setLbName(String lbName) {
		this.lbName = lbName;
	}

	/**
	 * @return the lbDnsName
	 */
	public String getLbDnsName() {
		return lbDnsName;
	}

	/**
	 * @param lbDnsName the lbDnsName to set
	 */
	public void setLbDnsName(String lbDnsName) {
		this.lbDnsName = lbDnsName;
	}

	/**
	 * @return the autoScalingId
	 */
	public Integer getAutoScalingId() {
		return autoScalingId;
	}

	/**
	 * @param autoScalingId the autoScalingId to set
	 */
	public void setAutoScalingId(Integer autoScalingId) {
		this.autoScalingId = autoScalingId;
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
	 * @return the backupYn
	 */
	public String getBackupYn() {
		if (backupYn == null || backupYn.equals("")) {
			backupYn = "N";
		}
		
		return backupYn;
	}

	/**
	 * @param backupYn the backupYn to set
	 */
	public void setBackupYn(String backupYn) {
		this.backupYn = backupYn;
	}

	/**
	 * @return the includeAll
	 */
	public String getIncludeAll() {
		if (includeAll == null || includeAll.equals("")) {
			includeAll = "false";
		}
		return includeAll;
	}

	/**
	 * @param includeAll the includeAll to set
	 */
	public void setIncludeAll(String includeAll) {
		this.includeAll = includeAll;
	}
}
//end of LoadBalancerDto.java