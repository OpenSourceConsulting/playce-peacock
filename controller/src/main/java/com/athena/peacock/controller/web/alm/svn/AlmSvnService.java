package com.athena.peacock.controller.web.alm.svn;

import javax.annotation.PostConstruct;

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

@Service
public class AlmSvnService {
	
	@Value("#{contextProperties['alm.svn.url']}")
	private String SVN_URL;
	
	@Value("#{contextProperties['alm.svn.id']}")
	private String SVN_ID;
	
	@Value("#{contextProperties['alm.svn.pw']}")
	private String SVN_PW;

	//private String SVN_URL = "svn://119.81.162.220/hiway/";
	private SVNRepository repository = null;

	@PostConstruct
	private void init() {

	}

	public void createSvnProject(String repositoryCode, String projectCode) throws SVNException {

		String url = SVN_URL +"/"+repositoryCode;

		// SVNKit Setting
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();

		// SVN Connection Setting
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(SVN_ID, SVN_PW);
		SVNURL svnUrl = SVNURL.parseURIEncoded(url);
		SVNRepository repository = DAVRepositoryFactory.create(svnUrl);
		repository.setAuthenticationManager(authManager);

		ISVNEditor editor = repository.getCommitEditor("peacock create project", null);
		SVNCommitInfo commitInfo = addDir(editor, projectCode);
/*		System.out.println("The directory was added: " + commitInfo);
		System.out.println(repository.getLocation());*/
		// SVNCommitClient svnCommitClient = repository.getCommitClient();

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
		System.out.println(latestRevision);
		return latestRevision;
	}

}
