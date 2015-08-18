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
public class DiskDto extends KVMBaseDto {

	private String diskType;
	private String diskDevice;
	private String driverName;
	private String driverType;
	private String driverCache;
	private String sourceFile;
	private String targetDev;
	private String targetBus;
	private String aliasName;
	private String addressType;
	
	public String getDiskType() {
		return diskType;
	}
	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}
	public String getDiskDevice() {
		return diskDevice;
	}
	public void setDiskDevice(String diskDevice) {
		this.diskDevice = diskDevice;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverType() {
		return driverType;
	}
	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}
	public String getDriverCache() {
		return driverCache;
	}
	public void setDriverCache(String driverCache) {
		this.driverCache = driverCache;
	}
	public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public String getTargetDev() {
		return targetDev;
	}
	public void setTargetDev(String targetDev) {
		this.targetDev = targetDev;
	}
	public String getTargetBus() {
		return targetBus;
	}
	public void setTargetBus(String targetBus) {
		this.targetBus = targetBus;
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
//end of DiskDto.java
