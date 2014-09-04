package kr.osci.peacockauth;

/*
 * The MIT License
 * 
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi, Yahoo! Inc., Seiji Sogabe, Tom Huybrechts
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import hudson.diagnosis.OldDataMonitor;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.AuthorizationStrategy;
import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.SidACL;
import hudson.security.UserMayOrMayNotExistException;
import hudson.security.SecurityRealm;
import hudson.util.FormValidation;
import hudson.util.FormValidation.Kind;
import hudson.util.VersionNumber;
import hudson.util.RobustReflectionConverter;
import hudson.Functions;
import hudson.Extension;
import hudson.model.User;
import net.sf.json.JSONObject;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.acls.sid.Sid;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;
import org.springframework.dao.DataAccessException;

import javax.servlet.ServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * {@link GlobalMatrixAuthorizationStrategy} plus per-project ACL.
 * 
 * <p>
 * Per-project ACL is stored in {@link AuthorizationMatrixProperty}.
 * 
 * @author Kohsuke Kawaguchi
 */
public class ProjectMatrixAuthorizationStrategy extends AuthorizationStrategy {

	private static final Logger LOG = Logger
			.getLogger(ProjectMatrixAuthorizationStrategy.class.getName());

	private transient SidACL acl = new AclImpl();

	private final Map<Permission, Set<String>> grantedPermissions = new HashMap<Permission, Set<String>>();

	private final Set<String> sids = new HashSet<String>();

	/** Contains the Crowd server URL. */
	public String url = "";

	/** Contains the application name to access Crowd. */
	public String applicationName = "";

	/** Contains the application password to access Crowd. */
	public String password = "";

	public ProjectMatrixAuthorizationStrategy() {

	}

	@DataBoundConstructor
	public ProjectMatrixAuthorizationStrategy(String url,
			String applicationName, String password) {

		this.url = url.trim();
		this.applicationName = applicationName.trim();
		this.password = password.trim();
	}

	@Override
	public ACL getRootACL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	private final class AclImpl extends SidACL {
		protected Boolean hasPermission(Sid p, Permission permission) {
			if (ProjectMatrixAuthorizationStrategy.this.hasPermission(
					toString(p), permission))
				return true;
			return null;
		}
	}

	/**
	 * Checks if the given SID has the given permission.
	 */
	public boolean hasPermission(String sid, Permission p) {
		for (; p != null; p = p.impliedBy) {
			Set<String> set = grantedPermissions.get(p);
			if (set != null && set.contains(sid) && p.getEnabled())
				return true;
		}
		return false;
	}

	/**
	 * Checks if the permission is explicitly given, instead of implied through
	 * {@link Permission#impliedBy}.
	 */
	public boolean hasExplicitPermission(String sid, Permission p) {
		Set<String> set = grantedPermissions.get(p);
		return set != null && set.contains(sid) && p.getEnabled();
	}

	@Extension
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

	public static class DescriptorImpl extends
			Descriptor<AuthorizationStrategy> {
		protected DescriptorImpl(
				Class<? extends ProjectMatrixAuthorizationStrategy> clazz) {
			super(clazz);
		}

		public DescriptorImpl() {
		}

		public String getDisplayName() {
			return "Athena Peacock ALM";
		}

		@Override
		public AuthorizationStrategy newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {

			ProjectMatrixAuthorizationStrategy gmas = create();
			Map<String, Object> data = formData.getJSONObject("data");
			for (Map.Entry<String, Object> r : data.entrySet()) {
				String sid = r.getKey();
				if (!(r.getValue() instanceof JSONObject)) {
					throw new FormException("not an object: " + formData,
							"data");
				}

				Map<String, Object> value = (JSONObject) r.getValue();
				for (Map.Entry<String, Object> e : value.entrySet()) {
					if (!(e.getValue() instanceof Boolean)) {
						throw new FormException("not an boolean: " + formData,
								"data");
					}
					if ((Boolean) e.getValue()) {
						Permission p = Permission.fromId(e.getKey());
						// gmas.add(p, sid);
					}
				}
			}
			return gmas;
		}

		protected ProjectMatrixAuthorizationStrategy create() {
			return new ProjectMatrixAuthorizationStrategy();
		}

		public List<PermissionGroup> getAllGroups() {
			List<PermissionGroup> groups = new ArrayList<PermissionGroup>(
					PermissionGroup.getAll());
			groups.remove(PermissionGroup.get(Permission.class));
			return groups;
		}

		public boolean showPermission(Permission p) {
			return p.getEnabled();
		}

		public FormValidation doCheckName(@QueryParameter String value)
				throws IOException, ServletException {
			return doCheckName_(value, Jenkins.getInstance(),
					Jenkins.ADMINISTER);
		}

		public FormValidation doCheckName_(String value,
				AccessControlled subject, Permission permission)
				throws IOException, ServletException {
			if (!subject.hasPermission(permission))
				return FormValidation.ok(); // can't check

			final String v = value.substring(1, value.length() - 1);
			SecurityRealm sr = Jenkins.getInstance().getSecurityRealm();
			String ev = Functions.escape(v);

			if (v.equals("authenticated"))
				// system reserved group
				return FormValidation
						.respond(Kind.OK, makeImg("user.png") + ev);

			try {
				try {
					sr.loadUserByUsername(v);
					return FormValidation.respond(Kind.OK,
							makeImg("person.png") + ev);
				} catch (UserMayOrMayNotExistException e) {
					// undecidable, meaning the user may exist
					return FormValidation.respond(Kind.OK, ev);
				} catch (UsernameNotFoundException e) {
					// fall through next
				} catch (DataAccessException e) {
					// fall through next
				} catch (AuthenticationException e) {
					// other seemingly unexpected error.
					return FormValidation
							.error(e,
									"Failed to test the validity of the user name "
											+ v);
				}

				try {
					sr.loadGroupByGroupname(v);
					return FormValidation.respond(Kind.OK, makeImg("user.png")
							+ ev);
				} catch (UserMayOrMayNotExistException e) {
					// undecidable, meaning the group may exist
					return FormValidation.respond(Kind.OK, ev);
				} catch (UsernameNotFoundException e) {
					// fall through next
				} catch (DataAccessException e) {
					// fall through next
				} catch (AuthenticationException e) {
					// other seemingly unexpected error.
					return FormValidation.error(e,
							"Failed to test the validity of the group name "
									+ v);
				}

				// couldn't find it. it doesn't exist
				return FormValidation.respond(Kind.ERROR, makeImg("error.png")
						+ ev);
			} catch (Exception e) {
				// if the check fails miserably, we still want the user to be
				// able to see the name of the user,
				// so use 'ev' as the message
				return FormValidation.error(e, ev);
			}
		}

		private String makeImg(String gif) {
			return String
					.format("<img src='%s%s/images/16x16/%s' style='margin-right:0.2em'>",
							Stapler.getCurrentRequest().getContextPath(),
							Jenkins.RESOURCE_PATH, gif);
		}
	}

}