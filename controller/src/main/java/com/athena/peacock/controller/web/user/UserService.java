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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.athena.peacock.common.provider.AppContext;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.menu.MenuDto;
import com.athena.peacock.controller.web.permission.PermissionDao;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class UserService implements UserDetailsService {
	
	public static final String SUPER_USER = "admin";

	@Autowired
	private UserDao dao;
	
	@Autowired
	private PermissionDao permDao;
	
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
			dao.updateLastLogon(user.getUserId());
			return user;
		} else {
			return null;
		}
	}
	
	public void updateLastLogon(int userId){
		dao.updateLastLogon(userId);
	}

	@Override
	public UserDetails loadUserByUsername(String username)	throws UsernameNotFoundException {
		
		UserDto userDetails = dao.getLoginUser(username);
		
		if(SUPER_USER.equals(username)){
			userDetails.addAuthority(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}else if(username != null){
			userDetails.addAuthority(new SimpleGrantedAuthority("ROLE_USER"));
		}
		
		List<MenuDto> dblist  = permDao.getUserMenuPermissions(userDetails.getUserId());
		
		for (MenuDto menuDto : dblist) {
			
			userDetails.addAuthority(new SimpleGrantedAuthority("ROLE_"+menuDto.getThread()+"_READ"));
			if(menuDto.getWriteYn() > 0){
				userDetails.addAuthority(new SimpleGrantedAuthority("ROLE_"+menuDto.getThread()+"_WRITE"));
			}
		}
		
		return userDetails;
	}
	
	public static int getLoginUserId(){
		
		UserDto userDetails = getLoginUser();
		
		return userDetails.getUserId();
	}
	
	public static UserDto getLoginUser(){
		SecurityContext secContext = SecurityContextHolder.getContext();
		
		System.err.println("secContext.getAuthentication().getPrincipal() : " + secContext.getAuthentication().getPrincipal());
		
		UserDto userDetails = null;
		if (secContext.getAuthentication().getPrincipal() instanceof UserDto) {
			userDetails = (UserDto)secContext.getAuthentication().getPrincipal();
		} else {
			userDetails = ((UserDao) AppContext.getBean(UserDao.class)).getLoginUser((String) secContext.getAuthentication().getPrincipal());
		}
		
		return userDetails;
	}
}
//end of UserService.java