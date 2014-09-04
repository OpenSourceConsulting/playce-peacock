package kr.osci.peacockauth;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import hudson.model.UnprotectedRootAction;
import hudson.security.ACL;
import hudson.security.SecurityRealm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import jenkins.model.Jenkins;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class PeacockRootAction implements UnprotectedRootAction {

    private static final Logger LOGGER = Logger.getLogger(PeacockRootAction.class.getName());

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIconFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrlName() {
		// TODO Auto-generated method stub
		return "peacock";
	}

	public void doBuild(StaplerRequest req, StaplerResponse rsp,
			@QueryParameter String job) throws IOException, ServletException {
		LOGGER.log(Level.INFO, "build on {0}", job);
		
		
		rsp.setContentType("application/json");
        PrintWriter w = rsp.getWriter();
        w.write("{\"status\":\"OK\"}");
        w.close();

	}
	
	public void doGetList(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
		
		 throw HttpResponses.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new IOException(" is not buildable"));

	}

}
