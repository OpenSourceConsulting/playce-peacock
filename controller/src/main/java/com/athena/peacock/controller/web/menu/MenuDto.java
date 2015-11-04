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
package com.athena.peacock.controller.web.menu;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * @author Bong-Jin Kwon
 *
 */
public class MenuDto extends BaseDto {

	private int menuId;
	private String menuNm;
	private String thread;
	
	private int readYn;
	private int writeYn;
	
	@SuppressWarnings("unused")
	private boolean isRead;
	@SuppressWarnings("unused")
	private boolean isWrite;

	/**
	 * 
	 */
	public MenuDto() {
		// TODO Auto-generated constructor stub
	}

	public int getReadYn() {
		return readYn;
	}

	public void setReadYn(int readYn) {
		this.readYn = readYn;
	}

	public int getWriteYn() {
		return writeYn;
	}

	public void setWriteYn(int writeYn) {
		this.writeYn = writeYn;
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
	 * @return the menuNm
	 */
	public String getMenuNm() {
		return menuNm;
	}

	/**
	 * @param menuNm the menuNm to set
	 */
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}

	/**
	 * @return the thread
	 */
	public String getThread() {
		return thread;
	}

	/**
	 * @param thread the thread to set
	 */
	public void setThread(String thread) {
		this.thread = thread;
	}

	/**
	 * @return the isRead
	 */
	public boolean isRead() {
		if(readYn == 1){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param isRead the isRead to set
	 */
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * @return the isWrite
	 */
	public boolean isWrite() {
		if(writeYn == 1){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param isWrite the isWrite to set
	 */
	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}

}
