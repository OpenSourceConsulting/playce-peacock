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

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.common.core.action.support.TargetHost;
import com.athena.peacock.common.core.util.SshExecUtil;
import com.athena.peacock.common.rest.PeacockRestTemplate;

/**
 * <pre>
 * Base controller for ceph control
 *  - each ceph controller should be extends CephBaseController
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public abstract class CephBaseController {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String JSON_PREFIX1 = "{";
	private static final String JSON_PREFIX2 = "[";
	
    @Value("#{contextProperties['ceph.rest.api.prefix']}")
    private String prefix;
    
    @Value("#{contextProperties['ceph.ssh.host']}")
    private String host;
    
    @Value("#{contextProperties['ceph.ssh.ip']}")
    private int ip;
    
    @Value("#{contextProperties['ceph.ssh.username']}")
    private String username;
    
    @Value("#{contextProperties['ceph.ssh.password']}")
    private String password;

	@Inject
	@Named("peacockRestTemplate")
	protected PeacockRestTemplate peacockRestTemplate;
	
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
	protected Object submit(String uri, HttpMethod method) throws RestClientException, Exception {
		return submit(uri, null, method);
	}
	//end of submit()
	
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
	protected Object submit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		String response = null;
		
		if (uri.startsWith("http")) {
			response = peacockRestTemplate.submit(uri, body, method);
		} else {
			response = peacockRestTemplate.submit(prefix + uri, body, method);
		}
		
		if (response.trim().startsWith(JSON_PREFIX1) || response.trim().startsWith(JSON_PREFIX2)) {
			return readTree(response);
		} else {
			return response;
		}
	}
	//end of submit()
	
	/**
	 * <pre>
	 * execute ssh command
	 * </pre>
	 * @param command
	 * @return
	 * @throws IOException
	 */
	protected Object execute(String command) throws IOException {
		String response = SshExecUtil.executeCommand(getTargetHost(), command);
		
		if (response.trim().startsWith(JSON_PREFIX1) || response.trim().startsWith(JSON_PREFIX2)) {
			return readTree(response);
		} else {
			return response;
		}
	}
	//end of execute()
	
	/**
	 * <pre>
	 * get TargetHost for ssh connection
	 * </pre>
	 * @return
	 */
	private TargetHost getTargetHost() {
		TargetHost targetHost = new TargetHost();
		targetHost.setHost(host);
		targetHost.setPort(ip);
		targetHost.setUsername(username);
		targetHost.setPassword(password);
		
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
//end of CephBaseController.java