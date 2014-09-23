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
 * Sang-cheon Park	2014. 9. 19.		First Draft.
 */
package com.athena.peacock.controller.common.core.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.controller.common.core.InitializingTask;
import com.athena.peacock.controller.web.dashboard.DashboardService;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("dashboardHandler")
public class DashboardHandler implements InitializingTask {

    protected final Logger logger = LoggerFactory.getLogger(DashboardHandler.class);
	
    @Inject
    @Named("dashboardService")
    private DashboardService dashboardService;

	@Override
	public void init() {
		try {
			if (!dashboardService.getStatus().equals("GATHERING")) {
				// Dashboard 데이터 수집 여부시 까지 Block되지 않고 서버 구동을 완료시키기 위해 Thread로 수집한다.
				new Thread() {
					public void run() {
						try {
							dashboardService.refreshDashboardInfo();
						} catch (Exception e) {
							logger.error("can not initiate dashboard info : ", e);
						}
					}
				}.start();
			}
		} catch (Exception e) {
			logger.error("can not initiate dashboard info : ", e);
		}
	}
}
//end of DashboardHandler.java