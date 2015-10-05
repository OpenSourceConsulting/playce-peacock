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
package com.athena.peacock.controller.web.ceph.pg;

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
 * @author Ji-Woong Choi
 * @version 1.0
 */
@Controller
@RequestMapping("/ceph/pg")
public class PgController extends CephBaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PgController.class);

	/**
	 * <pre>
	 * 관리 대상 Ceph에 대한 Placement Group의 현재 상태에 대한 요약 정보를 제공한다. 
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stat")
	public @ResponseBody SimpleJsonResponse getStatus(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/pg/stat", HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement Group Status가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement Group Status조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group Map에 대한 각 콘텐츠를 조회할 수 있는 기능을 제공한다. 
	 * Dump 대상 항목으로는 all | summary | sum | delta | pools | osds | pgs | pgs_brief 의 파라미터를 전달하여 각 결과값을 사용한다.
	 * </pre>
	 * @param jsonRes
	 * @param dumpContents Dump Target
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dump/{dumpcontents}")
	public @ResponseBody SimpleJsonResponse getDump(SimpleJsonResponse jsonRes, @PathVariable("dumpcontents") String dumpContents) throws Exception {
		try {
			Object response = managementSubmit("/pg/dump?dumpcontents=" + dumpContents, HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement Group Map이 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement Group Map 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group ID의 버전을 조회한다.
	 * Report version of OSD 
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tell/{pgid}/version")
	public @ResponseBody SimpleJsonResponse getVersion(SimpleJsonResponse jsonRes, @PathVariable("pgid") String pgid) throws Exception {
		try {
			Object response = managementSubmit("/tell/" + pgid + "/version", HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg(pgid + "에 대한 Placement Group ID 버전이 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg(pgid + "에 대한 Placement Group ID 버전 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	
	/**
	 * <pre>
	 * 지정된 Placement Group에 대한 상세 정보를 제공한다.
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/tell/{pgid}/query")
	public @ResponseBody SimpleJsonResponse getPgDetail(SimpleJsonResponse jsonRes, @PathVariable("pgid") String pgid) throws Exception {
		try {
			Object response = managementSubmit("/tell/" + pgid + "/query", HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg(pgid + "에 대한 Placement Group 상세정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg(pgid + "에 대한 Placement Group 상세정보 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group의 OSDS 맵핑 정보를 추출한다.
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/map/{pgid}")
	public @ResponseBody SimpleJsonResponse getMap(SimpleJsonResponse jsonRes, @PathVariable("pgid") String pgid) throws Exception {
		try {
			Object response = managementSubmit("pg/map?pgid=" + pgid, HttpMethod.GET);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement Group ID에 대한 OSDS 맵핑정보가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement Group ID에 대한 OSDS 맵핑정보 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement gropu id에 해당하는 repair를 시작시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/repair/{pgid}")
	public @ResponseBody SimpleJsonResponse startRepair(SimpleJsonResponse jsonRes, @PathVariable("pgid") String pgid) throws Exception {
		try {
			Object response = managementSubmit("pg/repair?pgid=" + pgid, HttpMethod.PUT);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement gropu id에 해당하는 repair가 정상적으로 시작되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement gropu id에 해당하는 repair 시작 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement gropu id에 해당하는 scrub를 시작시킨다. 
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scrub/{pgid}")
	public @ResponseBody SimpleJsonResponse startScrub(SimpleJsonResponse jsonRes, @PathVariable("pgid") String pgid) throws Exception {
		try {
			Object response = managementSubmit("pg/scrub?pgid=" + pgid, HttpMethod.PUT);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement gropu id에 해당하는 scrub이 정상적으로 시작되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement gropu id에 해당하는 scrub 시작 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group 생성을 트리거한다. - Trigger pg creates to be issued
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/send_pg_creates")
	public @ResponseBody SimpleJsonResponse sendPgCreate(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("pg/send_pg_creates", HttpMethod.PUT);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement gropu id에 해당하는 scrub이 정상적으로 시작되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement gropu id에 해당하는 scrub 시작 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group이 가득찰 경우를 대비한 비율을 지정한다. 
	 * Set ratio at which pgs are considered full
	 * </pre>
	 * @param jsonRes
	 * @param ratio float[0..1]
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/set_full_ratio/{ratio}")
	public @ResponseBody SimpleJsonResponse setFullRatio(SimpleJsonResponse jsonRes, @PathVariable("ratio") String ratio) throws Exception {
		try {
			Object response = managementSubmit("pg/set_full_ratio?ratio=" + ratio, HttpMethod.PUT);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement group full 비율이 정상적으로 지정되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement group full 비율 적용 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group이 거의 가득찰 경우를 대비한 비율을 지정한다. 
	 * Set ratio at which pgs are considered full
	 * </pre>
	 * @param jsonRes
	 * @param ratio float[0..1]
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/set_nearfull_ratio/{ratio}")
	public @ResponseBody SimpleJsonResponse setNearFullRatio(SimpleJsonResponse jsonRes, @PathVariable("ratio") String ratio) throws Exception {
		try {
			Object response = managementSubmit("pg/set_nearfull_ratio?ratio=" + ratio, HttpMethod.PUT);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement group nearfull 비율이 정상적으로 지정되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement group nearfull 비율 적용 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Placement Group의 복구 통계 정보를 초기화한다.
	 * Reset pg recovery statistics
	 * </pre>
	 * @param jsonRes
	 * @param pgid Placement Group ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("tell/{pgid}/reset_pg_recovery_stats")
	public @ResponseBody SimpleJsonResponse resetPgRecoveryStats(SimpleJsonResponse jsonRes, @PathVariable("pgid") String pgid) throws Exception {
		try {
			Object response = managementSubmit("/tell/" + pgid + "/reset_pg_recovery_stats", HttpMethod.PUT);
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("Placement Group의 복구 통계 정보가 초기화되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Placement Group의 복구 통계 정보 초기화 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
}
//end of PgController.java