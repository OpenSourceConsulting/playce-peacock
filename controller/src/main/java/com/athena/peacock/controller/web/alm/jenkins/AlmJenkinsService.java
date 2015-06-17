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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.jenkins.client.JenkinsServer;
import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JenkinsResponseDto;
import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JobDto;
import com.athena.peacock.controller.web.alm.project.AlmProjectDao;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
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
	private AlmProjectDao projectDao;
	
	private JenkinsServer jenkinsServer;

	@PostConstruct
	private void init() {
		try {
			jenkinsServer = new JenkinsServer(new URI(JENKINS_URL), JENKINS_ID,
					JENKINS_PW);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GridJsonResponse getJobs(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();
		List<JobDto> jobs = null;

		try {
			JenkinsResponseDto dto = jenkinsServer.getJobs();

			if (gridParam != null && gridParam.getSearch() != null) {
				jobs = getJobSearch(dto.getJobs(), gridParam.getSearch());
			} else {
				jobs = dto.getJobs();
			}

			Collections.sort(jobs, new JobNameComparator());
			response.setList(jobs);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			response.setSuccess(false);
			response.setMsg(e.getMessage());
		}

		return response;
	}

	public String copyJob(ProjectMappingDto jenkins) {

		ProjectDto projectDto = projectDao.getProject(jenkins.getMappingCode());
		
		String url = JENKINS_URL + "/job/JobCopy/buildWithParameters";

		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("TEMPLATE_NAME", "JobTemplate");
		parameter.put("JOB_NAME", jenkins.getMappingCode());
		parameter.put("REPOSITORY_NAME", "svn://svn.hiway.hhi.co.kr/"+projectDto.getRepositoryCode());
		parameter.put("JOBTYPE", "");

		try {
			jenkinsServer.copyJob(url, parameter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "OK";

	}

	public String copyPermission(String jobName, String userName) {
		String message = "OK";
		
		String url = JENKINS_URL + "/job/JobPermission/buildWithParameters";

		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("JOB_NAME", jobName);
		parameter.put("USER_NAME", userName);

		try {
			jenkinsServer.copyJob(url, parameter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = e.getMessage();
		}

		return message;

	}

	public class JobNameComparator implements Comparator<JobDto> {

		@Override
		public int compare(JobDto arg0, JobDto arg1) {
			// TODO Auto-generated method stub
			return arg0.getName().compareTo(arg1.getName());
		}
	}

	public List<JobDto> getJobSearch(List<JobDto> jobs, String search) {

		List<JobDto> searchJobs = new ArrayList<JobDto>();

		for (JobDto job : jobs) {

			if (job.getName().contains(search)) {
				searchJobs.add(job);
			}
		}

		return searchJobs;
	}

}
// end of AlmUserController.java