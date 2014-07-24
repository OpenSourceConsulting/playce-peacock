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
 * Sang-cheon Park	2013. 8. 25.		First Draft.
 */
package com.athena.peacock.controller.web.monitor;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class MonFactorDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private String monFactorId;
	private String monFactorName;
	private String monFactorUnit;
	private String monFactorDesc;
	private String autoScalingYn;
	private String displayName;
	
	/**
	 * @return the monFactorId
	 */
	public String getMonFactorId() {
		return monFactorId;
	}
	
	/**
	 * @param monFactorId the monFactorId to set
	 */
	public void setMonFactorId(String monFactorId) {
		this.monFactorId = monFactorId;
	}
	
	/**
	 * @return the monFactorName
	 */
	public String getMonFactorName() {
		return monFactorName;
	}
	
	/**
	 * @param monFactorName the monFactorName to set
	 */
	public void setMonFactorName(String monFactorName) {
		this.monFactorName = monFactorName;
	}

	/**
	 * @return the monFactorUnit
	 */
	public String getMonFactorUnit() {
		return monFactorUnit;
	}

	/**
	 * @param monFactorUnit the monFactorUnit to set
	 */
	public void setMonFactorUnit(String monFactorUnit) {
		this.monFactorUnit = monFactorUnit;
	}

	/**
	 * @return the monFactorDesc
	 */
	public String getMonFactorDesc() {
		return monFactorDesc;
	}

	/**
	 * @param monFactorDesc the monFactorDesc to set
	 */
	public void setMonFactorDesc(String monFactorDesc) {
		this.monFactorDesc = monFactorDesc;
	}

	/**
	 * @return the autoScalingYn
	 */
	public String getAutoScalingYn() {
		return autoScalingYn;
	}

	/**
	 * @param autoScalingYn the autoScalingYn to set
	 */
	public void setAutoScalingYn(String autoScalingYn) {
		this.autoScalingYn = autoScalingYn;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
//end of MonFactorDto.java