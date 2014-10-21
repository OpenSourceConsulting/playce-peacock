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
 * Bong-Jin Kwon	2014. 10. 14.		First Draft.
 */
package com.athena.peacock.controller.web.alm.svn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.athena.peacock.controller.web.alm.project.AlmProjectDao;
import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;

/**
 * @author Bong-Jin Kwon
 * 
 */
@Service
public class UserValidationService {

	@Autowired
	private AlmProjectDao projectDao;

	public UserValidationService() {
		// TODO Auto-generated constructor stub
	}

	// Project 리스트 정보
	public List<ProjectDto> getProjectList() {

		List<ProjectDto> projects = projectDao.getProjectList(null);

		return projects;

	}

	public long getLatestRevision(String svnUrl, String userName,
			String userPassword) throws SVNException {

		SVNURL url = SVNURL.parseURIEncoded(svnUrl);

		SVNRepository repository = SVNRepositoryFactory.create(url);

		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(userName, userPassword);
		repository.setAuthenticationManager(authManager);

		SVNNodeKind nodeKind = repository.checkPath("", -1);
		if (nodeKind == SVNNodeKind.NONE) {
			SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN,
					"No entry at URL ''{0}''", url);
			throw new SVNException(err);
		} else if (nodeKind == SVNNodeKind.FILE) {
			SVNErrorMessage err = SVNErrorMessage
					.create(SVNErrorCode.UNKNOWN,
							"Entry at URL ''{0}'' is a file while directory was expected",
							url);
			throw new SVNException(err);
		}

		long latestRevision = repository.getLatestRevision();

		return latestRevision;
	}

}
