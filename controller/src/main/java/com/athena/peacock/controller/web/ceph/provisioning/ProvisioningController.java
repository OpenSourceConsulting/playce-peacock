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
 * Ji-Woong Choi	2015. 9. 24.		First Draft.
 */
package com.athena.peacock.controller.web.ceph.provisioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/ceph/provisioning")

public class ProvisioningController extends CephBaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProvisioningController.class);
	
	@Autowired
	private ProvisioningService service;
			
	/**
	 * <pre>
	 * 초기 BareMetal 서버에 Ceph Management Server를 설치한다.
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/management")
	public @ResponseBody SimpleJsonResponse installManagement(SimpleJsonResponse jsonRes, ProvisioningConfig config) throws Exception {
		try {
			// Call install mangement in service
			service.installManagement(config);
			jsonRes.setSuccess(true);
			jsonRes.setData(config);
			jsonRes.setMsg("Ceph Management Server가 정상적으로 설치되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Ceph Management Server 설치 도중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		return jsonRes;
	}		
}
