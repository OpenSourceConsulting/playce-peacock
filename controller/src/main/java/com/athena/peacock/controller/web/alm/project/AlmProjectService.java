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

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectWizardDto;
import com.athena.peacock.controller.web.alm.repository.dto.RepositoryDto;
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

	@Autowired
	private AlmCrowdService crowdService;

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
		// dto1.setProjectId("w001");
		dto1.setProjectDescription("Project ~");
		dto1.setProjectName("HHI Project");

		projects.add(dto1);

		response.setList(projects);

		return response;
	}

	public DtoJsonResponse createProject(ProjectDto project) {

		DtoJsonResponse response = new DtoJsonResponse();
		projectDao.insertProject(project);

		// Group 생성
		addGroup(project.getProjectCode(), project.getGroupDescription());

		response.setMsg("프로젝트 생성 성공");
		return response;
		//

	}

	public DtoJsonResponse createProjectWizrd(ProjectWizardDto project) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg("프로젝트 Wizard 생성 요청 되었습니다.");

		// Project DTO
		ProjectDto pDto = project.getProject();

		// Project Insert
		projectDao.insertProject(pDto);

		// Project Group 생성
		addGroup(pDto.getProjectCode(), pDto.getGroupDescription());

		// User를 그룹에 추가
		List<AlmUserDto> userList = project.getUsers();
		addUserToGroup(pDto.getProjectCode(), userList);
		
		return response;

	}

	public DtoJsonResponse checkProjectCode(String projectCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		int count = projectDao.getProjectExist(projectCode);

		if (count == 0) {
			response.setMsg("사용가능한 code 입니다.");
		} else {
			response.setSuccess(false);
			response.setMsg("이미 사용중인 code 입니다.");
		}

		return response;

	}

	public DtoJsonResponse getJenkinsJob(String projectcode) {

		DtoJsonResponse response = new DtoJsonResponse();
		// projectDao.insertProject(projectcode);
		return response;
		//

	}

	public DtoJsonResponse createProjectMapping(String projectCode,
			String mappingtype, String mappingCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);
		mappingDto.setMappingCode(mappingCode);

		if (mappingtype.equals("jenkins")) {
			mappingDto.setMappingType(20);
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
		} else {
			response.setSuccess(false);
			response.setMsg("mapping code가 정확하지 않습니다");
			return response;
		}

		projectDao.insertProjectMapping(mappingDto);
		return response;
	}

	public DtoJsonResponse getProjectMapping(String projectCode,
			String mappingtype) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);

		// 10 Confluence
		// 20 Jenkins
		// 30 SVN
		if (mappingtype.equals("jenkins")) {
			mappingDto.setMappingType(20);
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
		} else {
			response.setSuccess(false);
			response.setMsg("mapping code가 정확하지 않습니다");
			return response;
		}

		response.setData(projectDao.getProjectMapping(mappingDto));
		return response;
	}

	public ProjectWizardDto getWizard() {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectWizardDto dto = new ProjectWizardDto();

		ProjectDto project = new ProjectDto();
		List<AlmUserDto> users = new ArrayList<AlmUserDto>();
		List<ProjectMappingDto> jenkins = new ArrayList<ProjectMappingDto>();
		List<ProjectMappingDto> confluence = new ArrayList<ProjectMappingDto>();

		project.setProjectName("하이웨이 V3");
		project.setProjectCode("w1000");
		project.setGroupDescription("하이웨이 v3 프로젝트");
		project.setGroupDescription("group description");

		AlmUserDto user1 = new AlmUserDto();
		user1.setUserId("a001");

		AlmUserDto user2 = new AlmUserDto();
		user2.setUserId("a002");

		users.add(user1);
		users.add(user2);

		ProjectMappingDto jenkinsDto = new ProjectMappingDto();
		jenkinsDto.setMappingCode("Test");
		jenkinsDto.setMappingType(10);
		jenkinsDto.setProjectCode("w1000");

		ProjectMappingDto confluenceDto1 = new ProjectMappingDto();
		confluenceDto1.setMappingCode("new");
		confluenceDto1.setMappingType(10);
		confluenceDto1.setProjectCode("con1");

		ProjectMappingDto confluenceDto2 = new ProjectMappingDto();
		confluenceDto2.setMappingCode("con2");
		confluenceDto2.setMappingType(10);
		confluenceDto2.setProjectCode("w1000");

		jenkins.add(jenkinsDto);
		confluence.add(confluenceDto1);
		confluence.add(confluenceDto2);

		// Repository
		RepositoryDto repository = new RepositoryDto();
		repository.setRepositoryCode("hiway");
		repository.setRepositoryDescription("hiway repository");
		repository.setRepositoryType(10);
		repository.setRepositoryUrl("http://www");

		dto.setConfluence(confluence);
		dto.setJenkins(jenkins);
		dto.setProject(project);
		dto.setRepository(repository);
		dto.setUsers(users);

		response.setData(dto);
		response.setMsg("프로젝트 생성 성공");
		return dto;
		//

	}

	private void addGroup(String name, String description) {

		AlmGroupDto groupData = new AlmGroupDto();
		groupData.setActive(true);
		groupData.setDescription(description);
		groupData.setName(name);

		// Group 생성
		crowdService.addGroup(groupData);
	}

	private void addUserToGroup(String groupName, List<AlmUserDto> userList) {

		// Group 생성
		for (AlmUserDto username : userList) {
			crowdService.addUserToGroup(username.getUserId(), groupName);
		}
	}
}
// end of UserService.java