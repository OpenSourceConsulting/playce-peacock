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
 * Bong-Jin Kwon	2013. 9. 24.		First Draft.
 */
package com.athena.peacock.controller.web.user;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.usergroup.UserGroupDto;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	public static final String SESSION_USER_KEY = "loginUser";
	
	@Autowired
	private UserService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, ExtjsGridParam gridParam){
		
		jsonRes.setTotal(service.getUserListTotalCount(gridParam));
		jsonRes.setList(service.getUserList(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping("/create")
	public @ResponseBody SimpleJsonResponse create(SimpleJsonResponse jsonRes, UserDto user){
		
		try{
			service.insertUser(user);
			jsonRes.setMsg("Create success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Create fail.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	public @ResponseBody SimpleJsonResponse update(SimpleJsonResponse jsonRes, UserDto user){
		
		try{
			service.updateUser(user);
			jsonRes.setMsg("Update success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Update fail.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody SimpleJsonResponse delete(SimpleJsonResponse jsonRes, @RequestParam("user_id") int user_id){
		
		try{
			service.deleteUser(user_id);
			jsonRes.setMsg("Delete success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Delete fail.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/getUser")
	public @ResponseBody DtoJsonResponse getUser(DtoJsonResponse jsonRes, @RequestParam("user_id") int user_id){
		
		jsonRes.setData(service.getUser(user_id));
		
		return jsonRes;
	}
	
	@RequestMapping("/login")
	public @ResponseBody SimpleJsonResponse login(SimpleJsonResponse jsonRes, @RequestParam("login_id") String login_id, @RequestParam("passwd") String passwd, HttpSession session){
		
		UserDto user = service.getLoginUser(login_id, passwd);
		
		if(user == null){
			jsonRes.setSuccess(false);
			jsonRes.setMsg("login id 또는 비밀번호가 잘못되었습니다.");
		}else{
			jsonRes.setData(user);
			session.setAttribute(SESSION_USER_KEY, user);
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/logout")
	public @ResponseBody SimpleJsonResponse logout(SimpleJsonResponse jsonRes, HttpSession session){
		
		session.removeAttribute(SESSION_USER_KEY);
		
		jsonRes.setMsg("logout success.");
		
		return jsonRes;
	}
	
	@RequestMapping("/grplist")
	public @ResponseBody GridJsonResponse grpList(GridJsonResponse jsonRes, @RequestParam("user_id") int user_id){
		
		List<UserGroupDto> list = service.getUserGroupList(user_id);
		
		jsonRes.setTotal(list.size());
		jsonRes.setList(list);
		
		return jsonRes;
	}
	
	@RequestMapping("/grpdelete")
	public @ResponseBody SimpleJsonResponse grpDelete(SimpleJsonResponse jsonRes, @RequestParam("user_id") int user_id, @RequestParam("group_id") int group_id){
		
		try{
			service.deleteGroup(user_id, group_id);
			jsonRes.setMsg("Delete success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Delete fail.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}

}
//end of UserController.java