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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectWizardDto;
import com.athena.peacock.controller.web.alm.svn.AlmSvnService;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.google.gson.Gson;

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
	private AlmCrowdService almCrowdService;

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
		//
		// almCrowdService.addGroup(groupData);
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
	DtoJsonResponse createProjectMapping(@PathVariable String projectCode,
			@PathVariable String mappingType, @PathVariable String mappingCode) {

		return almProjectService.createProjectMapping(projectCode, mappingType,
				mappingCode);
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
		Gson gson = new Gson();
		return almProjectService.createProjectWizrd(project);
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

	/*@RequestMapping(value = "/project/test", method = RequestMethod.GET)
	public @ResponseBody
	ProjectWizardDto getProjectMappingTest() {
		return almProjectService.getWizard();
	}
	
	@RequestMapping(value = "/project/svn", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse svnHistory() {
		try {
			svnService.getSvn();
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		// return almProjectService.createProjectWizrd();
	}*/

}
// end of AlmUserController.java