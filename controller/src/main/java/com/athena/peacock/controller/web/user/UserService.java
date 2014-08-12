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
 * Bong-Jin Kwon	2013. 9. 25.		First Draft.
 */
package com.athena.peacock.controller.web.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class UserService {

	@Autowired
	private UserDao dao;
	
	public UserService() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertUser(UserDto user){
		dao.insertUser(user);
	}
	
	public List<UserDto> getUserList(ExtjsGridParam gridParam){
		return dao.getUserList(gridParam);
	}
	
	public int getUserListTotalCount(ExtjsGridParam gridParam){
		
		return dao.getUserListTotalCount(gridParam);
	}
	
	public UserDto getUser(int userId){
		return dao.getUser(userId);
	}
	
	public void updateUser(UserDto user){
		dao.updateUser(user);
	}
	
	public void deleteUser(int userId){
		dao.deleteUser(userId);
	}
	
	public UserDto getLoginUser(String loginId, String passwd){
		UserDto user = dao.getLoginUser(loginId, passwd);
		
		if (user != null) {
			dao.updateLastLogon(user.getUser_id());
			return user;
		} else {
			return null;
		}
	}
}
//end of UserService.java