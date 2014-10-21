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
package com.athena.peacock.controller.web.alm.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.alm.jenkins.AlmJenkinsService;
import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JobNotificationDto;
import com.athena.peacock.controller.web.alm.nexus.client.NexusClient;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectUserPasswordResetDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectWizardDto;
import com.athena.peacock.controller.web.alm.svn.AlmSvnService;
import com.athena.peacock.controller.web.alm.svn.dto.SvnSyncDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 프로젝트 관련 컨트롤러.
 * </pre>
 * 
 * @author Dave Han
 * @version 1.0
 */
@Controller
@RequestMapping("/alm")
public class AlmProjectController {

	@Autowired
	private AlmProjectService almProjectService;

	@Autowired
	private AlmProjectProcess processService;

	@Autowired
	private NexusClient nexus;

	@Autowired
	private AlmJenkinsService jenkinsService;

	@Autowired
	private AlmSvnService svnService;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AlmProjectController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * <pre>
	 * Project 리스트
	 * </pre>
	 * 
	 * @param gridParam
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/jenkins/job", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse jenkinsJobList(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "search", required = false) String search) {

		ExtjsGridParam gridParam = new ExtjsGridParam();
		gridParam.setPage(offset);

		if (search != null) {
			gridParam.setSearch(search);
		}

		return jenkinsService.getJobs(gridParam);
	}

	@RequestMapping(value = "/jenkins/test", method = RequestMethod.GET)
	public @ResponseBody
	String test() {
		return jenkinsService.copyJob(null);
	}

	/**
	 * <pre>
	 * Project 리스트
	 * </pre>
	 * 
	 * @param gridParam
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse list(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "search", required = false) String search) {

		ExtjsGridParam gridParam = new ExtjsGridParam();
		gridParam.setPage(offset);
		
		if (search != null) {
			gridParam.setSearch(search);
		}

		return almProjectService.getProjectList(gridParam);
	}

	/**
	 * <pre>
	 * Project 등록
	 * </pre>
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse createProject(@RequestBody ProjectDto project) {
		return almProjectService.createProject(project);
	}

	/**
	 * <pre>
	 * Project 상세 정보
	 * </pre>
	 * 
	 * @param projectCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/{projectCode}", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse list(@PathVariable String projectCode) {
		return almProjectService.getProject(projectCode);
	}

	/**
	 * <pre>
	 * Jenkins 목록 조회
	 * </pre>
	 * 
	 * @param projectCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/{projectCode}/jenkins", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse getJenkinsJobs(@PathVariable String projectCode) {
		return almProjectService.getProject(projectCode);
	}

	/**
	 * <pre>
	 * 프로젝트 에 Job, Confluence 추가
	 * </pre>
	 * 
	 * @param projectCode
	 * @param mappingType
	 * @param mappingCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/{projectCode}/{mappingType}/{mappingCode}", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse createProjectMapping(
			@PathVariable String projectCode,
			@PathVariable String mappingType,
			@PathVariable String mappingCode,
			@RequestParam(value = "permission", required = false) String permission) {

		return almProjectService.createProjectMapping(projectCode, mappingType,
				mappingCode, permission);
	}

	/**
	 * <pre>
	 * 프로젝트 에 Job, Confluence 삭제
	 * </pre>
	 * 
	 * @param projectCode
	 * @param mappingType
	 * @param mappingCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/{projectCode}/{mappingType}/{mappingCode}", method = RequestMethod.DELETE)
	public @ResponseBody
	DtoJsonResponse deleteProjectMapping(@PathVariable String projectCode,
			@PathVariable String mappingType, @PathVariable String mappingCode) {

		return almProjectService.deleteProjectMapping(projectCode, mappingType,
				mappingCode);
	}

	/**
	 * <pre>
	 * 프로젝트 할당된 Job, Confluence 목록 조회
	 * </pre>
	 * 
	 * @param projectCode
	 * @param mappingType
	 * @param mappingCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/{projectCode}/{mappingType}", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse getProjectMapping(@PathVariable String projectCode,
			@PathVariable String mappingType) {

		return almProjectService.getProjectMapping(projectCode, mappingType);
	}

	/**
	 * <pre>
	 * 프로젝트 작업 히스토리 조회
	 * </pre>
	 * 
	 * @param projectCode
	 * @param mappingType
	 * @param mappingCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/{projectCode}/history", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse getProjectHistory(@PathVariable String projectCode) {
		return almProjectService.getProjectHistory(projectCode);
	}

	/**
	 * <pre>
	 * 프로젝트 Wizard 실행
	 * </pre>
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/wizard", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse createProjectWizard(@RequestBody ProjectWizardDto project) {
		return almProjectService.createProjectWizrd(project);
	}

	// User 등록
	@RequestMapping(value = "/project/{projectCode}/usermanagement/{username}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse addProjectUser(@PathVariable String projectCode,
			@PathVariable String username) {
		return almProjectService.addProjectUser(projectCode, username);
	}

	// User 삭제
	@RequestMapping(value = "/project/{projectCode}/usermanagement/{username}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	DtoJsonResponse removeProejctUser(@PathVariable String projectCode,
			@PathVariable String username) {
		return almProjectService.removeProjectUser(projectCode, username);
	}

	/**
	 * <pre>
	 * 프로젝트 code 중복 여부 체크
	 * </pre>
	 * 
	 * @param projectCode
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/project/wizard/{projectCode}", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse checkProjectCode(@PathVariable String projectCode) {
		return almProjectService.checkProjectCode(projectCode);
	}

	@RequestMapping(value = "/project/startjob", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse startJob() {
		processService.processProjectMapping();
		return null;
	}

	@RequestMapping(value = "/project/nexus/archetype", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse getnexus() {
		DtoJsonResponse response = new DtoJsonResponse();
		response.setData(nexus.getArchetype());
		return response;
	}

	@RequestMapping(value = "/project/jenkins/notification", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse getJenkinsNotification(
			@RequestBody JobNotificationDto notification) {
		System.out.println("****************************");
		System.out.println(notification.getName());
		System.out.println(notification.getUrl());
		DtoJsonResponse response = new DtoJsonResponse();
		return response;
	}

	/**
	 * <pre>
	 * 사용자 패스워드 변경
	 * </pre>
	 * 
	 * @param projectCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/user/passwordreset", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse getCrypto(@RequestBody ProjectUserPasswordResetDto resetDto) {
		return almProjectService.resetPassword(resetDto);
	}

	/**
	 * <pre>
	 * 사용자 패스워드 변경
	 * </pre>
	 * 
	 * @param projectCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/user/sendtest", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse sendTest() {
		return almProjectService.resetPasswordEmail(null);
	}

	/**
	 * <pre>
	 * SVN Sync
	 * </pre>
	 * 
	 * @param projectCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/svn/sync", method = RequestMethod.GET)
	public @ResponseBody
	SvnSyncDto svnSync() {
		return svnService.getSvnSyncInfomation();
	}

	@RequestMapping(value = "/project/jenkins/sync/{projectCode}", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse syncJenkins(@PathVariable String projectCode) {
		return almProjectService.syncJenkins(projectCode);
	}

}
// end of AlmUserController.java