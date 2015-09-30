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
package com.athena.peacock.controller.web.ceph.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/ceph/pool")
public class PoolController extends CephBaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PoolController.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/lspools")
	public @ResponseBody SimpleJsonResponse listPools(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/lspools", HttpMethod.GET);
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
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/data")
	public @ResponseBody SimpleJsonResponse getPoolStats(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/stats?name='data'", HttpMethod.GET);
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

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/data/size")
	public @ResponseBody SimpleJsonResponse getPoolSize(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/get?pool='data'&var=size", HttpMethod.GET);
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

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/data/pg_num")
	public @ResponseBody SimpleJsonResponse getPoolPgNum(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/get?pool='data'&var=pg_num", HttpMethod.GET);
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
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/create/test1")
	public @ResponseBody SimpleJsonResponse createPool(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/create?pool='test1'&pg_num=64", HttpMethod.GET);
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
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/set/data/pg_num")
	public @ResponseBody SimpleJsonResponse setPoolPgNum(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/set?pool='data'&var=pg_num&val=128", HttpMethod.GET);
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
}
//end of PoolController.java