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
package com.athena.peacock.controller.web.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.TreeJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private MenuService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public MenuController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("/alltree")
	public @ResponseBody TreeJsonResponse allTree(){
	
		TreeJsonResponse jsonRes = new TreeJsonResponse();
		jsonRes.setChildren(service.getAllMenuTree());
		
		return jsonRes;
	}
	
	/*
	 * 아래소스는 자동 생성한 CRUD 소스
	 * 
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(ExtjsGridParam gridParam){
	
		GridJsonResponse jsonRes = new GridJsonResponse();
		jsonRes.setTotal(service.getMenuListTotalCount(gridParam));
		jsonRes.setList(service.getMenuList(gridParam));
		
		return jsonRes;
	}
	
	@RequestMapping("/create")
	public @ResponseBody SimpleJsonResponse create(MenuDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		try{
			service.insertMenu(param);
			jsonRes.setMsg("사용자가 정상적으로 생성되었습니다.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("사용자 생성 중 에러가 발생하였습니다.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	public @ResponseBody SimpleJsonResponse update(MenuDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		try{
			service.updateMenu(param);
			jsonRes.setMsg("사용자 정보가 정상적으로 수정되었습니다.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("사용자 정보 수정 중 에러가 발생하였습니다.");
			
			e.printStackTrace();
		}
		
		
		return jsonRes;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody SimpleJsonResponse delete(MenuDto param){
		
		SimpleJsonResponse jsonRes = new SimpleJsonResponse();
		try{
			service.deleteMenu(param);
			jsonRes.setMsg("사용자 정보가 정상적으로 삭제되었습니다.");
			
		}catch(Exception e){
			
			jsonRes.setSuccess(false);
			jsonRes.setMsg("사용자 정보 삭제 중 에러가 발생하였습니다.");
			
			e.printStackTrace();
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/getMenu")
	public @ResponseBody DtoJsonResponse getMenu(MenuDto param){
	
		DtoJsonResponse jsonRes = new DtoJsonResponse();
		
		jsonRes.setData(service.getMenu(param));
		
		return jsonRes;
	}
*/
}
//end of MenuController.java