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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * 사용자 그룹 관리 컨트롤러.
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/usergroup")
public class UserGroupController {
	
	@Autowired
	private UserGroupService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public UserGroupController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, ExtjsGridParam gridParam){
		
		jsonRes.setTotal(service.getUserGroupListTotalCount(gridParam));
		jsonRes.setList(service.getUserGroupList(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping("/create")
	public @ResponseBody SimpleJsonResponse create(SimpleJsonResponse jsonRes, UserGroupDto userGroup){
		
		try{
			service.insertUserGroup(userGroup);
			jsonRes.setMsg("Create success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Create fail.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	public @ResponseBody SimpleJsonResponse update(SimpleJsonResponse jsonRes, UserGroupDto userGroup){
		
		try{
			service.updateUserGroup(userGroup);
			jsonRes.setMsg("Update success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Update fail.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody SimpleJsonResponse delete(SimpleJsonResponse jsonRes, @RequestParam("group_id") int group_id){
		
		try{
			service.deleteUserGroup(group_id);
			jsonRes.setMsg("Delete success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Delete fail.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/getUserGroup")
	public @ResponseBody DtoJsonResponse getUserGroup(DtoJsonResponse jsonRes, @RequestParam("group_id") int group_id){
		
		jsonRes.setData(service.getUserGroup(group_id));
		
		return jsonRes;
	}
	
	@RequestMapping("/userlist")
	public @ResponseBody GridJsonResponse userList(GridJsonResponse jsonRes, @RequestParam("group_id") int group_id){
		
		List<UserDto> list = service.getGroupUserList(group_id);
		
		jsonRes.setTotal(list.size());
		jsonRes.setList(list);
		
		return jsonRes;
	}
	
	@RequestMapping("/userdelete")
	public @ResponseBody SimpleJsonResponse grpDelete(SimpleJsonResponse jsonRes, @RequestParam("user_id") int user_id, @RequestParam("group_id") int group_id){
		
		try{
			service.deleteUser(group_id, user_id);
			jsonRes.setMsg("Delete success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Delete fail.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/adduserlist")
	public @ResponseBody GridJsonResponse addUserList(GridJsonResponse jsonRes, @RequestParam("group_id") int group_id){
		
		List<UserDto> list = service.getUserListForGroup(group_id);
		
		jsonRes.setTotal(list.size());
		jsonRes.setList(list);
		
		return jsonRes;
	}
	
	@RequestMapping("/adduser")
	public @ResponseBody SimpleJsonResponse adduser(SimpleJsonResponse jsonRes, @RequestParam("user_ids") int[] user_ids, @RequestParam("group_id") int group_id){
		
		try{
			service.addUsers(group_id, user_ids);
			jsonRes.setMsg("Add success.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Add fail.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}

}
//end of UserGroupController.java