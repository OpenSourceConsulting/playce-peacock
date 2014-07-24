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
 * Sang-cheon Park	2013. 10. 21.		First Draft.
 */
package com.athena.peacock.controller.common.provisioning;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ProvisioningDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Controller에서 각 Software의 config 파일을 읽기 위한 url prefix */
	private String urlPrefix;

	/** Apache, Tomcat, JBoss, MySQL 공통 Variables */
	private Integer softwareId;
	private String softwareName;
	private String version;
	private String machineId;
	private String serverName;
	private String port;
	private String password;
	private String autoStart = "Y";
	
	/** Apache Variables */
	private String targetDir;
	private String serverRoot;
	private String serverDomain;
	
	/** Tomcat Variables */
	private String javaHome;
	private String catalinaHome;
	private String catalinaBase;
	private String portOffset;
	private String compUser;
	
	/** JBoss Variables */
	private String jbossHome;
	private String serverHome;
	private String partitionName;
	private String bindAddress;
	private String bindPort;
	private String databaseType;
	private String jndiName;
	private String connectionUrl;
	private String userName;
	private String minPoolSize;
	private String maxPoolSize;
	
	/** MySQL Variables */
	private String dataDir;
	
	private Integer userId;

	/**
	 * @return the urlPrefix
	 */
	public String getUrlPrefix() {
		return urlPrefix;
	}

	/**
	 * @param urlPrefix the urlPrefix to set
	 */
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	/**
	 * @return the softwareId
	 */
	public Integer getSoftwareId() {
		return softwareId;
	}

	/**
	 * @param softwareId the softwareId to set
	 */
	public void setSoftwareId(Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * @return the softwareName
	 */
	public String getSoftwareName() {
		return softwareName;
	}

	/**
	 * @param softwareName the softwareName to set
	 */
	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the machineId
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the autoStart
	 */
	public String getAutoStart() {
		return autoStart;
	}

	/**
	 * @param autoStart the autoStart to set
	 */
	public void setAutoStart(String autoStart) {
		this.autoStart = autoStart;
	}

	/**
	 * @return the targetDir
	 */
	public String getTargetDir() {
		return targetDir;
	}

	/**
	 * @param targetDir the targetDir to set
	 */
	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	/**
	 * @return the serverRoot
	 */
	public String getServerRoot() {
		return serverRoot;
	}

	/**
	 * @param serverRoot the serverRoot to set
	 */
	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}

	/**
	 * @return the serverDomain
	 */
	public String getServerDomain() {
		return serverDomain;
	}

	/**
	 * @param serverDomain the serverDomain to set
	 */
	public void setServerDomain(String serverDomain) {
		this.serverDomain = serverDomain;
	}

	/**
	 * @return the javaHome
	 */
	public String getJavaHome() {
		return javaHome;
	}

	/**
	 * @param javaHome the javaHome to set
	 */
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	/**
	 * @return the catalinaHome
	 */
	public String getCatalinaHome() {
		return catalinaHome;
	}

	/**
	 * @param catalinaHome the catalinaHome to set
	 */
	public void setCatalinaHome(String catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	/**
	 * @return the catalinaBase
	 */
	public String getCatalinaBase() {
		return catalinaBase;
	}

	/**
	 * @param catalinaBase the catalinaBase to set
	 */
	public void setCatalinaBase(String catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	/**
	 * @return the portOffset
	 */
	public String getPortOffset() {
		return portOffset;
	}

	/**
	 * @param portOffset the portOffset to set
	 */
	public void setPortOffset(String portOffset) {
		this.portOffset = portOffset;
	}

	/**
	 * @return the compUser
	 */
	public String getCompUser() {
		return compUser;
	}

	/**
	 * @param compUser the compUser to set
	 */
	public void setCompUser(String compUser) {
		this.compUser = compUser;
	}

	/**
	 * @return the jbossHome
	 */
	public String getJbossHome() {
		return jbossHome;
	}

	/**
	 * @param jbossHome the jbossHome to set
	 */
	public void setJbossHome(String jbossHome) {
		this.jbossHome = jbossHome;
	}

	/**
	 * @return the serverHome
	 */
	public String getServerHome() {
		return serverHome;
	}

	/**
	 * @param serverHome the serverHome to set
	 */
	public void setServerHome(String serverHome) {
		this.serverHome = serverHome;
	}

	/**
	 * @return the partitionName
	 */
	public String getPartitionName() {
		return partitionName;
	}

	/**
	 * @param partitionName the partitionName to set
	 */
	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}

	/**
	 * @return the bindAddress
	 */
	public String getBindAddress() {
		return bindAddress;
	}

	/**
	 * @param bindAddress the bindAddress to set
	 */
	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}

	/**
	 * @return the bindPort
	 */
	public String getBindPort() {
		return bindPort;
	}

	/**
	 * @param bindPort the bindPort to set
	 */
	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}

	/**
	 * @return the databaseType
	 */
	public String getDatabaseType() {
		return databaseType;
	}

	/**
	 * @param databaseType the databaseType to set
	 */
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * @return the jndiName
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @param jndiName the jndiName to set
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * @return the connectionUrl
	 */
	public String getConnectionUrl() {
		return connectionUrl;
	}

	/**
	 * @param connectionUrl the connectionUrl to set
	 */
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the minPoolSize
	 */
	public String getMinPoolSize() {
		return minPoolSize;
	}

	/**
	 * @param minPoolSize the minPoolSize to set
	 */
	public void setMinPoolSize(String minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	/**
	 * @return the maxPoolSize
	 */
	public String getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 */
	public void setMaxPoolSize(String maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the dataDir
	 */
	public String getDataDir() {
		return dataDir;
	}

	/**
	 * @param dataDir the dataDir to set
	 */
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
//end of ProvisioningDetail.java