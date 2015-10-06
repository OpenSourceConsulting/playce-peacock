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
 * Sang-cheon Park	2015. 10. 6.		First Draft.
 */
package com.athena.peacock.controller.web.ceph;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.common.core.action.support.TargetHost;
import com.athena.peacock.common.core.util.SshExecUtil;
import com.athena.peacock.common.rest.PeacockRestTemplate;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("cephService")
public class CephService {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String JSON_PREFIX1 = "{";
	private static final String JSON_PREFIX2 = "[";
	
    @Value("#{contextProperties['ceph.management.rest.api.prefix']}")
    private String management;
	
    @Value("#{contextProperties['ceph.calamari.rest.api.prefix']}")
    private String calamari;
	
    @Value("#{contextProperties['ceph.radosgw.rest.api.prefix']}")
    private String radosgw;
	
    @Value("#{contextProperties['ceph.calamari.username']}")
    private String calamariUsername;
	
    @Value("#{contextProperties['ceph.calamari.password']}")
    private String calamariPassword;
    
    @Value("#{contextProperties['ceph.ssh.host']}")
    private String sshHost;
    
    @Value("#{contextProperties['ceph.ssh.ip']}")
    private int sshIp;
    
    @Value("#{contextProperties['ceph.ssh.username']}")
    private String sshUsername;
    
    @Value("#{contextProperties['ceph.ssh.password']}")
    private String sshPassword;

	@Inject
	@Named("peacockRestTemplate")
	private PeacockRestTemplate peacockRestTemplate;
	
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
	public Object managementSubmit(String uri, HttpMethod method) throws RestClientException, Exception {
		return managementSubmit(uri, null, method);
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
	public Object calamariSubmit(String uri, HttpMethod method) throws RestClientException, Exception {
		return calamariSubmit(uri, null, method);
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
	public Object radosgwSubmit(String uri, HttpMethod method) throws RestClientException, Exception {
		return radosgwSubmit(uri, null, method);
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
	public Object managementSubmit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return managementSubmit(uri, body, method, null, null);
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
	public Object calamariSubmit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return calamariSubmit(uri, body, method, null, null);
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
	public Object radosgwSubmit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return radosgwSubmit(uri, body, method, null, null);
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
	public Object managementSubmit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		String response = null;
		
		if (uri.startsWith("http")) {
			response = peacockRestTemplate.submit(uri, body, method, acceptableMediaTypes, contentType);
		} else {
			response = peacockRestTemplate.submit(management + uri, body, method, acceptableMediaTypes, contentType);
		}
		
		if (response != null && (response.trim().startsWith(JSON_PREFIX1) || response.trim().startsWith(JSON_PREFIX2))) {
			return readTree(response);
		} else {
			return response;
		}
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
	public Object calamariSubmit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		String response = null;
		peacockRestTemplate.calamariLogin(calamari + "/auth/login", calamariUsername, calamariPassword);
		
		if (uri.startsWith("http")) {
			response = peacockRestTemplate.submit(uri, body, method, acceptableMediaTypes, contentType);
		} else {
			response = peacockRestTemplate.submit(calamari + uri, body, method, acceptableMediaTypes, contentType);
		}

		if (response != null && (response.trim().startsWith(JSON_PREFIX1) || response.trim().startsWith(JSON_PREFIX2))) {
			return readTree(response);
		} else {
			return response;
		}
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
	public Object radosgwSubmit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		String response = null;
		
		if (uri.startsWith("http")) {
			response = peacockRestTemplate.submit(uri, body, method, acceptableMediaTypes, contentType);
		} else {
			response = peacockRestTemplate.submit(radosgw + uri, body, method, acceptableMediaTypes, contentType);
		}

		if (response != null && (response.trim().startsWith(JSON_PREFIX1) || response.trim().startsWith(JSON_PREFIX2))) {
			return readTree(response);
		} else {
			return response;
		}
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
	public Object sshCopyId(String host, String username, String passwd) throws IOException {
		String command = "sshpass -p " + passwd + " ssh-copy-id -o StrictHostKeyChecking=no " + username + "@" + host;
		return execute(command);
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
	public Object execute(String command) throws IOException {
		String response = SshExecUtil.executeCommand(getTargetHost(), command);

		if (response != null && (response.trim().startsWith(JSON_PREFIX1) || response.trim().startsWith(JSON_PREFIX2))) {
			return readTree(response);
		} else {
			return response;
		}
	}
	//end of execute()
	
	/**
	 * <pre>
	 * Create an Json Array Node
	 * </pre>
	 * @return
	 */
	public ArrayNode createArrayNode() {
		return MAPPER.createArrayNode();
	}
	//end of createArrayNode()
	
	/**
	 * <pre>
	 * Create an Json Object Node
	 * </pre>
	 * @return
	 */
	public ObjectNode createObjectNode() {
		return MAPPER.createObjectNode();
	}
	//end of createObjectNode()
	
	/**
	 * <pre>
	 * get TargetHost for ssh connection
	 * </pre>
	 * @return
	 */
	private TargetHost getTargetHost() {
		TargetHost targetHost = new TargetHost();
		targetHost.setHost(sshHost);
		targetHost.setPort(sshIp);
		targetHost.setUsername(sshUsername);
		targetHost.setPassword(sshPassword);
		
		return targetHost;
	}
	//end of getTargetHost()

	/**
	 * <pre>
	 * Method to deserialize JSON content as tree expressed using set of JsonNode instances. 
	 * </pre>
	 * @param json JSON content
	 * @return
	 */
	private JsonNode readTree(String json){
		try {
			return MAPPER.readTree(json);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	//end of readTree()
}
//end of CephService.java