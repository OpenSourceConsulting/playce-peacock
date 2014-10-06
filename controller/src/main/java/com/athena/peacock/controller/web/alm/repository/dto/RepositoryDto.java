package com.athena.peacock.controller.web.alm.repository.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class RepositoryDto {

	private String repositoryCode;
	private int repositoryType;
	private String repositoryDescription;
	private String repositoryUrl;
	private String repositoryStatus;

	public String getRepositoryCode() {
		return repositoryCode;
	}

	public void setRepositoryCode(String repositoryCode) {
		this.repositoryCode = repositoryCode;
	}

	public int getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(int repositoryType) {
		this.repositoryType = repositoryType;
	}

	public String getRepositoryDescription() {
		return repositoryDescription;
	}

	public void setRepositoryDescription(String repositoryDescription) {
		this.repositoryDescription = repositoryDescription;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

	public String getRepositoryStatus() {
		return repositoryStatus;
	}

	public void setRepositoryStatus(String repositoryStatus) {
		this.repositoryStatus = repositoryStatus;
	}

}
