package com.athena.peacock.controller.web.alm.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectTemplateDto {

	private String repository;
	private String type;

	private String serverTemplate;
	private String serverGroupId;
	private String serverArtifactId;
	private String serverPackage;

	private String mobileTemplate;
	private String mobileGroupId;
	private String mobileArtifactId;
	private String mobilePackage;

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServerTemplate() {
		return serverTemplate;
	}

	public void setServerTemplate(String serverTemplate) {
		this.serverTemplate = serverTemplate;
	}

	public String getServerGroupId() {
		return serverGroupId;
	}

	public void setServerGroupId(String serverGroupId) {
		this.serverGroupId = serverGroupId;
	}

	public String getServerArtifactId() {
		return serverArtifactId;
	}

	public void setServerArtifactId(String serverArtifactId) {
		this.serverArtifactId = serverArtifactId;
	}

	public String getServerPackage() {
		return serverPackage;
	}

	public void setServerPackage(String serverPackage) {
		this.serverPackage = serverPackage;
	}

	public String getMobileTemplate() {
		return mobileTemplate;
	}

	public void setMobileTemplate(String mobileTemplate) {
		this.mobileTemplate = mobileTemplate;
	}

	public String getMobileGroupId() {
		return mobileGroupId;
	}

	public void setMobileGroupId(String mobileGroupId) {
		this.mobileGroupId = mobileGroupId;
	}

	public String getMobileArtifactId() {
		return mobileArtifactId;
	}

	public void setMobileArtifactId(String mobileArtifactId) {
		this.mobileArtifactId = mobileArtifactId;
	}

	public String getMobilePackage() {
		return mobilePackage;
	}

	public void setMobilePackage(String mobilePackage) {
		this.mobilePackage = mobilePackage;
	}

}
