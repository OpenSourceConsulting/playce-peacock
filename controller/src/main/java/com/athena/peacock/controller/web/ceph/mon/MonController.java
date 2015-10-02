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
 * Sang-cheon Park	2015. 9. 24.		First Draft.
 * Ik-Han Kim 		2015. 9. 30.        Add monitoring functions
 */
package com.athena.peacock.controller.web.ceph.mon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.ceph.CephBaseController;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("/ceph/mon")
public class MonController extends CephBaseController  {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonController.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/status2")
	public @ResponseBody SimpleJsonResponse getStatus1(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/status", HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
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
	@RequestMapping("/dump")
	public @ResponseBody SimpleJsonResponse getStatus2(SimpleJsonResponse jsonRes, @QueryParam("epoch") String epoch) throws Exception {
		try {
			Object response = managementSubmit("/mon/dump?epoch=" + epoch, HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
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
	@RequestMapping("/stat2")
	public @ResponseBody SimpleJsonResponse getStat(SimpleJsonResponse jsonRes) throws Exception {
		try {
			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.ALL);
			Object response = managementSubmit("/mon/stat", null, HttpMethod.GET, acceptableMediaTypes, MediaType.APPLICATION_JSON);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
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
	@RequestMapping("/cluster/{fsid}/{path}")
	public @ResponseBody SimpleJsonResponse subCluster(SimpleJsonResponse jsonRes, @PathVariable("fsid") String fsid, @PathVariable("path") String path) throws Exception {
		try {
			Object response = null;
			Map<String, Object> params = null;
			
			if (path.equals("cli")) {
				List<String> command = new ArrayList<String>();
				command.add("osd");
				command.add("dump");

				params = new HashMap<String, Object>();
				params.put("command", "osd list");
			}
			
			if (params != null) {
				response = calamariSubmit("/cluster/" + fsid + "/" + path, params, HttpMethod.POST);
			} else {
				response = calamariSubmit("/cluster/" + fsid + "/" + path, HttpMethod.GET);
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("조회 중 에러가 발생하였습니다.");
			
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
	@RequestMapping("/monstatus2")
	public @ResponseBody SimpleJsonResponse getMonStatus1(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = execute("ceph mon_status");
			
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("mon_status가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("mon_status 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
}
//end of MonController.java