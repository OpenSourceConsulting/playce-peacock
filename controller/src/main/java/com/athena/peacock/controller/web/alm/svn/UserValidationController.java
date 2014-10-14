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
 * Bong-Jin Kwon	2014. 10. 14.		First Draft.
 */
package com.athena.peacock.controller.web.alm.svn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tmatesoft.svn.core.SVNException;

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.model.user.UserTemplate;

/**
 * <pre>
 * svn user connection 이상여부 체크
 * 
 * </pre>
 * @author Bong-Jin Kwon
 *
 */
@Controller
@RequestMapping("/svn/uvalid")
public class UserValidationController {
	
	@Autowired
	private UserValidationService uvalidService;
	
	@Autowired
	private AlmCrowdService crowdService;

	/**
	 * 
	 */
	public UserValidationController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model) {
		
		model.addAttribute("projects", uvalidService.getProjectList());

		

		return "SVNUserValidation";
	}
	
	
	// 그룹 정보
	@RequestMapping(value = "/{groupname}/users", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getGroupUsers(@PathVariable String groupname) {
		GridJsonResponse response = new GridJsonResponse();
		
		List<User> users = new ArrayList<User>();
		users.add( new UserTemplate("idkbj") );
		users.add( new UserTemplate("user1") );
		
		response.setList(users);
		response.setMsg("");
		
		return response;
	}
	
	
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse check(@RequestParam(value="url") String url, @RequestParam(value="uname") String uname, @RequestParam(value="pass") String pass) {

		SimpleJsonResponse res = new SimpleJsonResponse();
		
		try{
			if(uvalidService.getLatestRevision(url, uname, pass) > -1){
				res.setMsg("valid");
			}
		}catch(SVNException e){
			
			e.printStackTrace();
			
			res.setSuccess(false);
			res.setMsg(e.getMessage());
		}

		return res;
	}

}
