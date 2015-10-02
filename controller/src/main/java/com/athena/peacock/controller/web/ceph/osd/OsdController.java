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
 */
package com.athena.peacock.controller.web.ceph.osd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
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
 * @author Hojin kim
 * @version 1.1

 */
@Controller
@RequestMapping("/ceph/osd")
public class OsdController extends CephBaseController   {

	private static final Logger LOGGER = LoggerFactory.getLogger(OsdController.class);
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
	public @ResponseBody SimpleJsonResponse getDump(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/dump", HttpMethod.GET);
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
	@RequestMapping("/tree")
	public @ResponseBody SimpleJsonResponse getOSDTree(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/tree", HttpMethod.GET);
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
	@RequestMapping("/blacklist/ls")
	public @ResponseBody SimpleJsonResponse getOSDBlacklist(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/blacklist/ls", HttpMethod.GET);
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
	@RequestMapping("/crush/dump")
	public @ResponseBody SimpleJsonResponse getOSDCrushDump(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/crush/dump", HttpMethod.GET);
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
	@RequestMapping("/crush/rule/dump")
	public @ResponseBody SimpleJsonResponse getOSDCrushRuleDump(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/crush/rule/dump", HttpMethod.GET);
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
	@RequestMapping("/crush/rule/list")
	public @ResponseBody SimpleJsonResponse getOSDCrushRulelist(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/crush/rule/list", HttpMethod.GET);
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
	@RequestMapping("/map")
	public @ResponseBody SimpleJsonResponse getOSDDumpepoch(SimpleJsonResponse jsonRes, @QueryParam("epoch") String epoch) throws Exception {
		try {
			Object response = managementSubmit("/osd/dump?epoch=" + epoch, HttpMethod.GET);
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
	@RequestMapping("/find")
	public @ResponseBody SimpleJsonResponse getOSDCrushMapFind(SimpleJsonResponse jsonRes, @QueryParam("id") String id) throws Exception {
		try {
			Object response = managementSubmit("/osd/find?id=" + id, HttpMethod.GET);
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
	@RequestMapping("/getcrushmap")
	public @ResponseBody SimpleJsonResponse getOSDCrushMap(SimpleJsonResponse jsonRes, @QueryParam("epoch") String epoch) throws Exception {
		try {
			Object response = managementSubmit("/osd/getcrushmap?epoch=" + epoch, HttpMethod.GET);
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
	@RequestMapping("/getmap")
	public @ResponseBody SimpleJsonResponse getOSDMap(SimpleJsonResponse jsonRes, @QueryParam("epoch") String epoch) throws Exception {
		try {
			Object response = managementSubmit("/osd/getmap?epoch=" + epoch, HttpMethod.GET);
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
	@RequestMapping("/perf")
	public @ResponseBody SimpleJsonResponse getOSDPerf(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/perf", HttpMethod.GET);
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
	@RequestMapping("/stat")
	public @ResponseBody SimpleJsonResponse getOSDstat(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/stat", HttpMethod.GET);
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

	@RequestMapping("/create/test1")
	public @ResponseBody
	SimpleJsonResponse createPool(SimpleJsonResponse jsonRes, @QueryParam("name") String name) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/stats?name=" + name, HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Pool List가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Pool List 조회 중 에러가 발생하였습니다.");

			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}

		return jsonRes;
	}
	@RequestMapping("/diskava")
	public @ResponseBody SimpleJsonResponse getOsddiskava(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = execute("/usr/bin/diff /root/partitions.txt /root/partitions2.txt | awk {'print $5'}");
			
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("fdisk가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("fdisk 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
}
// end of OsdController.java
