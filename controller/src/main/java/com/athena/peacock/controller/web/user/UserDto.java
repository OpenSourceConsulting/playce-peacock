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
 * Sang-cheon Park	2013. 8. 19.		First Draft.
 */
package com.athena.peacock.controller.web.user;

import java.util.Date;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
public class UserDto {
	
	private int user_id;
	private int role_id;
	private String login_id;
	private String passwd;
	private String user_name;
	private String dept_name;
	private String email;
	private boolean is_admin;
	private int status;
	private Date last_logon;
	private int reg_user_id;
	private Date reg_dt;
	private int upd_user_id;
	private Date upd_dt;
	
	private int groups;


	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserDto() {
		// TODO Auto-generated constructor stub
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getLast_logon() {
		return last_logon;
	}

	public void setLast_logon(Date last_logon) {
		this.last_logon = last_logon;
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
	
	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
	}

}
//end of User.java