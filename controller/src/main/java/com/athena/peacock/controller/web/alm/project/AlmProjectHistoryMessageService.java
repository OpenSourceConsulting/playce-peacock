package com.athena.peacock.controller.web.alm.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectHistoryDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;

@Service
public class AlmProjectHistoryMessageService {

	@Autowired
	private AlmProjectDao projectDao;

	// Project Wizard 생성 Message
	public void createProjectWizard(ProjectDto project) {

		createProjectHistor(project.getProjectCode(), project.getProjectCode()
				+ " 프로젝트가 Wizard 생성되었습니다.");
	}

	// Project 생성 Message
	public void createProject(ProjectDto project) {
		createProjectHistor(project.getProjectCode(), project.getProjectCode()
				+ " 프로젝트가 생성되었습니다.");
	}

	// Project Group 생성 Message
	public void createProjectGroup(ProjectDto project) {
		createProjectHistor(project.getProjectCode(), project.getProjectCode()
				+ " 프로젝트가 그룹이 생성되었습니다.");
	}

	// Confluence 권한 추가 Message
	public void addPermissionConfluenceSpace(ProjectMappingDto mappingDto) {

		StringBuffer sb = new StringBuffer();
		sb.append(mappingDto.getProjectCode());
		sb.append(" 프로젝트에");
		sb.append(mappingDto.getMappingCode());
		sb.append(" Confluence Space 권한 생성 요청 되었습니다.");
		createProjectHistor(mappingDto.getProjectCode(), sb.toString());
	}

	// SVN Project 생성 추가 Message
	public void createSvnProject(ProjectMappingDto mappingDto) {
		createProjectHistor(mappingDto.getProjectCode(),
				mappingDto.getProjectCode() + " 프로젝트에 SVN Project 생성 요청 되었습니다.");
	}

	public void addUserToGroup(ProjectMappingDto mappingDto) {

		StringBuffer sb = new StringBuffer();
		sb.append(mappingDto.getProjectCode());
		sb.append(" 프로젝트에");
		sb.append(mappingDto.getMappingCode());
		sb.append(" Confluence Space 권한 생성 요청 되었습니다.");
		createProjectHistor(mappingDto.getProjectCode(), sb.toString());
	}

	private void createProjectHistor(String projectCode, String message) {

		ProjectHistoryDto history = new ProjectHistoryDto();
		history.setProjectCode(projectCode);
		history.setMessage(message);
		projectDao.insertProjectHistory(history);

	}
}
