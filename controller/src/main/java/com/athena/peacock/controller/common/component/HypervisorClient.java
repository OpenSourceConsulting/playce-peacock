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
 * Sang-cheon Park	2015. 8. 16.		First Draft.
 */
package com.athena.peacock.controller.common.component;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public abstract class HypervisorClient {

	protected Integer hypervisorId;
	protected String rhevmName;
	protected String protocol;
	protected String host;
	protected String domain;
	protected String port;
	protected String username;
	protected String password;
	
	public Integer getHypervisorId() {
		return hypervisorId;
	}

	public void setHypervisorId(Integer hypervisorId) {
		this.hypervisorId = hypervisorId;
	}

	public String getRhevmName() {
		return rhevmName;
	}

	public void setRhevmName(String rhevmName) {
		this.rhevmName = rhevmName;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public String getDomain() {
		return domain;
	}

	public String getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
//end of HypervisorClient.java