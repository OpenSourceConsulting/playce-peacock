package com.athena.peacock.controller.web.alm.svn;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;

/*
 * ReporterBaton implementation that always reports 'empty wc' state.
 */
public class ExportReporterBaton implements ISVNReporterBaton {

	private long exportRevision;

	public ExportReporterBaton(long revision) {
		exportRevision = revision;
	}

	public void report(ISVNReporter reporter) throws SVNException {
		try {
			/*
			 * Here empty working copy is reported.
			 * 
			 * ISVNReporter includes methods that allows to report mixed-rev
			 * working copy and even let server know that some files or
			 * directories are locally missing or locked.
			 */
			reporter.setPath("", null, exportRevision, true);

			/*
			 * Don't forget to finish the report!
			 */
			reporter.finishReport();
		} catch (SVNException svne) {
			reporter.abortReport();
			System.out.println("Report failed.");
		}
	}
}