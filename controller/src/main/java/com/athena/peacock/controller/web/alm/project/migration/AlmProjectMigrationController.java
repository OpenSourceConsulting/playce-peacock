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
package com.athena.peacock.controller.web.alm.project.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 * 프로젝트  마이그레이션 관련 컨트롤러.
 * </pre>
 * 
 * @author Dave Han
 * @version 1.0
 */
@Controller
@RequestMapping("/alm")
public class AlmProjectMigrationController {

	@Autowired
	private AlmProjectMigrationService service;
	
	
	@RequestMapping(value = "/project/migration/createCrowdUser", method = RequestMethod.GET)
	public @ResponseBody
	String createCrowdUser() {
		return service.createCrowdUser();
	}
	
	@RequestMapping(value = "/project/migration/checkuser", method = RequestMethod.GET)
	public @ResponseBody
	String checkUser() {
		return service.checkUser();
	}
	
	@RequestMapping(value = "/project/migration/createProject", method = RequestMethod.GET)
	public @ResponseBody
	String createProject() {
		return service.createProject();
	}

	@RequestMapping(value = "/project/migration/addGroupUser", method = RequestMethod.GET)
	public @ResponseBody
	String addGroupUser() {
		return service.addGroupUser();
	}

	@RequestMapping(value = "/project/migration/addJenkins", method = RequestMethod.GET)
	public @ResponseBody
	String addJenkins() {
		return service.addJenkinsMapping();
	}

}
// end of AlmUserController.java