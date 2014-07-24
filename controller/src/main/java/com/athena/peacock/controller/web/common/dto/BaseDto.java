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
package com.athena.peacock.controller.web.common.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer regUserId;
	private Date regDt;
	private Integer updUserId;
	private Date updDt;
	
	/** 페이징 관련 */
	private int start;
	private int limit = 10;
	
	/**
	 * @return the regUserId
	 */
	public Integer getRegUserId() {
		return regUserId;
	}
	
	/**
	 * @param regUserId the regUserId to set
	 */
	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}
	
	/**
	 * @return the regDt
	 */
	public Date getRegDt() {
		return regDt;
	}
	
	/**
	 * @param regDt the regDt to set
	 */
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	
	/**
	 * @return the updUserId
	 */
	public Integer getUpdUserId() {
		return updUserId;
	}
	
	/**
	 * @param updUserId the updUserId to set
	 */
	public void setUpdUserId(Integer updUserId) {
		this.updUserId = updUserId;
	}
	
	/**
	 * @return the updDt
	 */
	public Date getUpdDt() {
		return updDt;
	}
	
	/**
	 * @param updDt the updDt to set
	 */
	public void setUpdDt(Date updDt) {
		this.updDt = updDt;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
//end of BaseDto.java