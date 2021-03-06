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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

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
		
		try {
			user.setRegUserId(UserService.getLoginUserId());
			
			service.insertUser(user);
			jsonRes.setMsg("생성되었습니다.");
		} catch (Exception e) {
			jsonRes.setMsg("사용자 생성 중 오류가 발생했습니다.<br/>이미 등록된 사용자인지 확인하십시오.");
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/update")
	public @ResponseBody SimpleJsonResponse update(SimpleJsonResponse jsonRes, UserDto user){
		
		user.setUpdUserId(UserService.getLoginUserId());
		
		service.updateUser(user);
		jsonRes.setMsg("수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping("/updateMyAccount")
	public @ResponseBody SimpleJsonResponse updateMyAccount(SimpleJsonResponse jsonRes, UserDto user){
		
		int userId = UserService.getLoginUserId();
		
		user.setUserId(userId);
		user.setUpdUserId(userId);
		
		service.updateUser(user);
		jsonRes.setMsg("수정되었습니다.");
		
		
		return jsonRes;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody SimpleJsonResponse delete(SimpleJsonResponse jsonRes, @RequestParam("userId") int userId){
		try {
			service.deleteUser(userId);
			jsonRes.setMsg("삭제되었습니다.");
		} catch (Exception e) {
			jsonRes.setMsg("사용자 삭제 중 오류가 발생했습니다.<br/>User Permission에 등록된 사용자인지 확인하십시오.");
		}
		return jsonRes;
	}
	
	@RequestMapping("/getUser")
	public @ResponseBody DtoJsonResponse getUser(DtoJsonResponse jsonRes, @RequestParam("userId") int userId){
		
		jsonRes.setData(service.getUser(userId));
		
		return jsonRes;
	}
	
	@RequestMapping("/getMyAccount")
	public @ResponseBody DtoJsonResponse getMyAccount(){
		
		DtoJsonResponse jsonRes = new DtoJsonResponse();
		
		int userId = UserService.getLoginUserId();
		
		jsonRes.setData(service.getUser(userId));
		
		return jsonRes;
	}
	
	@RequestMapping("/login")
	public @ResponseBody SimpleJsonResponse login(SimpleJsonResponse jsonRes, @RequestParam("loginId") String loginId, @RequestParam("passwd") String passwd, HttpSession session){
		 try{
			UserDto user = service.getLoginUser(loginId, passwd);
			
			if(user == null){
				jsonRes.setSuccess(false);
				jsonRes.setMsg("login id 또는 비밀번호가 잘못되었습니다.");
			}else{
				jsonRes.setData(user);
				session.setAttribute(SESSION_USER_KEY, user);
			}
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		
		
		return jsonRes;
	}
	
	@RequestMapping("/onAfterLogout")
	public @ResponseBody SimpleJsonResponse logout(SimpleJsonResponse jsonRes, HttpSession session){
		
		session.invalidate();
		
		jsonRes.setMsg("로그아웃 되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping("/onAfterLogin")
	public @ResponseBody SimpleJsonResponse onAfterLogin(SimpleJsonResponse jsonRes){
		
		
		UserDto userDetails = UserService.getLoginUser();
		
		jsonRes.setData(userDetails);
		
		if(userDetails != null){
			service.updateLastLogon(userDetails.getUserId());
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/notLogin")
	public @ResponseBody SimpleJsonResponse notLogin(SimpleJsonResponse jsonRes){
		
		jsonRes.setSuccess(false);
		jsonRes.setMsg("로그인 정보가 없습니다. 관리자에게 문의하세요.");
		jsonRes.setData("notLogin");
		
		return jsonRes;
	}
	
	@RequestMapping("/accessDenied")
	public @ResponseBody SimpleJsonResponse accessDenied(SimpleJsonResponse jsonRes){
		
		jsonRes.setSuccess(false);
		jsonRes.setMsg("해당 작업에 대한 권한이 없습니다. 관리자에게 문의하세요.");
		
		return jsonRes;
	}
	
	@RequestMapping("/loginFail")
	public @ResponseBody SimpleJsonResponse loginFail(SimpleJsonResponse jsonRes){
		
		jsonRes.setSuccess(false);
		jsonRes.setMsg("login ID 또는 password 가 잘못되었습니다.");
		
		return jsonRes;
	}
	
	@RequestMapping("/reset")
	public String reset(){
		
		return "ResetUser";//   /WEB-INF/view/ResetUser.jsp
	}

}
//end of UserController.java