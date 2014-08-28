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
package com.athena.peacock.controller.web.alm.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserAddDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.exception.InvalidUserException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.integration.rest.entity.PasswordEntity;
import com.atlassian.crowd.integration.rest.entity.UserEntity;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.search.builder.Restriction;
import com.atlassian.crowd.search.query.entity.restriction.PropertyRestriction;
import com.atlassian.crowd.search.query.entity.restriction.constants.GroupTermKeys;
import com.atlassian.crowd.search.query.entity.restriction.constants.UserTermKeys;
import com.atlassian.crowd.service.client.CrowdClient;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class AlmProjectService {

	public AlmProjectService() {
		// TODO Auto-generated constructor stub

	}



	public GridJsonResponse getList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();

		List<ProjectDto> projects = new ArrayList<ProjectDto>();

		ProjectDto dto1 = new ProjectDto();
		dto1.setProjectId("w001");
		dto1.setProjectDescription("Project ~");
		dto1.setProjectName("HHI Project");

		projects.add(dto1);

		response.setList(projects);

		return response;
	}
	
	public DtoJsonResponse getProject(String projectId) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectDto dto1 = new ProjectDto();
		dto1.setProjectId("w001");
		dto1.setProjectDescription("Project ~");
		dto1.setProjectName("HHI Project");
		dto1.setProjectDescription("프로젝트 설명");
		response.setData(dto1);
		
		return response;
	}
	
	public GridJsonResponse getUserList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();

		List<ProjectDto> projects = new ArrayList<ProjectDto>();

		ProjectDto dto1 = new ProjectDto();
		dto1.setProjectId("w001");
		dto1.setProjectDescription("Project ~");
		dto1.setProjectName("HHI Project");

		projects.add(dto1);

		response.setList(projects);

		return response;
	}

}
// end of UserService.java