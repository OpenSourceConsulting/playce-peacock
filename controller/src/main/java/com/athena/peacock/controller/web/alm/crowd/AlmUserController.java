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
package com.athena.peacock.controller.web.alm.crowd;

import java.util.Iterator;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserAddDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/alm")
public class AlmUserController {

	@Autowired
	private AlmCrowdService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AlmUserController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/usermanagement", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse userList(
			@RequestParam(value = "limit", defaultValue = "0") int offset,
			@RequestParam(value = "search", required = false) String search) {

		ExtjsGridParam gridParam = new ExtjsGridParam();
		gridParam.setPage(offset);

		if (search != null) {
			gridParam.setSearch(search);
		}

		return service.getList("USER", gridParam);
	}

	// User 상세 정보
	@RequestMapping(value = "/usermanagement/{username}", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse getUser(DtoJsonResponse jsonRes,
			@PathVariable String username) {
		return service.getUser(username);
	}

	@RequestMapping(value = "/usermanagement/{username}/groups", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse getUserGroup(@PathVariable String username) {
		return service.getUserGroup(username);
	}

	// User 추가
	@RequestMapping(value = "/usermanagement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse addUser(@Valid @RequestBody AlmUserAddDto userData,
			BindingResult result) {

		if (result.hasErrors()) {
			DtoJsonResponse response = new DtoJsonResponse();
			response.setSuccess(false);
			
			StringBuilder sb = new StringBuilder();
			
			Iterator<FieldError> iter = result.getFieldErrors().iterator();
			FieldError error = null;
			
			while (iter.hasNext()) {
				error = iter.next();
				sb.append(",").append(error.getField());
			}
			
			if (StringUtils.isEmpty(sb.toString())) {
				response.setMsg("유효하지 않은 파라메타가 입력되었습니다.");
			} else {
				response.setMsg(sb.toString().substring(1) + "에 유효하지 않은 파라메타가 입력되었습니다.");
			}
			
			response.setData(result.getAllErrors());
			return response;
		}

		return service.addUser(userData);
	}

	// User 추가
	@RequestMapping(value = "/usermanagement", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse modifyUser(@RequestBody AlmUserAddDto userData,
			BindingResult result) {

		if (result.hasErrors()) {
			DtoJsonResponse response = new DtoJsonResponse();
			response.setSuccess(false);
			
			StringBuilder sb = new StringBuilder();
			
			Iterator<FieldError> iter = result.getFieldErrors().iterator();
			FieldError error = null;
			
			while (iter.hasNext()) {
				error = iter.next();
				sb.append(",").append(error.getField());
			}
			
			if (StringUtils.isEmpty(sb.toString())) {
				response.setMsg("유효하지 않은 파라메타가 입력되었습니다.");
			} else {
				response.setMsg(sb.toString().substring(1) + "에 유효하지 않은 파라메타가 입력되었습니다.");
			}
			
			response.setData(result.getAllErrors());
			return response;
		}

		return service.modifyUser(userData);
	}

	// User 삭제
	@RequestMapping(value = "/usermanagement/{username}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse removeUser(@PathVariable String username) {
		return service.removeUser(username);
	}

	/*
	 * // User 패스워드 변경
	 * 
	 * @RequestMapping(value = "/usermanagement/{username}/password", method =
	 * RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE) public
	 * 
	 * @ResponseBody DtoJsonResponse passwodReset(@PathVariable String username)
	 * { return service.changePasswordUser(username); }
	 */

}
// end of AlmUserController.java