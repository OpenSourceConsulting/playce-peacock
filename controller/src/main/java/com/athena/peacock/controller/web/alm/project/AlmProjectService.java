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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;

import com.athena.peacock.controller.web.alm.confluence.AlmConfluenceService;
import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserDto;
import com.athena.peacock.controller.web.alm.jenkins.AlmJenkinsService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectWizardDto;
import com.athena.peacock.controller.web.alm.svn.AlmSvnService;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Dave
 * @version 1.0
 */
@Service
public class AlmProjectService {

	@Autowired
	private AlmProjectDao projectDao;

	@Autowired
	private AlmCrowdService crowdService;

	@Autowired
	private AlmJenkinsService jenkinsService;

	@Autowired
	private AlmSvnService svnService;

	@Autowired
	private AlmConfluenceService confluenceService;

	public AlmProjectService() {
		// TODO Auto-generated constructor stub

	}

	// Project List
	public GridJsonResponse getProjectList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();
		List<ProjectDto> projects = projectDao.getProjectList();
		response.setList(projects);
		return response;

	}

	// Project 상세정보
	public DtoJsonResponse getProject(String projectCode) {

		DtoJsonResponse response = new DtoJsonResponse();
		ProjectDto dto = projectDao.getProject(projectCode);
		response.setData(dto);
		return response;

	}

	// Project 생성
	public DtoJsonResponse createProject(ProjectDto project) {

		DtoJsonResponse response = new DtoJsonResponse();
		projectDao.insertProject(project);

		// SVN 생성
		// Group 생성
		addGroup(project.getProjectCode(), project.getGroupDescription());
		response.setMsg("프로젝트 생성 성공");
		return response;
		//

	}

	// Project Wizard 실행
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

		if (userList != null) {
			addUserToGroup(pDto.getProjectCode(), userList);
		}

		// Job Mapping
		ProjectMappingDto jenkinsMapping = new ProjectMappingDto();
		jenkinsMapping.setMappingCode(pDto.getProjectCode());
		jenkinsMapping.setMappingType(20);
		jenkinsMapping.setProjectCode(pDto.getProjectCode());
		projectDao.insertProjectMapping(jenkinsMapping);

		// Confluence 저장
		List<ProjectMappingDto> confluences = project.getConfluence();

		if (confluences != null) {
			for (ProjectMappingDto confluence : confluences) {
				ProjectMappingDto confluenceMapping = new ProjectMappingDto();
				confluenceMapping.setMappingType(10);
				confluenceMapping.setProjectCode(pDto.getProjectCode());
				confluenceMapping.setMappingCode(confluence.getMappingCode());
				projectDao.insertProjectMapping(confluenceMapping);
			}
		}

		addPermissionToSpace(pDto.getProjectCode(), confluences);

		// SVN 프로젝트 생성
		try {
			svnService.createSvnProject(pDto.getRepository(),
					pDto.getProjectCode());
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Job 생성

		createJob("JobCopy", null, pDto.getProjectCode());

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
			response.setMsg("Jenkins Job이 추가되었습니다.");
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
			response.setMsg("SVN Repository가 추가되었습니다.");
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
			response.setMsg("Confluence Space가 추가되었습니다.");
		} else {
			response.setSuccess(false);
			response.setMsg("mapping code가 정확하지 않습니다");
			return response;
		}

		projectDao.insertProjectMapping(mappingDto);

		return response;
	}

	public GridJsonResponse getProjectMapping(String projectCode,
			String mappingtype) {

		GridJsonResponse response = new GridJsonResponse();

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

		response.setList(projectDao.getProjectMapping(mappingDto));
		return response;
	}

	public DtoJsonResponse deleteProjectMapping(String projectCode,
			String mappingtype, String mappingCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);
		mappingDto.setMappingCode(mappingCode);

		if (mappingtype.equals("jenkins")) {
			mappingDto.setMappingType(20);
			response.setMsg("Jenkins Job이 삭제되었습니다.");
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
			response.setMsg("SVN Repository가 삭제되었습니다.");
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
			response.setMsg("Confluence Space가 삭제되었습니다.");
		} else {
			response.setSuccess(false);
			response.setMsg("mapping code가 정확하지 않습니다");
			return response;
		}

		projectDao.deleteProjectMapping(mappingDto);

		return response;
	}

	private void createJob(String jobName, String templateName,
			String newJobName) {

		jenkinsService.createJob(jobName, templateName, newJobName);
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

		// User 그룹에 추가
		for (AlmUserDto username : userList) {
			// USER HISTORY
			crowdService.addUserToGroup(username.getUserId(), groupName);
		}
	}

	private void addPermissionToSpace(String groupName,
			List<ProjectMappingDto> spaces) {

		for (ProjectMappingDto space : spaces) {
			// USER HISTORY
			System.out.println("***************************");
			confluenceService.addPermissions(groupName, space.getMappingCode());
		}

	}
}
// end of UserService.java