/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 9. 22.		First Draft.
 */
package com.athena.peacock.controller.web.alm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.athena.peacock.common.provider.AppContext;
import com.athena.peacock.controller.web.alm.jenkins.AlmJenkinsService;
import com.athena.peacock.controller.web.alm.project.AlmProjectService;
import com.athena.peacock.controller.web.alm.repository.AlmReposirotyService;

/**
 * <pre>
 * Dashboard 데이터 조회용 컴포넌트
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component("almDashboardComponent")
public class AlmDashboardComponent {

	@Autowired
	private AlmJenkinsService jenkinsService;

	@Autowired
	private AlmReposirotyService repositoryService;

	@Autowired
	private AlmProjectService projectService;

	public int getProjectCnt() {

		if (projectService == null) {
			projectService = AppContext.getBean(AlmProjectService.class);
		}

		return projectService.getProjectCount();
	}

	public int getSvnCnt() {

		if (repositoryService == null) {
			repositoryService = AppContext.getBean(AlmReposirotyService.class);
		}

		return repositoryService.getRepositoryCount();
	}

	public int getJenkinsCnt() {
		if (jenkinsService == null) {
			jenkinsService = AppContext.getBean(AlmJenkinsService.class);
		}

		return jenkinsService.getJobs(null).getList().size();
	}
}
// end of AlmDashboardComponent.java