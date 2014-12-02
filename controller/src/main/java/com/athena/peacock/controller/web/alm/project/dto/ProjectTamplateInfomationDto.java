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


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */

public class ProjectTamplateInfomationDto {

	private String projectRepository;
	private String projecttTempalte;
	private String projectGroupId;
	private String projectArtifcatId;
	private String projectPackage;

	public ProjectTamplateInfomationDto() {
		super();
	}

	public ProjectTamplateInfomationDto(String projectRepository,
			String projecttTempalte, String projectGroupId,
			String projectArtifcatId, String projectPackage) {
		super();
		this.projectRepository = projectRepository;
		this.projecttTempalte = projecttTempalte;
		this.projectGroupId = projectGroupId;
		this.projectArtifcatId = projectArtifcatId;
		this.projectPackage = projectPackage;
	}

	public String getProjectRepository() {
		return projectRepository;
	}

	public void setProjectRepository(String projectRepository) {
		this.projectRepository = projectRepository;
	}

	public String getProjecttTempalte() {
		return projecttTempalte;
	}

	public void setProjecttTempalte(String projecttTempalte) {
		this.projecttTempalte = projecttTempalte;
	}

	public String getProjectGroupId() {
		return projectGroupId;
	}

	public void setProjectGroupId(String projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

	public String getProjectArtifcatId() {
		return projectArtifcatId;
	}

	public void setProjectArtifcatId(String projectArtifcatId) {
		this.projectArtifcatId = projectArtifcatId;
	}

	public String getProjectPackage() {
		return projectPackage;
	}

	public void setProjectPackage(String projectPackage) {
		this.projectPackage = projectPackage;
	}

}
// end of User.java