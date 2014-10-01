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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.jenkins.client.JenkinsClient;
import com.athena.peacock.controller.web.alm.jenkins.client.JenkinsServer;
import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JenkinsResponseDto;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * Jenkins 관련 Job Service
 * </pre>
 * 
 * @author Jungsu Han
 * @version 1.0
 */
@Service
public class AlmJenkinsService {

	@Value("#{contextProperties['alm.jenkins.url']}")
	private String JENKINS_URL;

	@Value("#{contextProperties['alm.jenkins.id']}")
	private String JENKINS_ID;

	@Value("#{contextProperties['alm.jenkins.pw']}")
	private String JENKINS_PW;

	@Value("#{contextProperties['alm.jenkins.servertemplate']}")
	private String JENKINS_SERVERTEMPLATE;

	@Value("#{contextProperties['alm.jenkins.mobiletemplate']}")
	private String JENKINS_MOBILETEMPLATE;

	@Autowired
	private JenkinsClient jenkinsClient;

	private JenkinsServer jenkins;

	@PostConstruct
	private void init() {
		try {
			jenkins = new JenkinsServer(new URI(JENKINS_URL), JENKINS_ID,
					JENKINS_PW);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GridJsonResponse getJobs() {

		GridJsonResponse response = new GridJsonResponse();

		try {
			JenkinsResponseDto dto = jenkins.getJobs();
			response.setList(dto.getJobs());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public void createJob(String templateName, String newJobName) {
		jenkinsClient.copyJob(templateName, newJobName);
	}

}
// end of AlmUserController.java