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
 * Bong-Jin Kwon	2013. 9. 24.		First Draft.
 */
package com.athena.peacock.controller.web.alm.jenkins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.jenkins.client.JenkinsClient;
import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JenkinsResponseDto;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * 
 * @author Jungsu Han
 * @version 1.0
 */
@Service
public class AlmJenkinsService {

	@Value("#{contextProperties['alm.jenkins.servertemplate']}")
	private String JENKINS_SERVERTEMPLATE;

	@Value("#{contextProperties['alm.jenkins.mobiletemplate']}")
	private String JENKINS_MOBILETEMPLATE;

	@Autowired
	private JenkinsClient jenkinsClient;

	public GridJsonResponse getJobs() {

		GridJsonResponse response = new GridJsonResponse();
		JenkinsResponseDto dto = jenkinsClient.getJobs();
		response.setList(dto.getJobs());
		return response;
	}

	public void createJob(String templateName, String newJobName) {
		jenkinsClient.copyJob(templateName, newJobName);
	}

}
// end of AlmUserController.java