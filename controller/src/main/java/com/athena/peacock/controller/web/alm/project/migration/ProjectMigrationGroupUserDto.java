package com.athena.peacock.controller.web.alm.project.migration;

public class ProjectMigrationGroupUserDto {

	private String groupname;
	private String username;
	private String CHECKFLAG;

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCHECKFLAG() {
		return CHECKFLAG;
	}

	public void setCHECKFLAG(String cHECKFLAG) {
		CHECKFLAG = cHECKFLAG;
	}

}
