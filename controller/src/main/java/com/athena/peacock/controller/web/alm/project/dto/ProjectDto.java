/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 8. 19.		First Draft.
 */
package com.athena.peacock.controller.web.alm.project.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Dave
 * @version 1.0
 */

public class ProjectDto {

	private String projectCode;
	private String projectName;
	private String projectDescription;
	private String groupDescription;
	private String repositoryCode;
	private boolean createSvn;
	private boolean createJob;
	private String svnStatus;
	private String jenkinsStatus;
	private String lastSync;

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getRepositoryCode() {
		return repositoryCode;
	}

	public void setRepositoryCode(String repositoryCode) {
		this.repositoryCode = repositoryCode;
	}

	public String getSvnStatus() {
		return svnStatus;
	}

	public void setSvnStatus(String svnStatus) {
		this.svnStatus = svnStatus;
	}

	public String getJenkinsStatus() {
		return jenkinsStatus;
	}

	public void setJenkinsStatus(String jenkinsStatus) {
		this.jenkinsStatus = jenkinsStatus;
	}

	public String getLastSync() {
		return lastSync;
	}

	/**
	 * @param lastSync
	 *            the lastSync to set
	 */
	public void setLastSync(String lastSync) {
		this.lastSync = lastSync;
	}

	public boolean isCreateSvn() {
		return createSvn;
	}

	public void setCreateSvn(boolean createSvn) {
		this.createSvn = createSvn;
	}

	public boolean isCreateJob() {
		return createJob;
	}

	public void setCreateJob(boolean createJob) {
		this.createJob = createJob;
	}

}
// end of User.java