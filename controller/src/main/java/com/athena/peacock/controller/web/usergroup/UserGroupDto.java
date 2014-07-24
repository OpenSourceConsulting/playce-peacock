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
 * Bong-Jin Kwon	2013. 10. 2.		First Draft.
 */
package com.athena.peacock.controller.web.usergroup;

import java.util.Date;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
public class UserGroupDto {
	
	private int group_id;
	private String group_name;
	private String description;
	private int reg_user_id;
	private Date reg_dt;
	private int upd_user_id;
	private Date upd_dt;
	
	private int users;
	private int user_id;

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserGroupDto() {
		// TODO Auto-generated constructor stub
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReg_user_id() {
		return reg_user_id;
	}

	public void setReg_user_id(int reg_user_id) {
		this.reg_user_id = reg_user_id;
	}

	public Date getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(Date reg_dt) {
		this.reg_dt = reg_dt;
	}

	public int getUpd_user_id() {
		return upd_user_id;
	}

	public void setUpd_user_id(int upd_user_id) {
		this.upd_user_id = upd_user_id;
	}

	public Date getUpd_dt() {
		return upd_dt;
	}

	public void setUpd_dt(Date upd_dt) {
		this.upd_dt = upd_dt;
	}

}
//end of UserGroupDto.java