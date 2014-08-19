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
 * Bong-Jin Kwon	2013. 9. 25.		First Draft.
 */
package com.athena.peacock.controller.web.alm.confluence;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.confluence.dto.SpaceSummaryDto;
import com.athena.peacock.controller.web.alm.user.dto.AlmUserDto;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class AlmConfluenceService {

	private static final String JIRA_URI = "http://119.81.162.218:8090";
	private static final String RPC_PATH = "/rpc/xmlrpc";
	private static final String USER_NAME = "admin";
	private static final String PASSWORD = "wjdtn#1212";

	private XmlRpcClient rpcClient;
	private XmlRpcClientConfigImpl config;

	public AlmConfluenceService() throws MalformedURLException {
		// TODO Auto-generated constructor stub

		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(JIRA_URI + RPC_PATH));
		rpcClient = new XmlRpcClient();
		rpcClient.setConfig(config);

	}

	public void insertUser(AlmUserDto user) {

	}

	public GridJsonResponse getList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();
		
		try {
			response.setList(getCrowdSpaceList());
		} catch (XmlRpcException e) {
			response.setSuccess(false);
			response.setMsg("XmlRpcException");
		}
		return response;
	}

	private List<SpaceSummaryDto> getCrowdSpaceList() throws XmlRpcException {

		List<SpaceSummaryDto> list = new ArrayList<SpaceSummaryDto>();

		Vector<String> loginTokenVector = new Vector<String>(1);
		loginTokenVector.add(getLoginToken());
		Object[] spaces = (Object[]) rpcClient.execute("confluence2.getSpaces",
				loginTokenVector);

		// Print projects
		for (int i = 0; i < spaces.length; i++) {
			Map space = (Map) spaces[i];
			SpaceSummaryDto tmp = new SpaceSummaryDto();
			tmp.setKey((String) space.get("key"));
			tmp.setName((String) space.get("name"));
			tmp.setType((String) space.get("type"));
			list.add(tmp);
		}
		
		return list;
	}

	private String getLoginToken() throws XmlRpcException {

		Vector<String> loginParams = new Vector<String>(2);
		loginParams.add(USER_NAME);
		loginParams.add(PASSWORD);
		String loginToken = (String) rpcClient.execute("confluence2.login",
				loginParams);

		return loginToken;
	}

}
// end of UserService.java