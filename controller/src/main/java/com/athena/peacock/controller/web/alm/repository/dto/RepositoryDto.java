package com.athena.peacock.controller.web.alm.repository.dto;

public class RepositoryDto {

	private String repositoryCode;
	private int repositoryType;
	private String repositoryDescription;
	private String repositoryUrl;

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

}
