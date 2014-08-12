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
 * Sang-cheon Park	2014. 8. 12.		First Draft.
 */
package com.athena.peacock.controller.common.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.athena.peacock.controller.web.hypervisor.HypervisorDto;

/**
 * <pre>
 * Multi RHEV-M 사용을 위해 각 RHEV-M에 해당하는 RHEVRestTemplate을 관리한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class RHEVMRestTemplateManager {

	private static Map<Integer, RHEVMRestTemplate> templates = new ConcurrentHashMap<Integer, RHEVMRestTemplate>();
	
	/**
	 * <pre>
	 * 주어진 hypervisorId에 해당하는 RHEVMRestTemplate 객체를 가져온다.
	 * </pre>
	 * @param hypervisorId
	 * @return
	 */
	public static RHEVMRestTemplate getRHEVMRestTemplate(int hypervisorId) {
		return templates.get(hypervisorId);
	}//end of getRHEVMRestTemplate()
	
	/**
	 * <pre>
	 * 주어진 정보를 이용하여 RHEVMRestTemplate 객체를 생성하고 Map에 저장한다.
	 * </pre>
	 * @param hypervisor
	 */
	public synchronized static void setRHEVMRestTemplate(HypervisorDto hypervisor) {
		RHEVMRestTemplate template = new RHEVMRestTemplate(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
				hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
		
		templates.put(hypervisor.getHypervisorId(), template);
	}//end of setRHEVMRestTemplate()
	
	/**
	 * <pre>
	 * 주어진 hypervisorId에 해당하는 RHEVMRestTemplate 객체를 제거한다.
	 * </pre>
	 * @param hypervisorId
	 */
	public synchronized static void removeRHEVMRestTemplate(int hypervisorId) {
		templates.remove(hypervisorId);
	}
}
//end of RHEVRestTemplateManager.java