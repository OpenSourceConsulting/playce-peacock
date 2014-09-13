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
 * Bong-Jin Kwon	            		First Draft.
 */
package com.athena.peacock.controller.web.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.json.JSONUtil;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.common.model.TreeJsonResponse;
import com.athena.peacock.controller.web.menu.MenuDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
	
	@Autowired
	private PermissionService service;
	
	@Autowired
	private PermissionUserMapService userMapService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public PermissionController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(ExtjsGridParam gridParam){
	
		GridJsonResponse jsonRes = new GridJsonResponse();
		jsonRes.setTotal(service.getPermissionListTotalCount(gridParam));
		jsonRes.setList(service.getPermissionList(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping("/menutree")
	public @ResponseBody TreeJsonResponse menuTree(@RequestParam(value="permId") int permId){
	
		TreeJsonResponse jsonRes = new TreeJsonResponse();
		jsonRes.setChildren(service.getPermissionMenuList(permId));
		
		return jsonRes;
	}
	
	@RequestMapping("/userlist")
	public @ResponseBody GridJsonResponse userList(ExtjsGridParam gridParam, @RequestParam(value="permId") int permId){
	
		gridParam.addExParam("permId", permId);
		
		GridJsonResponse jsonRes = new GridJsonResponse();
		jsonRes.setTotal(userMapService.getPermissionUserMapListTotalCount(gridParam));
		jsonRes.setList(userMapService.getPermissionUserMapList(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping("/create")
	public @ResponseBody SimpleJsonResponse create(PermissionDto param, @RequestParam(value="permMenus") String json){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		
		List<PermissionMenuMapDto> menus = JSONUtil.jsonToList(json, PermissionMenuMapDto.class);
		
		service.createPermission(param, menus);
				
		jsonRes.setMsg("Permission이 정상적으로 생성되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	public @ResponseBody SimpleJsonResponse update(PermissionDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		service.updatePermission(param);
		
		jsonRes.setMsg("Permission이 정상적으로 수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping("/updatemenus")
	public @ResponseBody SimpleJsonResponse updateMenus(PermissionMenuMapDto param, @RequestParam(value="permMenus") String json){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		
		List<PermissionMenuMapDto> menus = JSONUtil.jsonToList(json, PermissionMenuMapDto.class);
		
		service.updateMenus(param, menus);
				
		jsonRes.setMsg("Permission Detail이 정상적으로 수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody SimpleJsonResponse delete(PermissionDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		service.deletePermission(param);
		jsonRes.setMsg("Permission이 정상적으로 삭제되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping("/insertuser")
	public @ResponseBody SimpleJsonResponse insertUser(PermissionUserMapDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		service.insertPermissionUser(param);
		jsonRes.setMsg("User가 정상적으로 등록(권한 부여)되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping("/deleteuser")
	public @ResponseBody SimpleJsonResponse deleteUser(PermissionUserMapDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		service.deletePermissionUser(param);
		jsonRes.setMsg("User가 정상적으로 삭제(권한 해제)되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping("/getPermission")
	public @ResponseBody DtoJsonResponse getPermission(PermissionDto param){
	
		DtoJsonResponse jsonRes = new DtoJsonResponse();
		
		jsonRes.setData(service.getPermission(param));
		
		return jsonRes;
	}

}
//end of PermissionController.java