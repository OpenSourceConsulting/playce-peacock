package com.athena.peacock.controller.web.alm.svn;

import javax.annotation.PostConstruct;

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

	private String SVN_URL = "svn://119.81.162.220/hiway/";
	private SVNRepository repository = null;

	@PostConstruct
	private void init() {

	}

	public void getSvn() throws SVNException {

		String url = "svn://119.81.162.220/hiway";
		String user = "userName";
		String password = "password";
		int historyCount = 3;

		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();

		// SVN Setting
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager("osc09", "osc09");
		SVNURL svnUrl = SVNURL.parseURIEncoded(url);
		SVNRepository repository = DAVRepositoryFactory.create(svnUrl);
		repository.setAuthenticationManager(authManager);

		ISVNEditor editor = repository.getCommitEditor("peacock", null);
		SVNCommitInfo commitInfo = addDir(editor, "test6");
		System.out.println("The directory was added: " + commitInfo);
		System.out.println(repository.getLocation());
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
		long latestRevision = repository.getLatestRevision();
		System.out.println(latestRevision);
		return latestRevision;
	}

}
