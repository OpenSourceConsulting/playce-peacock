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
 * Sang-cheon Park	2013. 11. 7.		First Draft.
 */
package com.athena.peacock.controller.job;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class AutoScalingStatus {
	
	private static Map<Integer, Integer> autoScalingMap = new ConcurrentHashMap<Integer, Integer>();
	private static Map<Integer, Long> autoScalingTimeMap = new ConcurrentHashMap<Integer, Long>();

	private AutoScalingStatus() {}
	
	public static void addStatus(int autoScalingId, int size) {
		autoScalingMap.put(autoScalingId, size);
		autoScalingTimeMap.put(autoScalingId, System.currentTimeMillis());
	}

	public static Integer getProcessCount(int autoScalingId) {
		return autoScalingMap.get(autoScalingId);
	}
	
	public static Long getStartTime(int autoScalingId) {
		return autoScalingTimeMap.get(autoScalingId);
	}
	
	public static void complete(int autoScalingId) {
		Integer size = autoScalingMap.get(autoScalingId);
		
		if (--size < 1) {
			removeStatus(autoScalingId);
		} else {
			autoScalingMap.put(autoScalingId, size);
		}
	}
	
	public static void removeStatus(int autoScalingId) {
		autoScalingMap.remove(autoScalingId);
		autoScalingTimeMap.remove(autoScalingId);
	}
}
//end of AutoScalingStatus.java