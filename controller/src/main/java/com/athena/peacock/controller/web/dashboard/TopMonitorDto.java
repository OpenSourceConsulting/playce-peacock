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
package com.athena.peacock.controller.web.dashboard;


/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class TopMonitorDto {

	private String instanceName;
	private int used;
	private int free;
	
	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}
	/**
	 * @param instanceName the instanceName to set
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	/**
	 * @return the used
	 */
	public int getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(int used) {
		this.used = used;
	}
	/**
	 * @return the free
	 */
	public int getFree() {
		return free;
	}
	/**
	 * @param free the free to set
	 */
	public void setFree(int free) {
		this.free = free;
	}
}
//end of TopMonitorDto.java