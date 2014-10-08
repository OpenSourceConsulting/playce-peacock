package com.athena.peacock.controller.web.alm.svn;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.crowd.dto.ProjectUserDto;
import com.athena.peacock.controller.web.alm.project.AlmProjectDao;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectProcessStatusDto;
import com.athena.peacock.controller.web.alm.svn.dto.SvnGroupUserDto;
import com.athena.peacock.controller.web.alm.svn.dto.SvnProjectDto;
import com.athena.peacock.controller.web.alm.svn.dto.SvnSyncDto;
import com.athena.peacock.controller.web.alm.svn.dto.SvnUserDto;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.user.User;

@Service
public class AlmSvnService {

	protected final Logger logger = LoggerFactory
			.getLogger(AlmSvnService.class);

	@Value("#{contextProperties['alm.svn.url']}")
	private String SVN_URL;

	@Value("#{contextProperties['alm.svn.id']}")
	private String SVN_ID;

	@Value("#{contextProperties['alm.svn.pw']}")
	private String SVN_PW;

	@Autowired
	private AlmCrowdService crowdService;

	@Autowired
	private AlmProjectDao projectDao;

	// private String SVN_URL = "svn://119.81.162.220/hiway/";
	private SVNRepository repository = null;

	@PostConstruct
	private void init() {

	}

	public ProjectProcessStatusDto createSvnProject(String repositoryCode,
			String projectCode) {

		ProjectProcessStatusDto statusDto = new ProjectProcessStatusDto();

		try {
			String url = SVN_URL + "/" + repositoryCode;

			// SVNKit Setting
			SVNRepositoryFactoryImpl.setup();
			FSRepositoryFactory.setup();

			// SVN Connection Setting
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(SVN_ID, SVN_PW);
			SVNURL svnUrl = SVNURL.parseURIEncoded(url);
			SVNRepository repository = DAVRepositoryFactory.create(svnUrl);
			repository.setAuthenticationManager(authManager);

			ISVNEditor editor = repository.getCommitEditor(
					"peacock create project", null);
			SVNCommitInfo commitInfo = addDir(editor, projectCode);
			statusDto.setSuccess(true);

		} catch (SVNException e) {
			statusDto.setSuccess(false);
			statusDto.setErrorMessage(e.getMessage());
			logger.info(e.getMessage());
		}

		return statusDto;

	}

	private SVNCommitInfo addDir(ISVNEditor editor, String dirPath)
			throws SVNException {

		editor.openRoot(-1);
		editor.addDir(dirPath, null, -1);
		editor.closeDir();
		editor.closeDir();
		return editor.closeEdit();
	}

	private long getLastesRevision() throws SVNException {

		int historyCount = 3;
		long latestRevision = repository.getLatestRevision();
		return latestRevision;
	}

	public boolean checkSvnProject(String repositoryCode) {

		try {
			String url = SVN_URL + "/" + repositoryCode;

			// SVNKit Setting
			SVNRepositoryFactoryImpl.setup();
			FSRepositoryFactory.setup();

			// SVN Connection Setting
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(SVN_ID, SVN_PW);
			SVNURL svnUrl = SVNURL.parseURIEncoded(url);
			SVNRepository repository = DAVRepositoryFactory.create(svnUrl);
			repository.setAuthenticationManager(authManager);
			repository.getLatestRevision();
			return true;

		} catch (SVNException e) {
			logger.info(e.getMessage());
			return false;
		}

	}

	public String getSvnUrl() {

		return SVN_URL;
	}

	public SvnSyncDto getSvnSyncInfomation() {

		SvnSyncDto sync = new SvnSyncDto();

		// User List 생성
		List<ProjectUserDto> users = crowdService.getProjectAllUser();
		List<SvnUserDto> svnUser = new ArrayList<SvnUserDto>();

		for (ProjectUserDto user : users) {
			SvnUserDto tmpUser = new SvnUserDto();
			tmpUser.setUsername(user.getUSERNAME());
			tmpUser.setPassword(user.getPASSWORD());
			svnUser.add(tmpUser);
		}

		sync.setUsers(svnUser);

		// Group User 생성
		List<SvnGroupUserDto> groupUsers = new ArrayList<SvnGroupUserDto>();

		List<Group> groups = crowdService.getNestedChildGroupsOfGroup();

		if (groups != null) {

			for (Group group : groups) {

				try {
					List<User> groupusers = crowdService.getGroupUsers(group
							.getName());

					SvnGroupUserDto tmpGroup = new SvnGroupUserDto();
					tmpGroup.setGroupName(group.getName());

					StringBuffer sb = new StringBuffer();

					for (User groupuser : groupusers) {
						sb.append(groupuser.getName());
						sb.append(",");
					}

					tmpGroup.setUserList(sb.toString());
					groupUsers.add(tmpGroup);
				} catch (GroupNotFoundException
						| ApplicationPermissionException
						| InvalidAuthenticationException
						| OperationFailedException e) {
					// TODO Auto-generated catch block
					logger.info(e.getMessage());
				}
			}
		}

		sync.setGroups(groupUsers);

		// Project Permission 생성
		List<SvnProjectDto> projects = new ArrayList<SvnProjectDto>();

		List<ProjectDto> projectList = projectDao.getProjectList();

		for (ProjectDto project : projectList) {
			SvnProjectDto tmp = new SvnProjectDto();
			tmp.setProjectCode(project.getProjectCode());
			tmp.setRepository(project.getRepositoryCode());
			projects.add(tmp);
		}

		sync.setProjects(projects);

		return sync;

	}
}
