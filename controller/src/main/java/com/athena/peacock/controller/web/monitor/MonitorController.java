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
 * Sang-cheon Park	2013. 10. 18.		First Draft.
 */
package com.athena.peacock.controller.web.monitor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.common.core.handler.MonFactorHandler;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller("monitorController")
@RequestMapping("/monitor")
public class MonitorController {
	
	@Inject
	@Named("monitorService")
	private MonitorService monitorService;
	
	@Inject
	@Named("monFactorHandler")
	private MonFactorHandler monFactorHandler;
	
	@RequestMapping("/list")
	public @ResponseBody List<MonDataDto> list(MonDataDto monData) throws Exception {
		Assert.notNull(monData.getMachineId(), "machineId can not be null.");
		Assert.notNull(monData.getMonFactorId(), "monFactorId can not be null.");
		
		List<MonDataDto> monDataList = monitorService.getMonDataList(monData);
		
		return monDataList;
	}
	
	@RequestMapping("/factor_list")
	public @ResponseBody GridJsonResponse factorList(GridJsonResponse jsonRes, String includeAll) throws Exception {
		
		// Get all list without paging
		List<MonFactorDto> monFactorList = monFactorHandler.getMonFactorList();
		List<MonFactorDto> factorList = new ArrayList<MonFactorDto>();
		
		if ("true".equals(includeAll) || "Y".equals(includeAll) || "y".equals(includeAll)) {
			factorList = monFactorList;
		} else {
			for (MonFactorDto monFactor : monFactorList) {
				if (StringUtils.isNotEmpty(monFactor.getAutoScalingYn()) && "Y".equals(monFactor.getAutoScalingYn())) {
					factorList.add(monFactor);
				}
			}
		}
		
		jsonRes.setTotal(factorList.size());
		jsonRes.setList(factorList);
		
		return jsonRes;
	}
}
//end of MonitorController.java