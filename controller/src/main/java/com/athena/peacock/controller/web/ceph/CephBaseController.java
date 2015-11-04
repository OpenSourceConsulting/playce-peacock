/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2015. 9. 28.		First Draft.
 */
package com.athena.peacock.controller.web.ceph;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;

/**
 * <pre>
 * Base controller for ceph control
 *  - each ceph controller should be extends CephBaseController
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class CephBaseController {
	
	@Inject
	@Named("cephService")
	private CephService cephService;
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object managementSubmit(String uri, HttpMethod method) throws RestClientException, Exception {
		return cephService.managementSubmit(uri, method);
	}
	//end of managementSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object calamariSubmit(String uri, HttpMethod method) throws RestClientException, Exception {
		return cephService.calamariSubmit(uri, method);
	}
	//end of calamariSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object radosgwSubmit(String uri, HttpMethod method) throws RestClientException, Exception {
		return cephService.radosgwSubmit(uri, method);
	}
	//end of radosgwSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object managementSubmit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return cephService.managementSubmit(uri, body, method);
	}
	//end of managementSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object calamariSubmit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return cephService.calamariSubmit(uri, body, method);
	}
	//end of calamariSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object radosgwSubmit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return cephService.radosgwSubmit(uri, body, method);
	}
	//end of radosgwSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param body
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object managementSubmit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		return cephService.managementSubmit(uri, body, method, acceptableMediaTypes, contentType);
	}
	//end of managementSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param body
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object calamariSubmit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		return cephService.calamariSubmit(uri, body, method, acceptableMediaTypes, contentType);
	}
	//end of calamariSubmit()
	
	/**
	 * <pre>
	 * do http request with given parameters
	 * </pre>
	 * @param uri
	 * @param body
	 * @param method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	protected Object radosgwSubmit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		return cephService.radosgwSubmit(uri, body, method, acceptableMediaTypes, contentType);
	}
	//end of radosgwSubmit()
	
	/**
	 * <pre>
	 * execute /usr/bin/ssh-copy-id to host
	 * </pre>
	 * @param host
	 * @param username
	 * @param passwd
	 * @return
	 * @throws IOException
	 */
	protected Object sshCopyId(String host, String username, String passwd) throws Exception {
		return cephService.sshCopyId(host, username, passwd);
	}
	//end of sshCopyId()
	
	/**
	 * <pre>
	 * execute ssh command
	 * </pre>
	 * @param command
	 * @return
	 * @throws IOException
	 */
	protected Object execute(String command) throws Exception {
		return cephService.execute(command);
	}
	//end of execute()
	
	/**
	 * <pre>
	 * Create an Json Array Node
	 * </pre>
	 * @return
	 */
	protected ArrayNode createArrayNode() {
		return cephService.createArrayNode();
	}
	//end of createArrayNode()
	
	/**
	 * <pre>
	 * Create an Json Object Node
	 * </pre>
	 * @return
	 */
	protected ObjectNode createObjectNode() {
		return cephService.createObjectNode();
	}
	//end of createObjectNode()
}
//end of CephBaseController.java