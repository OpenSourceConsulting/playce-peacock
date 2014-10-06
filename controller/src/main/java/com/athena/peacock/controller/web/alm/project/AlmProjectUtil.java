package com.athena.peacock.controller.web.alm.project;

import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectTamplateInfomationDto;
import com.google.gson.Gson;

public class AlmProjectUtil {

	// Project Mapping 정보
	public static ProjectMappingDto getTemplateMapping(String projectCode,
			String type, ProjectTamplateInfomationDto templateInfomation) {

		Gson gson = new Gson();

		ProjectMappingDto projectMapping = new ProjectMappingDto();
		projectMapping.setProjectCode(projectCode);
		projectMapping.setMappingType(40);
		projectMapping.setMappingPermission(gson.toJson(templateInfomation));
		projectMapping.setStatus("STANDBY");
		projectMapping.setMappingExecution("CREATE");

		if (type.equals("MOBILE")) {
			projectMapping.setMappingCode(projectCode + "_mobile");
		} else {
			projectMapping.setMappingCode(projectCode);
		}

		return projectMapping;
	}

	// Jenkins Mapping Dto 반환
	public static ProjectMappingDto getJenkinsMapping(String projectCode,
			String type, String execution) {

		ProjectMappingDto jenkinsMapping = new ProjectMappingDto();
		jenkinsMapping.setProjectCode(projectCode);
		jenkinsMapping.setMappingType(20);
		jenkinsMapping.setStatus("STANDBY");
		jenkinsMapping.setMappingExecution(execution);

		if (type.equals("MOBILE")) {
			jenkinsMapping.setMappingCode(projectCode + "_mobile");
		} else {
			jenkinsMapping.setMappingCode(projectCode);
		}

		return jenkinsMapping;
	}

}
