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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

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
	
	@Autowired
	private AlmProjectDao projectDao;

	public AlmProjectService() {
		// TODO Auto-generated constructor stub

	}



	public GridJsonResponse getProjectList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();

		List<ProjectDto> projects = projectDao.getProjectList();
		response.setList(projects);

		return response;
	}
	
	public DtoJsonResponse getProject(String projectCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectDto dto = projectDao.getProject(projectCode);
		response.setData(dto);
		return response;
	}
	
	public GridJsonResponse getUserList(String projectCode) {

		GridJsonResponse response = new GridJsonResponse();

		List<ProjectDto> projects = new ArrayList<ProjectDto>();

		ProjectDto dto1 = new ProjectDto();
		//dto1.setProjectId("w001");
		dto1.setProjectDescription("Project ~");
		dto1.setProjectName("HHI Project");

		projects.add(dto1);

		response.setList(projects);

		return response;
	}
	
	public DtoJsonResponse createProject(ProjectDto project){
		
		DtoJsonResponse response = new DtoJsonResponse();
		projectDao.insertProject(project);
		
		response.setMsg("프로젝트 생성 성공");
		return response;
		//
		
	}

}
// end of UserService.java