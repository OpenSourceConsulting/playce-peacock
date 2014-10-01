package com.athena.peacock.controller.web.alm.project.dto;

public class ProjectUserPasswordResetDto {

	private String key;
	private String password;
	private String passwordCheck;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public void setPasswordCheck(String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

}
