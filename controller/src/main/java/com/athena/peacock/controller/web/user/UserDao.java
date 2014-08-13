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
 * Bong-Ji Kwon 	2013. 10. 16.		First Draft.
 */
package com.athena.peacock.controller.web.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * UserDao
 *
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class UserDao extends AbstractBaseDao {

	/**
	 * UserDao
	 *
	 * @param
	 * @exception
	 */
	public UserDao() {
	}

	public void insertUser(UserDto user){
		sqlSession.insert("UserMapper.insertUser", user);
	}
	
	public List<UserDto> getUserList(ExtjsGridParam gridParam){
		return sqlSession.selectList("UserMapper.getUserList", gridParam);
	}
	
	public int getUserListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("UserMapper.getUserListTotalCount", gridParam);
	}
	
	public UserDto getUser(int userId){
		return sqlSession.selectOne("UserMapper.getUser", userId);
	}
	
	public void updateUser(UserDto user){
		sqlSession.update("UserMapper.updateUser", user);
	}
	
	public void deleteUser(int userId){
		sqlSession.delete("UserMapper.deleteUser", userId);
	}
	
	public UserDto getLoginUser(String loginId, String passwd){
		UserDto user = new UserDto();
		user.setLoginId(loginId);
		user.setPasswd(passwd);
		
		return sqlSession.selectOne("UserMapper.getLoginUser", user);
	}

	public void updateLastLogon(int userId) {
		sqlSession.update("UserMapper.updateLastLogon", userId);
	}
}