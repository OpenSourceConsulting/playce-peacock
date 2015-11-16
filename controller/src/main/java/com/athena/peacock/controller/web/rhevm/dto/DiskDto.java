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
 * Ji-Woong Choi	2013. 10. 23.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm.dto;

public class DiskDto extends RHEVBaseDto {

	private String active;
	private String virtualSize;
	private String actualSize;
	private String bootable;
	private String sharable;
	private String _interface;
	private String status;
	
	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
	/**
	 * @return the virtualSize
	 */
	public String getVirtualSize() {
		return virtualSize;
	}
	/**
	 * @param virtualSize the virtualSize to set
	 */
	public void setVirtualSize(String virtualSize) {
		this.virtualSize = virtualSize;
	}
	/**
	 * @return the actualSize
	 */
	public String getActualSize() {
		return actualSize;
	}
	/**
	 * @param actualSize the actualSize to set
	 */
	public void setActualSize(String actualSize) {
		this.actualSize = actualSize;
	}
	/**
	 * @return the bootable
	 */
	public String getBootable() {
		return bootable;
	}
	/**
	 * @param bootable the bootable to set
	 */
	public void setBootable(String bootable) {
		this.bootable = bootable;
	}
	/**
	 * @return the sharable
	 */
	public String getSharable() {
		return sharable;
	}
	/**
	 * @param sharable the sharable to set
	 */
	public void setSharable(String sharable) {
		this.sharable = sharable;
	}
	/**
	 * @return the _interface
	 */
	public String getInterface() {
		return _interface;
	}
	/**
	 * @param _interface the _interface to set
	 */
	public void setInterface(String _interface) {
		this._interface = _interface;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
