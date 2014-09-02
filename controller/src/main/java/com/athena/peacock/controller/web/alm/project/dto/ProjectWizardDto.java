package com.athena.peacock.controller.web.alm.project.dto;

import java.util.List;

import com.athena.peacock.controller.web.alm.repository.dto.RepositoryDto;
import com.atlassian.crowd.model.user.User;

public class ProjectWizardDto {

	private ProjectDto project;
	private List<User> users;
	private List<ProjectMappingDto> jenkins;
	private List<ProjectMappingDto> confluence;
	private RepositoryDto repository;

	public ProjectDto getProject() {
		return project;
	}

	public void setProject(ProjectDto project) {
		this.project = project;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<ProjectMappingDto> getJenkins() {
		return jenkins;
	}

	public void setJenkins(List<ProjectMappingDto> jenkins) {
		this.jenkins = jenkins;
	}

	public List<ProjectMappingDto> getConfluence() {
		return confluence;
	}

	public void setConfluence(List<ProjectMappingDto> confluence) {
		this.confluence = confluence;
	}

	public RepositoryDto getRepository() {
		return repository;
	}

	public void setRepository(RepositoryDto repository) {
		this.repository = repository;
	}

}
