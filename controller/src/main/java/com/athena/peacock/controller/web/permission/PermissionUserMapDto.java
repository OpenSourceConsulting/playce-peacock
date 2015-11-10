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
 * Bong-Jin Kwon	2014. 9. 12.		First Draft.
 */
package com.athena.peacock.controller.web.permission;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * @author Bong-Jin Kwon
 *
 */
public class PermissionUserMapDto extends BaseDto {

	private int permId;
	private int userId;

	/**
	 * 
	 */
	public PermissionUserMapDto() {
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
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
