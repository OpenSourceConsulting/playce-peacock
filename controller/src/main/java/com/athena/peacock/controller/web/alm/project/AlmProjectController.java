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
package com.athena.peacock.controller.web.alm.project;

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

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * @author Jungsu Han
 * @version 1.0
 */
@Controller
@RequestMapping("/alm")
public class AlmProjectController {
	
	
	@Autowired
	private AlmProjectService almProjectService;
	
	@Autowired
	private AlmCrowdService almCrowdService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AlmProjectController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse list(ExtjsGridParam gridParam){
		return almProjectService.getProjectList(gridParam);
	}
	
	@RequestMapping(value = "/project", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DtoJsonResponse createProject(@Valid @RequestBody ProjectDto project, BindingResult result){
		
		//almCrowdService.addGroup(groupData);

		if (result.hasErrors()) {
			DtoJsonResponse response = new DtoJsonResponse();
			response.setSuccess(false);
			response.setMsg("invalid parameter");
			response.setData(result.getAllErrors());
			return response;
		}
		
		return almProjectService.createProject(project);
	}
	
	@RequestMapping(value = "/project/{projectCode}", method = RequestMethod.GET)
	public @ResponseBody DtoJsonResponse list(@PathVariable String projectCode){
		return almProjectService.getProject(projectCode);
	}
	
}
//end of AlmUserController.java