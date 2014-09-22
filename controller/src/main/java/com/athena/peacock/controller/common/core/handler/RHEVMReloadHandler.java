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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.controller.common.core.InitializingTask;
import com.athena.peacock.controller.web.rhevm.RHEVMService;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("rhevmReloadHandler")
public class RHEVMReloadHandler implements InitializingTask {
	
    @Inject
    @Named("rhevmService")
    private RHEVMService rhevmService;

	@Override
	public void init() {
		//new ReloadThread(rhevmService).start();
	}
	
	class ReloadThread extends Thread {
		
		private RHEVMService rhevmService;
		
		public ReloadThread(RHEVMService rhevmService) {
			this.rhevmService = rhevmService;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					// 30분에 한번씩 rhev rest template을 초기화 한다.
					Thread.sleep(1000 * 30);
					rhevmService.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
//end of RHEVMReloadHandler.java