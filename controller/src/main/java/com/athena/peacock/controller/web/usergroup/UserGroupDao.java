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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class UserGroupDao extends AbstractBaseDao {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserGroupDao() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertUserGroup(UserGroupDto userGroup){
		sqlSession.insert("UserGroupMapper.insertUserGroup", userGroup);
	}
	
	public List<UserGroupDto> getUserGroupList(ExtjsGridParam gridParam){
		return sqlSession.selectList("UserGroupMapper.getUserGroupList", gridParam);
	}
	
	public int getUserGroupListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("UserGroupMapper.getUserGroupListTotalCount", gridParam);
	}
	
	public UserGroupDto getUserGroup(int groupId){
		return sqlSession.selectOne("UserGroupMapper.getUserGroup", groupId);
	}
	
	public void updateUserGroup(UserGroupDto userGroup){
		sqlSession.update("UserGroupMapper.updateUserGroup", userGroup);
	}
	
	public void deleteUserGroup(int groupId){
		sqlSession.delete("UserGroupMapper.deleteUserGroup", groupId);
	}
	
	public List<UserDto> getGroupUserList(int groupId){
		return sqlSession.selectList("UserGroupMapper.getGroupUserList", groupId);
	}
	
	public void deleteUser(int groupId, int userId){
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("user_id", userId);
		paramMap.put("group_id", groupId);
		
		sqlSession.delete("UserGroupMapper.deleteUser", paramMap);
	}
	
	public List<UserDto> getUserListForGroup(int groupId){
		return sqlSession.selectList("UserGroupMapper.getUserListForGroup", groupId);
	}
	
	public void addUsers(UserGroupDto[] users){
		for (int i = 0; i < users.length; i++) {
			sqlSession.insert("UserGroupMapper.addUser", users[i]);
		}
	}

}
//end of UserGroupDao.java