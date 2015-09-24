/* 
 * Athena Peacock - Auto Provisioning
 * 
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
 * khoj			2015. 9. 24.		First Draft.
 */
package com.athena.peacock.controller.web.ceph;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.common.core.action.support.TargetHost;
import com.athena.peacock.common.core.util.SshExecUtil;
import com.athena.peacock.common.rest.PeacockRestTemplate;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.test.TestController;
/**
 * <pre>
 * 
 * </pre>
 * @author khoj
 * @version 1.0
 */
@Controller
@RequestMapping("/khoj")

public class TestControllerKhoj {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Inject
	@Named("peacockRestTemplate")
	private PeacockRestTemplate peacockRestTemplate;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/osd/tree")
	public @ResponseBody SimpleJsonResponse getStatus(SimpleJsonResponse jsonRes) throws Exception {
		try {
			String response = peacockRestTemplate.submit("http://192.168.0.227:5000/api/v0.1/osd/tree", HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(readTree(response));
			jsonRes.setMsg("status가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("status 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/osdtree")
	public @ResponseBody SimpleJsonResponse getMonStatus(SimpleJsonResponse jsonRes) throws Exception {
		try {
			TargetHost targetHost = new TargetHost();
			targetHost.setHost("192.168.0.227");
			targetHost.setPort(22);
			targetHost.setUsername("root");
			targetHost.setPassword("jan01jan");
			
			String response = SshExecUtil.executeCommand(targetHost, "ceph osd tree");
			
			jsonRes.setSuccess(true);
			//jsonRes.setData(readTree(response));
			jsonRes.setData(response);
			jsonRes.setMsg("mon_status가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("mon_status 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
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
}
//end of TestControllerKhoj.java