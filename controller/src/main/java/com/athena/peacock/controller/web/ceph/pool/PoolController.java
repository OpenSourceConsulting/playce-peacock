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

import javax.ws.rs.QueryParam;

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
	@RequestMapping("/stats")
	public @ResponseBody SimpleJsonResponse getPoolStats(SimpleJsonResponse jsonRes, @QueryParam("name") String name) throws Exception {
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

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/get/size")
	public @ResponseBody SimpleJsonResponse getPoolSize(SimpleJsonResponse jsonRes, @QueryParam("pool") String pool) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/get?pool=" + pool + "&var=size", HttpMethod.GET);
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
	@RequestMapping("/get/pg_num")
	public @ResponseBody SimpleJsonResponse getPoolPgNum(SimpleJsonResponse jsonRes, @QueryParam("pool") String pool) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/get?pool=" + pool + "&var=pg_num", HttpMethod.GET);
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
	@RequestMapping("/create")
	public @ResponseBody SimpleJsonResponse createPool(SimpleJsonResponse jsonRes, @QueryParam("pool") String pool, @QueryParam("pg_num") String pg_num) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/create?pool=" + pool + "&pg_num=" + pg_num, HttpMethod.PUT);
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
	@RequestMapping("/delete")
	public @ResponseBody SimpleJsonResponse deletePool(SimpleJsonResponse jsonRes, @QueryParam("pool") String pool) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/delete?pool=" + pool + "&pool2=" + pool + "&sure=--yes-i-really-really-mean-it", HttpMethod.PUT);
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
	@RequestMapping("/set/size")
	public @ResponseBody SimpleJsonResponse setPoolSize(SimpleJsonResponse jsonRes, @QueryParam("name") String name, @QueryParam("size") String size) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/set?pool=" + name + "&var=size&val=" + size, HttpMethod.PUT);
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
	@RequestMapping("/set/pg_num")
	public @ResponseBody SimpleJsonResponse setPoolPgNum(SimpleJsonResponse jsonRes, @QueryParam("name") String name, @QueryParam("pg_num") String pg_num) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/set?pool=" + name + "&var=pg_num&val=" + pg_num, HttpMethod.PUT);
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
	@RequestMapping("/rename")
	public @ResponseBody SimpleJsonResponse  renamePool(SimpleJsonResponse jsonRes, @QueryParam("name") String name, @QueryParam("srcpool") String srcpool, @QueryParam("destpool") String destpool) throws Exception {
		try {
			Object response = managementSubmit("/osd/pool/rename?srcpool=" + srcpool + "&destpool=" + destpool, HttpMethod.PUT);
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