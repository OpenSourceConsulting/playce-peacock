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
 * Sang-cheon Park	2015. 10. 13.		First Draft.
 */
package com.athena.peacock.controller.web.ceph.base;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class CephDto extends BaseDto {

	private static final long serialVersionUID = -6765693525752120066L;
	
	private int cephId;
	private int machineId;
	private String mgmtHost;
	private String mgmtPort;
	private String mgmtUsername;
	private String mgmtPassword;
	private String mgmtApiPrefix;
	private String radosgwApiPrefix;
	private String calamariApiPrefix;
	private String calamariUsername;
	private String calamariPassword;
	private String s3AccessKey;
	private String s3SecretKey;
	
	/**
	 * @return the cephId
	 */
	public int getCephId() {
		return cephId;
	}
	/**
	 * @param cephId the cephId to set
	 */
	public void setCephId(int cephId) {
		this.cephId = cephId;
	}
	/**
	 * @return the machineId
	 */
	public int getMachineId() {
		return machineId;
	}
	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}
	/**
	 * @return the mgmtHost
	 */
	public String getMgmtHost() {
		return mgmtHost;
	}
	/**
	 * @param mgmtHost the mgmtHost to set
	 */
	public void setMgmtHost(String mgmtHost) {
		this.mgmtHost = mgmtHost;
	}
	/**
	 * @return the mgmtPort
	 */
	public String getMgmtPort() {
		return mgmtPort;
	}
	/**
	 * @param mgmtPort the mgmtPort to set
	 */
	public void setMgmtPort(String mgmtPort) {
		this.mgmtPort = mgmtPort;
	}
	/**
	 * @return the mgmtUsername
	 */
	public String getMgmtUsername() {
		return mgmtUsername;
	}
	/**
	 * @param mgmtUsername the mgmtUsername to set
	 */
	public void setMgmtUsername(String mgmtUsername) {
		this.mgmtUsername = mgmtUsername;
	}
	/**
	 * @return the mgmtPassword
	 */
	public String getMgmtPassword() {
		return mgmtPassword;
	}
	/**
	 * @param mgmtPassword the mgmtPassword to set
	 */
	public void setMgmtPassword(String mgmtPassword) {
		this.mgmtPassword = mgmtPassword;
	}
	/**
	 * @return the mgmtApiPrefix
	 */
	public String getMgmtApiPrefix() {
		return mgmtApiPrefix;
	}
	/**
	 * @param mgmtApiPrefix the mgmtApiPrefix to set
	 */
	public void setMgmtApiPrefix(String mgmtApiPrefix) {
		this.mgmtApiPrefix = mgmtApiPrefix;
	}
	/**
	 * @return the radosgwApiPrefix
	 */
	public String getRadosgwApiPrefix() {
		return radosgwApiPrefix;
	}
	/**
	 * @param radosgwApiPrefix the radosgwApiPrefix to set
	 */
	public void setRadosgwApiPrefix(String radosgwApiPrefix) {
		this.radosgwApiPrefix = radosgwApiPrefix;
	}
	/**
	 * @return the calamariApiPrefix
	 */
	public String getCalamariApiPrefix() {
		return calamariApiPrefix;
	}
	/**
	 * @param calamariApiPrefix the calamariApiPrefix to set
	 */
	public void setCalamariApiPrefix(String calamariApiPrefix) {
		this.calamariApiPrefix = calamariApiPrefix;
	}
	/**
	 * @return the calamariUsername
	 */
	public String getCalamariUsername() {
		return calamariUsername;
	}
	/**
	 * @param calamariUsername the calamariUsername to set
	 */
	public void setCalamariUsername(String calamariUsername) {
		this.calamariUsername = calamariUsername;
	}
	/**
	 * @return the calamariPassword
	 */
	public String getCalamariPassword() {
		return calamariPassword;
	}
	/**
	 * @param calamariPassword the calamariPassword to set
	 */
	public void setCalamariPassword(String calamariPassword) {
		this.calamariPassword = calamariPassword;
	}
	/**
	 * @return the s3AccessKey
	 */
	public String getS3AccessKey() {
		return s3AccessKey;
	}
	/**
	 * @param s3AccessKey the s3AccessKey to set
	 */
	public void setS3AccessKey(String s3AccessKey) {
		this.s3AccessKey = s3AccessKey;
	}
	/**
	 * @return the s3SecretKey
	 */
	public String getS3SecretKey() {
		return s3SecretKey;
	}
	/**
	 * @param s3SecretKey the s3SecretKey to set
	 */
	public void setS3SecretKey(String s3SecretKey) {
		this.s3SecretKey = s3SecretKey;
	}
}
//end of CephDto.java