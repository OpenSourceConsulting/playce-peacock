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

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserDto;
import com.athena.peacock.controller.web.alm.jenkins.AlmJenkinsService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectHistoryDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectTamplateInfomationDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectTemplateDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectUserPasswordResetDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectWizardDto;
import com.athena.peacock.controller.web.alm.projectuser.AlmUserService;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.atlassian.crowd.model.user.User;

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
	private AlmUserService userService;

	@Autowired
	private AlmProjectHistoryMessageService historyService;
	
	@Autowired
	private AlmProjectProcess projectProcess;

	public AlmProjectService() {
		// TODO Auto-generated constructor stub

	}

	// Project History 정보
	public GridJsonResponse getProjectHistory(String projectCode) {

		GridJsonResponse response = new GridJsonResponse();
		List<ProjectHistoryDto> projects = projectDao
				.getProjectHistory(projectCode);
		response.setList(projects);
		return response;

	}

	// Project 리스트 정보
	public GridJsonResponse getProjectList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();
		List<ProjectDto> projects = projectDao.getProjectList(gridParam);
		response.setList(projects);
		return response;

	}

	public int getProjectCount() {

		return projectDao.getProjectCount();

	}

	// Project 상세정보
	public DtoJsonResponse getProject(String projectCode) {

		DtoJsonResponse response = new DtoJsonResponse();
		ProjectDto dto = projectDao.getProject(projectCode);

		// Jenkins와 SVN 상태 확인
		dto.setJenkinsStatus(getJenkinsStatus(projectCode));
		dto.setSvnStatus(getSvnStatus(projectCode));

		response.setData(dto);
		return response;

	}

	// Project 생성
	public DtoJsonResponse createProject(ProjectDto project) {

		DtoJsonResponse response = new DtoJsonResponse();

		// Project 정보 저장
		addProject(project);

		// Project Group 생성
		addGroup(project);

		projectProcess.processProjectMapping();

		// Make response Data
		response.setMsg("프로젝트가 생성되었습니다");
		return response;

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

		// Repository 추가
		pDto.setRepositoryCode(project.getTemplate().getRepository());

		String projectCode = pDto.getProjectCode();

		// Project 저장
		addProject(pDto);

		// Project Group 생성
		addGroup(pDto);

		// User를 그룹에 추가
		List<AlmUserDto> userList = project.getUsers();
		if (userList != null) {
			addUserToGroup(projectCode, userList);
		}

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
				confluenceMapping.setStatus("STANDBY");
				confluenceMapping.setMappingExecution("PERMISSION");
				projectDao.insertProjectMapping(confluenceMapping);
				historyService.addPermissionConfluenceSpace(confluenceMapping);

			}
		}

		// SVN Mapping
		ProjectMappingDto svnMapping = new ProjectMappingDto();
		svnMapping.setMappingCode(projectCode);
		svnMapping.setMappingType(30);
		svnMapping.setProjectCode(projectCode);
		svnMapping.setStatus("STANDBY");
		svnMapping.setMappingExecution("CREATE");

		projectDao.insertProjectMapping(svnMapping);
		historyService.createSvnProject(svnMapping);

		// Project Template
		ProjectTemplateDto template = project.getTemplate();

		if (template.getType() != null) {

			ProjectTamplateInfomationDto templateInfomationServer = new ProjectTamplateInfomationDto(
					template.getRepository(), template.getServerTemplate(),
					template.getServerGroupId(),
					template.getServerArtifactId(), template.getServerPackage());

			// Template Mapping
			projectDao.insertProjectMapping(AlmProjectUtil.getTemplateMapping(
					projectCode, "", templateInfomationServer));

			createProjectHistor(projectCode, projectCode
					+ " 프로젝트에 템플릿 생성 요청 되었습니다.");

			// Job Mapping
			projectDao.insertProjectMapping(AlmProjectUtil.getJenkinsMapping(
					projectCode, "", "CREATE"));
			createProjectHistor(projectCode, projectCode
					+ " Jenkins Job 생성 요청 되었습니다.");

			if (template.getType().equals("Mobile Project")) {

				ProjectTamplateInfomationDto templateInfomationMobile = new ProjectTamplateInfomationDto(
						template.getRepository(), template.getMobileTemplate(),
						template.getMobileGroupId(),
						template.getMobileArtifactId(),
						template.getMobilePackage());

				// Template Mapping
				projectDao.insertProjectMapping(AlmProjectUtil
						.getTemplateMapping(projectCode, "MOBILE",
								templateInfomationMobile));
				createProjectHistor(projectCode, projectCode
						+ " 프로젝트에 Mobile 템플릿 생성 요청 되었습니다.");

				// Job Mapping
				projectDao.insertProjectMapping(AlmProjectUtil
						.getJenkinsMapping(projectCode, "MOBILE", "CREATE"));
				createProjectHistor(projectCode, projectCode
						+ " Jenkins Mobile Job 생성 요청 되었습니다.");

			}
		}

		projectProcess.processProjectMapping();
		
		return response;

	}

	// 유저를 그룹에 추가
	public DtoJsonResponse addProjectUser(String projectCode, String username) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg(username + " 유저가 프로젝트 그룹에 추가 되었습니다");
		crowdService.addUserToGroup(username, projectCode);
		createProjectHistor(projectCode, projectCode + " 프로젝트 그룹에 " + username
				+ "유저가 추가되었습니다.");
		return response;

	}

	// 유저를 그룹에서 제거
	public DtoJsonResponse removeProjectUser(String projectCode, String username) {

		DtoJsonResponse response = new DtoJsonResponse();

		response.setMsg(username + " 유저가 프로젝트 그룹에서 제거 되었습니다");
		crowdService.removeUserFromGroup(username, projectCode);
		createProjectHistor(projectCode, projectCode + " 프로젝트 그룹에 " + username
				+ "유저가 제거되었습니다.");

		return response;

	}

	// 프로젝트 코드 중복여부 확인
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

	// Project Mapping 추가
	public DtoJsonResponse createProjectMapping(String projectCode,
			String mappingtype, String mappingCode, String permission) {

		DtoJsonResponse response = new DtoJsonResponse();

		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);
		mappingDto.setMappingCode(mappingCode);
		mappingDto.setStatus("STANDBY");
		mappingDto.setMappingExecution("PERMISSION");

		if (mappingtype.equals("jenkins")) {
			addJenkinsMapping(projectCode, mappingCode, "PERMISSION");
			response.setMsg("Jenkins Job이 추가되었습니다");
		} else if (mappingtype.equals("confluence")) {
			mappingDto.setMappingType(10);
			mappingDto.setMappingPermission(permission);
			projectDao.insertProjectMapping(mappingDto);
			response.setMsg("Confluence Space에 권한 추가 요청되었습니다.");
			historyService.addPermissionConfluenceSpace(mappingDto);
		} else {
			response.setSuccess(false);
			response.setMsg("맵핑 코드가 정확하지 않습니다");
			return response;
		}

		projectProcess.processProjectMapping();
		
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
	private void addGroup(ProjectDto project) {

		AlmGroupDto groupData = new AlmGroupDto();
		groupData.setActive(true);
		groupData.setDescription(project.getGroupDescription());
		groupData.setName(project.getProjectCode());

		// Group 생성
		crowdService.addGroup(groupData);
		historyService.createProjectGroup(project);
	}

	// User를 그룹에 추가
	private void addUserToGroup(String groupName, List<AlmUserDto> userList) {

		for (AlmUserDto username : userList) {
			// USER HISTORY
			crowdService.addUserToGroup(username.getUserId(), groupName);
			createProjectHistor(groupName,
					groupName + " 프로젝트 그룹에 " + username.getDisplayName()
							+ "유저가 추가되었습니다.");
		}
	}

	// Project 저장
	private void addProject(ProjectDto pDto) {
		// DB에 저장
		projectDao.insertProject(pDto);
		// 하스토리 정보 기록
		historyService.createProjectWizard(pDto);
	}

	// 작업 History 저장
	private void createProjectHistor(String projectCode, String message) {
		ProjectHistoryDto history = new ProjectHistoryDto();
		history.setProjectCode(projectCode);
		history.setMessage(message);
		projectDao.insertProjectHistory(history);
	}

	// Jenkins Mapping 저장
	private void addJenkinsMapping(String projectCode, String mappingCode,
			String execution) {

		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);
		mappingDto.setMappingCode(mappingCode);
		mappingDto.setStatus("STANDBY");
		mappingDto.setMappingExecution(execution);
		mappingDto.setMappingType(20);
		projectDao.insertProjectMapping(mappingDto);

		StringBuffer sb = new StringBuffer();
		sb.append(projectCode);
		sb.append(" 프로젝트에");
		sb.append(mappingCode);
		sb.append(" Jenkins Job 권한 생성 요청 되었습니다.");
		createProjectHistor(projectCode, sb.toString());
	}

	// User password Reset
	public DtoJsonResponse resetPassword(ProjectUserPasswordResetDto resetDto) {
		return userService.resetPassword(resetDto);
	}

	public DtoJsonResponse resetPasswordEmail(
			ProjectUserPasswordResetDto resetDto) {
		userService.sendResetPassword("aaa");
		return null;
	}

	// Admin Console에서 비밀번호 변경
	public DtoJsonResponse resetPasswordByAdmin(String usercode) {
		String password = usercode.substring(1);
		return crowdService.changePasswordUser(usercode, password);
	}

	public DtoJsonResponse syncJenkins(String projectCode) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg("Jenkins Sync 작업이 요청되었습니다");

		List<User> users = crowdService.getGroupUserList(projectCode);

		StringBuffer sb = new StringBuffer();

		if (users == null || users.size() == 0) {
			response.setMsg("Sync 할 User가 존재하지 않습니다.");
			return response;
		}
		for (int i = 0; i < users.size(); i++) {

			User user = users.get(i);
			sb.append(user.getName());

			if (i != (users.size() - 1)) {
				sb.append(",");
			}
		}

		// Jenkins Job 확인
		ProjectMappingDto mappingDto = new ProjectMappingDto();
		mappingDto.setProjectCode(projectCode);
		mappingDto.setMappingType(20);
		List<ProjectMappingDto> mappingList = projectDao
				.getProjectMapping(mappingDto);

		for (ProjectMappingDto mapping : mappingList) {
			jenkinsService.copyPermission(mapping.getMappingCode(),
					sb.toString());
		}

		return response;
	}

	private String getJenkinsStatus(String projectCode) {
		/*
		// HttpClient를 이용한 Jenkins Job 관련 조회 시 "Invalid use of BasicClientConnManager: connection still allocated." 오류가 간헐적으로 발생.
		GridJsonResponse response = jenkinsService.getJobs(null);

		if (response != null && response.isSuccess()) {
			jenkinsService.getJobSearch((List<JobDto>) response.getList(), projectCode);
			return "OK";
		} else {
			return response.getMsg();
		}
		*/
		
		return "OK";
	}

	private String getSvnStatus(String projectCode) {

		return "OK";
	}
}
// end of AlmProjectService.java