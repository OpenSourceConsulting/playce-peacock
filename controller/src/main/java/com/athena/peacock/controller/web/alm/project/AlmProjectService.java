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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNException;

import com.athena.peacock.controller.web.alm.confluence.AlmConfluenceService;
import com.athena.peacock.controller.web.alm.confluence.dto.SpaceErrorDto;
import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserDto;
import com.athena.peacock.controller.web.alm.jenkins.AlmJenkinsService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectHistoryDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectTamplateInfomationDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectTemplateDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectWizardDto;
import com.athena.peacock.controller.web.alm.svn.AlmSvnService;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.google.gson.Gson;

/**
 * <pre>
 *  10 Confluence
 * 20 Jenkins
 *  30 SVN
 *  40 Template
 * </pre>
 * 
 * @author Dave
 * @version 1.0
 */
@Service
public class AlmProjectService {

	@Value("#{contextProperties['alm.jenkins.servertemplate']}")
	private String JENKINS_SERVERTEMPLATE;

	@Value("#{contextProperties['alm.jenkins.mobiletemplate']}")
	private String JENKINS_MOBILETEMPLATE;

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

	// Project 리스트 정보
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

		createProjectHistor(project.getProjectCode(), project.getProjectCode()
				+ " 프로젝트가 생성되었습니다.");

		// Group 생성
		addGroup(project.getProjectCode(), project.getGroupDescription());

		createProjectHistor(project.getProjectCode(), project.getProjectCode()
				+ " 프로젝트가 그룹이 생성되었습니다.");
		response.setMsg("프로젝트가 생성되었습니다");
		return response;
		//

	}

	// Project Wizard 실행
	@Transactional
	public DtoJsonResponse createProjectWizrd(ProjectWizardDto project) {

		DtoJsonResponse response = new DtoJsonResponse();

		// Project 및 맵핑정보를 DB에 저장 후 실제 Process는 Quarz Job에서 처리

		response.setMsg("프로젝트 마법사 생성이 요청되었습니다.");
		// PROJECT TABLE 저장

		// Project DTO
		ProjectDto pDto = project.getProject();
		String projectCode = pDto.getProjectCode();

		// Project 저장
		projectDao.insertProject(pDto);
		createProjectHistor(projectCode, projectCode + " 프로젝트가 생성되었습니다.");

		// Project Group 생성
		addGroup(projectCode, pDto.getGroupDescription());
		createProjectHistor(projectCode, projectCode + " 프로젝트 그룹이 생성되었습니다.");

		// User를 그룹에 추가
		List<AlmUserDto> userList = project.getUsers();
		if (userList != null) {
			addUserToGroup(projectCode, userList);
		}

		createProjectHistor(projectCode, projectCode
				+ " 프로젝트에 Jenkins Job 생성 요청 되었습니다.");

		// Confluence 저장
		List<ProjectMappingDto> confluences = project.getConfluence();

		if (confluences != null) {

			for (ProjectMappingDto confluence : confluences) {
				ProjectMappingDto confluenceMapping = new ProjectMappingDto();
				confluenceMapping.setMappingType(10);
				confluenceMapping.setProjectCode(projectCode);
				confluenceMapping.setMappingCode(confluence.getMappingCode());
				confluenceMapping.setMappingPermission(confluence
						.getMappingPermission());
				projectDao.insertProjectMapping(confluenceMapping);

				StringBuffer sb = new StringBuffer();
				sb.append(projectCode);
				sb.append(" 프로젝트에");
				sb.append(confluence.getMappingCode());
				sb.append(" Confluence Space 권한 생성 요청 되었습니다.");
				createProjectHistor(projectCode, sb.toString());
			}
		}

		// SVN Mapping
		ProjectMappingDto svnMapping = new ProjectMappingDto();
		svnMapping.setMappingCode(projectCode);
		svnMapping.setMappingType(30);
		svnMapping.setProjectCode(projectCode);
		projectDao.insertProjectMapping(svnMapping);

		createProjectHistor(projectCode, projectCode
				+ " 프로젝트에 SVN Project 생성 요청 되었습니다.");

		// Project Template
		ProjectTemplateDto template = project.getTemplate();

		Gson gson = new Gson();
		if (template.getType() != null) {
			ProjectTamplateInfomationDto templateInfomationServer = new ProjectTamplateInfomationDto(
					template.getRepository(), template.getServerTemplate(),
					template.getServerGroupId(),
					template.getServerArtifactId(), template.getServerPackage());

			ProjectMappingDto projectMapping = new ProjectMappingDto();
			projectMapping.setProjectCode(projectCode);
			projectMapping.setMappingCode(projectCode);
			projectMapping.setMappingType(40);
			projectMapping.setMappingPermission(gson
					.toJson(templateInfomationServer));
			projectDao.insertProjectMapping(projectMapping);

			createProjectHistor(projectCode, projectCode
					+ " 프로젝트에 템플릿 생성 요청 되었습니다.");

			// Job Mapping
			ProjectMappingDto jenkinsMapping = new ProjectMappingDto();
			jenkinsMapping.setMappingCode(projectCode);
			jenkinsMapping.setMappingType(20);
			jenkinsMapping.setProjectCode(projectCode);
			projectDao.insertProjectMapping(jenkinsMapping);
			createProjectHistor(projectCode, projectCode
					+ " Jenkins Job 생성 요청 되었습니다.");

			if (template.getType().equals("Mobile Project")) {

				ProjectTamplateInfomationDto templateInfomationMobile = new ProjectTamplateInfomationDto(
						template.getRepository(), template.getMobileTemplate(),
						template.getMobileGroupId(),
						template.getMobileArtifactId(),
						template.getMobilePackage());

				ProjectMappingDto mobileProjectMapping = new ProjectMappingDto();
				mobileProjectMapping.setProjectCode(projectCode);
				mobileProjectMapping.setMappingCode(projectCode + "_mobile");
				mobileProjectMapping.setMappingType(40);
				mobileProjectMapping.setMappingPermission(gson
						.toJson(templateInfomationServer));
				projectDao.insertProjectMapping(mobileProjectMapping);

				createProjectHistor(projectCode, projectCode
						+ " 프로젝트에 Mobile 템플릿 생성 요청 되었습니다.");

				// Job Mapping
				ProjectMappingDto jenkinsMobileMapping = new ProjectMappingDto();
				jenkinsMobileMapping.setProjectCode(projectCode);
				jenkinsMobileMapping.setMappingCode(projectCode + "_mobile");
				jenkinsMobileMapping.setMappingType(20);
				projectDao.insertProjectMapping(jenkinsMobileMapping);
				createProjectHistor(projectCode, projectCode
						+ " Jenkins Mobile Job 생성 요청 되었습니다.");

			}
		}

		// addPermissionToSpace(pDto.getProjectCode(), confluences);

		// SVN 프로젝트 생성
		/*
		 * try { svnService.createSvnProject(pDto.getRepository(),
		 * pDto.getProjectCode()); } catch (SVNException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		// Job 생성

		// createJob("JobCopy", null, pDto.getProjectCode());

		return response;

	}

	public DtoJsonResponse addProjectUser(String projectCode, String username) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg(username + " 유저가 프로젝트 그룹에 추가 되었습니다");
		crowdService.addUserToGroup(username, projectCode);
		createProjectHistor(projectCode, projectCode + " 프로젝트 그룹에 " + username
				+ "유저가 추가되었습니다.");
		return response;

	}

	public DtoJsonResponse removeProjectUser(String projectCode, String username) {

		DtoJsonResponse response = new DtoJsonResponse();

		response.setMsg(username + " 유저가 프로젝트 그룹에서 제거 되었습니다");
		crowdService.removeUserFromGroup(username, projectCode);
		createProjectHistor(projectCode, projectCode + " 프로젝트 그룹에 " + username
				+ "유저가 제거되었습니다.");

		return response;

	}

	public DtoJsonResponse checkProjectCode(String projectCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		int count = projectDao.getProjectExist(projectCode);

		if (count == 0) {
			response.setMsg("사용가능한 코드입니다");
		} else {
			response.setSuccess(false);
			response.setMsg("이미 사용중인 코드입니다.");
		}

		return response;

	}

	public DtoJsonResponse getJenkinsJob(String projectcode) {

		DtoJsonResponse response = new DtoJsonResponse();
		// projectDao.insertProject(projectcode);
		return response;

	}

	public DtoJsonResponse createProjectMapping(String projectCode,
			String mappingtype, String mappingCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);
		mappingDto.setMappingCode(mappingCode);

		if (mappingtype.equals("jenkins")) {
			mappingDto.setMappingType(20);
			response.setMsg("Jenkins Job이 추가되었습니다");
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
			response.setMsg("SVN Repository가 추가되었습니다");
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
			response.setMsg("Confluence Space가 추가되었습니다");
		} else {
			response.setSuccess(false);
			response.setMsg("맵핑 코드가 정확하지 않습니다");
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

		if (mappingtype.equals("jenkins")) {
			mappingDto.setMappingType(20);
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
		} else {
			response.setSuccess(false);
			response.setMsg("맵핑 코드가 정확하지 않습니다");
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
			response.setMsg("Jenkins Job이 삭제되었습니다");
		} else if (mappingtype.equals("svn")) {
			mappingDto.setMappingType(30);
			response.setMsg("SVN Repository가 삭제되었습니다");
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
			response.setMsg("Confluence Space가 삭제되었습니다");
		} else {
			response.setSuccess(false);
			response.setMsg("mapping code가 정확하지 않습니다");
			return response;
		}

		projectDao.deleteProjectMapping(mappingDto);

		return response;
	}

	// 그룹 생성
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
			createProjectHistor(groupName,
					groupName + " 프로젝트 그룹에 " + username.getDisplayName()
							+ "유저가 추가되었습니다.");
		}
	}

	private SpaceErrorDto addPermissionToSpace(ProjectMappingDto spaces) {

		return confluenceService.addPermissions(spaces.getProjectCode(),
				spaces.getMappingCode());

	}

	private void createProjectHistor(String projectCode, String message) {

		ProjectHistoryDto history = new ProjectHistoryDto();
		history.setProjectCode(projectCode);
		history.setMessage(message);
		projectDao.insertProjectHistory(history);
	}

	public void processProjectMapping() {

		List<ProjectMappingDto> lists = projectDao.getProjectMappingStandBy();

		for (ProjectMappingDto list : lists) {

			// History
			if (list.getMappingType() == 10) {

				projectDao.startProjectMappingJob(list);
				SpaceErrorDto spaceResponse = addPermissionToSpace(list);

				if (spaceResponse.isSuccess()) {
					createProjectHistor(
							list.getProjectCode(),
							list.getProjectCode() + " 프로젝트에 "
									+ list.getMappingCode()
									+ " Confluence Space 권한 생성 되었습니다.");
					list.setStatus("COMPLETE");
				} else {
					list.setStatus("FAIL");
					list.setExitMessage(spaceResponse.getErrorMessage());
				}

				projectDao.endProjectMappingJob(list);
			} else if (list.getMappingType() == 20) {

				// projectDao.startProjectMappingJob(list);
				// createJob("JobCopy", null, list.getProjectCode());
				// projectDao.endProjectMappingJob(list);
			} else if (list.getMappingType() == 30) {
				projectDao.startProjectMappingJob(list);

				try {
					svnService.createSvnProject("hiway", list.getProjectCode());
					createProjectHistor(
							list.getProjectCode(),
							list.getProjectCode() + " 프로젝트에 "
									+ list.getMappingCode()
									+ " SVN Project 생성 되었습니다.");
					list.setStatus("COMPLETE");
				} catch (SVNException e) {
					list.setStatus("FAIL");
					list.setExitMessage(e.getMessage());
					createProjectHistor(
							list.getProjectCode(),
							list.getProjectCode() + " 프로젝트에 "
									+ list.getMappingCode()
									+ " SVN Project 생성 실패했습니다.");
				}

				projectDao.endProjectMappingJob(list);

			}

		}
	}

	// Project 리스트 정보
	public GridJsonResponse getProjectHistory(String projectCode) {

		GridJsonResponse response = new GridJsonResponse();
		List<ProjectHistoryDto> projects = projectDao
				.getProjectHistory(projectCode);
		response.setList(projects);
		return response;

	}
}
// end of UserService.java