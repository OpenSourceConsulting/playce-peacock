package com.athena.peacock.controller.web.alm.project.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectHistoryDto {

	private int projectHistoryId;
	private String projectCode;
	private String message;
	private String createTime;

	public int getProjectHistoryId() {
		return projectHistoryId;
	}

	public void setProjectHistoryId(int projectHistoryId) {
		this.projectHistoryId = projectHistoryId;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
