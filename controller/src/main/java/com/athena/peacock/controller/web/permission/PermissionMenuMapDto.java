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
public class PermissionMenuMapDto extends BaseDto {

	private int permId;
	private int menuId;
	private int readYn;
	private int writeYn;


	/**
	 * 
	 */
	public PermissionMenuMapDto() {
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
	 * @return the menuId
	 */
	public int getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the readYn
	 */
	public int getReadYn() {
		return readYn;
	}

	/**
	 * @param readYn the readYn to set
	 */
	public void setReadYn(int readYn) {
		this.readYn = readYn;
	}

	/**
	 * @return the writeYn
	 */
	public int getWriteYn() {
		return writeYn;
	}

	/**
	 * @param writeYn the writeYn to set
	 */
	public void setWriteYn(int writeYn) {
		this.writeYn = writeYn;
	}

}
