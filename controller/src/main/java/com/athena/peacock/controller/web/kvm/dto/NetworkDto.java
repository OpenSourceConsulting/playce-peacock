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
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class NetworkDto extends KVMBaseDto {

	private String ifType;
	private String macAddress;
	private String sourceBridge;
	private String targetDev;
	private String modelType;
	private String aliasName;
	private String addressType;
	
	public String getIfType() {
		return ifType;
	}
	public void setIfType(String ifType) {
		this.ifType = ifType;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getSourceBridge() {
		return sourceBridge;
	}
	public void setSourceBridge(String sourceBridge) {
		this.sourceBridge = sourceBridge;
	}
	public String getTargetDev() {
		return targetDev;
	}
	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
}
//end of NetworkDto.java
