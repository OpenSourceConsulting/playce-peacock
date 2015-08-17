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
 * Sang-cheon Park	2015. 8. 10.		First Draft.
 */
package com.athena.peacock.controller.web.kvm.dto;

/**
 * <pre>
 * KVM 기본 정보를 포함하는 상위 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class KVMBaseDto {
	
	private Integer hypervisorId;
	private String vmId;
	private String name;
	private String description;
	private String xmlDesc;
	
	public Integer getHypervisorId() {
		return hypervisorId;
	}
	public void setHypervisorId(Integer hypervisorId) {
		this.hypervisorId = hypervisorId;
	}
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getXmlDesc() {
		return xmlDesc;
	}
	public void setXmlDesc(String xmlDesc) {
		this.xmlDesc = xmlDesc;
	}
}
//end of KVMBaseDto.java