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
 * Sang-cheon Park	2014. 8. 12.		First Draft.
 */
package com.athena.peacock.controller.web.hypervisor;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class HypervisorDto  extends BaseDto {

	private static final long serialVersionUID = -831794935273475173L;
	
	private Integer hypervisorId;
	private String hypervisorType;
	private String rhevmName;
	private String rhevmProtocol;
	private String rhevmPort;
	private String rhevmDomain;
	private String rhevmHost;
	private String rhevmUsername;
	private String rhevmPassword;

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

	/**
	 * @return the hypervisorType
	 */
	public String getHypervisorType() {
		return hypervisorType;
	}

	/**
	 * @param hypervisorType the hypervisorType to set
	 */
	public void setHypervisorType(String hypervisorType) {
		this.hypervisorType = hypervisorType;
	}

	/**
	 * @return the rhevmName
	 */
	public String getRhevmName() {
		return rhevmName;
	}

	/**
	 * @param rhevmName the rhevmName to set
	 */
	public void setRhevmName(String rhevmName) {
		this.rhevmName = rhevmName;
	}

	/**
	 * @return the rhevmProtocol
	 */
	public String getRhevmProtocol() {
		return rhevmProtocol;
	}

	/**
	 * @param rhevmProtocol the rhevmProtocol to set
	 */
	public void setRhevmProtocol(String rhevmProtocol) {
		this.rhevmProtocol = rhevmProtocol;
	}

	/**
	 * @return the rhevmPort
	 */
	public String getRhevmPort() {
		return rhevmPort;
	}

	/**
	 * @param rhevmPort the rhevmPort to set
	 */
	public void setRhevmPort(String rhevmPort) {
		this.rhevmPort = rhevmPort;
	}

	/**
	 * @return the rhevmDomain
	 */
	public String getRhevmDomain() {
		return rhevmDomain;
	}

	/**
	 * @param rhevmDomain the rhevmDomain to set
	 */
	public void setRhevmDomain(String rhevmDomain) {
		this.rhevmDomain = rhevmDomain;
	}

	/**
	 * @return the rhevmHost
	 */
	public String getRhevmHost() {
		return rhevmHost;
	}

	/**
	 * @param rhevmHost the rhevmHost to set
	 */
	public void setRhevmHost(String rhevmHost) {
		this.rhevmHost = rhevmHost;
	}

	/**
	 * @return the rhevmUsername
	 */
	public String getRhevmUsername() {
		return rhevmUsername;
	}

	/**
	 * @param rhevmUsername the rhevmUsername to set
	 */
	public void setRhevmUsername(String rhevmUsername) {
		this.rhevmUsername = rhevmUsername;
	}

	/**
	 * @return the rhevmPassword
	 */
	public String getRhevmPassword() {
		return rhevmPassword;
	}

	/**
	 * @param rhevmPassword the rhevmPassword to set
	 */
	public void setRhevmPassword(String rhevmPassword) {
		this.rhevmPassword = rhevmPassword;
	}
}
//end of HypervisorDto.java