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
 * Bong-Jin Kwon	2013. 9. 25.		First Draft.
 */
package com.athena.peacock.controller.web.alm.project.migration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.project.AlmProjectDao;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;

/**
 * <pre>
 *  프로젝트 맵핑 코드
 *  10 Confluence
 *  20 Jenkins
 *  30 SVN
 *  40 Template
 * </pre>
 * 
 * @author Dave
 * @version 1.0
 */
@Service
public class AlmProjectMigrationService {

	@Autowired
	private AlmProjectDao projectDao;

	@Autowired
	private AlmProjectMigrationDao migrationUserDao;

	@Autowired
	private AlmCrowdService crowdService;

	public AlmProjectMigrationService() {
		// TODO Auto-generated constructor stub

	}

	public String createGroup() {

		List<ProjectDto> projects = projectDao.getProjectList();

		for (ProjectDto project : projects) {
			AlmGroupDto tmpData = new AlmGroupDto();
			tmpData.setName(project.getProjectCode());
			tmpData.setDescription(project.getProjectCode() + " 프로젝트 그룹");
			tmpData.setActive(true);
			crowdService.addGroup(tmpData);
		}
		return "OK";
	}

	public String checkUser() {

		List<ProjectMigrationUserDto> users = migrationUserDao.getUserList();

		for (ProjectMigrationUserDto user : users) {

			DtoJsonResponse response = crowdService.getUser(user.getUSERNAME());

			if (response.isSuccess()) {
				user.setCHECKFLAG("TRUE");
			} else {
				user.setCHECKFLAG("FALSE");
			}

			migrationUserDao.checkUser(user);

		}
		return "OK";
	}

	public String addGroupUser() {

		List<ProjectMigrationGroupUserDto> users = migrationUserDao
				.getGroupUserList();

		for (ProjectMigrationGroupUserDto user : users) {

			DtoJsonResponse response = crowdService.addUserToGroup(
					user.getUsername(), user.getGroupname());

			if (response.isSuccess()) {
				user.setCHECKFLAG("TRUE");
			} else {
				user.setCHECKFLAG("FALSE");
			}

			migrationUserDao.checkGroupUser(user);

		}
		return "OK";
	}
}
// end of AlmProjectService.java