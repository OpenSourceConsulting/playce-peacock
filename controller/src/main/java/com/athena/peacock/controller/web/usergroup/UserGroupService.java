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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class UserGroupService {
	
	@Autowired
	private UserGroupDao dao;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserGroupService() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertUserGroup(UserGroupDto userGroup){
		dao.insertUserGroup(userGroup);
	}
	
	public List<UserGroupDto> getUserGroupList(ExtjsGridParam gridParam){
		return dao.getUserGroupList(gridParam);
	}
	
	public int getUserGroupListTotalCount(ExtjsGridParam gridParam){
		
		return dao.getUserGroupListTotalCount(gridParam);
	}
	
	public UserGroupDto getUserGroup(int groupId){
		return dao.getUserGroup(groupId);
	}
	
	public void updateUserGroup(UserGroupDto userGroup){
		dao.updateUserGroup(userGroup);
	}
	
	public void deleteUserGroup(int groupId){
		dao.deleteUserGroup(groupId);
	}
	
	public List<UserDto> getGroupUserList(int groupId){
		return dao.getGroupUserList(groupId);
	}
	
	public void deleteUser(int groupId, int userId){
		dao.deleteUser(groupId, userId);
	}
	
	public List<UserDto> getUserListForGroup(int groupId){
		return dao.getUserListForGroup(groupId);
	}
	
	public void addUsers(int groupId, int[] user_ids){
		
		UserGroupDto[] users = new UserGroupDto[user_ids.length];
		for (int i = 0; i < user_ids.length; i++) {
			users[i] = new UserGroupDto();
			users[i].setGroup_id(groupId);
			users[i].setUser_id(user_ids[i]);
		}
		
		dao.addUsers(users);
	}

}
//end of UserGroupService.java