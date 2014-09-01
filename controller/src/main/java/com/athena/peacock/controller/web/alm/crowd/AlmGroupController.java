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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * 
 * @author Jungsu Han
 * @version 1.0
 */
@Controller
@RequestMapping("/alm")
public class AlmGroupController {

	@Autowired
	private AlmCrowdService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AlmGroupController() {
		// TODO Auto-generated constructor stub
	}

	// 그룹 리스트
	@RequestMapping(value = "/groupmanagement", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse getGroupList(ExtjsGridParam gridParam) {
		return service.getList("GROUP", gridParam);
	}

	// 그룹 정보
	@RequestMapping(value = "/groupmanagement/{groupname}", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse getGroup(DtoJsonResponse jsonRes,
			@PathVariable String groupname) {
		return service.getGroup(groupname);
	}

	// 그룹 추가
	@RequestMapping(value = "/groupmanagement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse addUser(@Valid @RequestBody AlmGroupDto groupData,
			BindingResult result) {

		if (result.hasErrors()) {
			DtoJsonResponse response = new DtoJsonResponse();
			response.setSuccess(false);
			response.setMsg("invalid parameter");
			response.setData(result.getAllErrors());
			return response;
		}

		return service.addGroup(groupData);
	}

	// 그룹 삭제
	@RequestMapping(value = "/groupmanagement/{groupname}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse removeGroup(@PathVariable String groupname) {

		return service.removeGroup(groupname);
	}

	// 그룹 정보
	@RequestMapping(value = "/groupmanagement/{groupname}/users", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse getGroupUsers(DtoJsonResponse jsonRes,
			@PathVariable String groupname) {
		return service.getGroupUser(groupname);
	}

	// 그룹에 유저 추가
	@RequestMapping(value = "/groupmanagement/{groupname}/{username}", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse addUserToGroup(@PathVariable String groupname,
			@PathVariable String username) {
		return service.addUserToGroup(username, groupname);
	}

	// 그룹에 유저 삭제
	@RequestMapping(value = "/groupmanagement/{groupname}/{username}", method = RequestMethod.DELETE)
	public @ResponseBody
	DtoJsonResponse removeUserFromGroup(@PathVariable String groupname,
			@PathVariable String username) {
		return service.removeUserFromGroup(username, groupname);
	}

}
// end of AlmUserController.java