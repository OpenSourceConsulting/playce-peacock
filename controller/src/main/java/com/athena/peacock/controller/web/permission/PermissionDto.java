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
 * Bong-Jin Kwon	2014. 9. 11.		First Draft.
 */
package com.athena.peacock.controller.web.permission;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * @author Bong-Jin Kwon
 *
 */
public class PermissionDto extends BaseDto {

	private static final long serialVersionUID = 7242080619020077737L;
	
	private int permId;
	private String permNm;
	private int users;
	
	/**
	 * 
	 */
	public PermissionDto() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the permId
	 */
	public int getPermId() {
		return permId;
	}

	/**
	 * @param permId the permId to set
	 */
	public void setPermId(int permId) {
		this.permId = permId;
	}

	/**
	 * @return the permNm
	 */
	public String getPermNm() {
		return permNm;
	}

	/**
	 * @param permNm the permNm to set
	 */
	public void setPermNm(String permNm) {
		this.permNm = permNm;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

}
