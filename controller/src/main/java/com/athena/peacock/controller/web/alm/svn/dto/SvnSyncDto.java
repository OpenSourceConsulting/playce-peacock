package com.athena.peacock.controller.web.alm.svn.dto;

import java.util.List;

public class SvnSyncDto {

	private List<SvnGroupUserDto> groups;
	private List<SvnUserDto> users;
	private List<SvnProjectDto> projects;

	public List<SvnGroupUserDto> getGroups() {
		return groups;
	}

	public void setGroups(List<SvnGroupUserDto> groups) {
		this.groups = groups;
	}

	public List<SvnUserDto> getUsers() {
		return users;
	}

	public void setUsers(List<SvnUserDto> users) {
		this.users = users;
	}

	public List<SvnProjectDto> getProjects() {
		return projects;
	}

	public void setProjects(List<SvnProjectDto> projects) {
		this.projects = projects;
	}

}
