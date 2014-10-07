package com.athena.peacock.controller.web.alm.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.confluence.AlmConfluenceService;
import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.jenkins.AlmJenkinsService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectHistoryDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectProcessStatusDto;
import com.athena.peacock.controller.web.alm.svn.AlmSvnService;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.atlassian.crowd.model.user.User;

/**
 * <pre>
 * 프로젝트 맵핑 처리하는 프로세스
 * </pre>
 * 
 * @author Dave Han
 * @version 1.0
 */
@Service
public class AlmProjectProcess {

	@Autowired
	private AlmProjectDao projectDao;

	@Autowired
	private AlmSvnService svnService;

	@Autowired
	private AlmConfluenceService confluenceService;

	@Autowired
	private AlmJenkinsService jenkinsService;

	@Autowired
	private AlmCrowdService crowdService;

	public void processProjectMapping() {

		List<ProjectMappingDto> lists = projectDao.getProjectMappingStandBy();

		for (ProjectMappingDto list : lists) {

			ProjectProcessStatusDto statusDto = null;

			// Check Start Time

			projectDao.startProjectMappingJob(list);

			// Process
			if (list.getMappingType() == 10) { // SPACE 권한 생성

				statusDto = addPermissionToSpace(list);

			} else if (list.getMappingType() == 20) {

				if (list.getMappingExecution() != null
						&& list.getMappingExecution().equals("CREATE")) {
					createJenkinsJob(list);
				}

				// 퍼미션 추가
				statusDto = createJenkinsPermission(list);

			} else if (list.getMappingType() == 30) { // SVN Project 생성

				projectDao.startProjectMappingJob(list);
				statusDto = createSvnProject(list);

			}

			if (statusDto != null) {
				if (statusDto.isSuccess()) {
					list.setStatus("COMPLETE");
				} else {
					list.setStatus("FAIL");
					list.setExitMessage(statusDto.getErrorMessage());
				}

				// Check End Time
				projectDao.endProjectMappingJob(list);
			}
		}
	}

	// SVN 생성
	private ProjectProcessStatusDto createSvnProject(ProjectMappingDto svn) {

		ProjectProcessStatusDto statusDto = svnService.createSvnProject(
				"hiway", svn.getProjectCode());

		if (statusDto.isSuccess()) {
			createProjectHistor(svn.getProjectCode(), svn.getProjectCode()
					+ " 프로젝트에 " + svn.getMappingCode()
					+ " SVN Project 생성 되었습니다.");
		} else {
			createProjectHistor(svn.getProjectCode(), svn.getProjectCode()
					+ " 프로젝트에 " + svn.getMappingCode()
					+ " SVN Project 생성이 실패되었습니다." + statusDto.getErrorMessage());
		}

		return statusDto;

	}

	// 작업 History 저장
	private void createProjectHistor(String projectCode, String message) {

		ProjectHistoryDto history = new ProjectHistoryDto();
		history.setProjectCode(projectCode);
		history.setMessage(message);
		projectDao.insertProjectHistory(history);

	}

	// Confluence 권한 생성
	private ProjectProcessStatusDto addPermissionToSpace(
			ProjectMappingDto spaces) {

		ProjectProcessStatusDto statusDto = confluenceService.addPermissions(
				spaces.getProjectCode(), spaces.getMappingCode(),
				spaces.getMappingPermission());

		if (statusDto.isSuccess()) {
			createProjectHistor(
					spaces.getProjectCode(),
					spaces.getProjectCode() + " 프로젝트에 "
							+ spaces.getMappingCode()
							+ " Confluence Space 권한 생성 되었습니다.");
		} else {
			createProjectHistor(
					spaces.getProjectCode(),
					spaces.getProjectCode() + " 프로젝트에 "
							+ spaces.getMappingCode()
							+ " Confluence Space 권한 생성이 샐패했습니다."
							+ statusDto.getErrorMessage());
		}

		return statusDto;

	}

	// SVN 생성
	private ProjectProcessStatusDto createJenkinsJob(ProjectMappingDto jenkins) {
		ProjectProcessStatusDto statusDto = new ProjectProcessStatusDto();
		statusDto.setSuccess(true);
		jenkinsService.copyJob(jenkins);
		return statusDto;
	}

	private ProjectProcessStatusDto createJenkinsPermission(
			ProjectMappingDto jenkins) {

		ProjectProcessStatusDto statusDto = new ProjectProcessStatusDto();
		statusDto.setSuccess(true);

		List<User> users = crowdService.getGroupUserList(jenkins
				.getProjectCode());

		for (User user : users) {
			jenkinsService.copyPermission(jenkins.getProjectCode(),
					user.getName());
		}

		return statusDto;
	}
}
