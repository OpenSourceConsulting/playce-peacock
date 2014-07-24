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
public class LBListenerDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private Integer loadBalancerId;
	private Integer listenPort;
	private String protocol;
	private String stickinessYn;
	private Integer backendPort;

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
	 * @return the listenPort
	 */
	public Integer getListenPort() {
		return listenPort;
	}

	/**
	 * @param listenPort the listenPort to set
	 */
	public void setListenPort(Integer listenPort) {
		this.listenPort = listenPort;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the stickinessYn
	 */
	public String getStickinessYn() {
		return stickinessYn;
	}

	/**
	 * @param stickinessYn the stickinessYn to set
	 */
	public void setStickinessYn(String stickinessYn) {
		this.stickinessYn = stickinessYn;
	}

	/**
	 * @return the backendPort
	 */
	public Integer getBackendPort() {
		return backendPort;
	}

	/**
	 * @param backendPort the backendPort to set
	 */
	public void setBackendPort(Integer backendPort) {
		this.backendPort = backendPort;
	}
}
//end of LBListenerDto.java