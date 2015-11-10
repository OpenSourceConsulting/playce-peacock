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
package com.athena.peacock.controller.job;


import com.athena.peacock.common.scheduler.InternalJobExecutionException;
import com.athena.peacock.common.scheduler.quartz.BaseJob;
import com.athena.peacock.common.scheduler.quartz.JobExecution;
//import com.athena.peacock.controller.web.alm.project.AlmProjectProcess;

/**
 * <pre>
 * ALM에 생성 요청된 프로세스를 처리하기 위한  Quartz Job
 * </pre>
 * 
 * @author Dave
 * @version 1.0
 */
public class AlmProcessJob extends BaseJob {

//	private AlmProjectProcess almProjectService;

	// TODO
	// Instance Grid의 정보 RHEV Manager 상의 정보를 주기적으로 동기화.

	@Override
	protected void executeInternal(JobExecution context)
			throws InternalJobExecutionException {

		try {
			LOGGER.debug("Alm Job Processing...");

//			if (almProjectService == null) {
//				almProjectService = AppContext.getBean(AlmProjectProcess.class);
//			}
//
//			almProjectService.processProjectMapping();

		} catch (Exception e) {
			LOGGER.error("Unhandled exception has occurred.", e);
			throw new InternalJobExecutionException(e);
		}
	}
}
// end of DashboardInfoCollectJob.java