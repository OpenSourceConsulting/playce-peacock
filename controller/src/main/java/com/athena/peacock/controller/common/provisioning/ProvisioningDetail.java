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
	private String machineId;
	private Integer softwareId;
	private String softwareName;
	private String user;
	private String version;
	private String javaHome;
	private String serverHome;
	private String serverName;
	private String encoding;
	private String heapSize;
	private String permgenSize;
	private String bindAddress;
	private String hostName;
	private String databaseType1;
	private String databaseType2;
	private String jndiName1;
	private String jndiName2;
	private String connectionUrl1;
	private String connectionUrl2;
	private String userName1;
	private String userName2;
	private String password1;
	private String password2;
	private String autoStart = "Y";
	
	private String fileLocation;
	private String fileName;
	
	/** HTTPD Variables */
	private String group;
	private String apacheHome;
	private String httpPort;
	private String httpsPort;
	private String uriworkermap;
	private String workers;
	
	/** Tomcat Variables */
	private String catalinaHome;
	private String catalinaBase;
	private String portOffset;
	private String httpEnable = "Y";
	private String highAvailability = "Y";
	private String otherBindAddress;
	private String localIPAddress;
	private String maxIdle1;
	private String maxIdle2;
	private String maxActive1;
	private String maxActive2;
	
	/** JBoss Variables */
	private String jbossHome;
	private String baseTemplate;
	private String serverBase;
	private String domainIp;
	private String bindPort;
	private String udpGroupAddr;
	private String multicastPort;
	private String serverPeerID;
	private String jvmRoute;
	private String minPoolSize1;
	private String minPoolSize2;
	private String maxPoolSize1;
	private String maxPoolSize2;
	
	/** MySQL Variables */
	private String dataDir;
	private String port;
	
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
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
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
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the heapSize
	 */
	public String getHeapSize() {
		return heapSize;
	}

	/**
	 * @param heapSize the heapSize to set
	 */
	public void setHeapSize(String heapSize) {
		this.heapSize = heapSize;
	}

	/**
	 * @return the permgenSize
	 */
	public String getPermgenSize() {
		return permgenSize;
	}

	/**
	 * @param permgenSize the permgenSize to set
	 */
	public void setPermgenSize(String permgenSize) {
		this.permgenSize = permgenSize;
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
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the databaseType1
	 */
	public String getDatabaseType1() {
		return databaseType1;
	}

	/**
	 * @param databaseType1 the databaseType1 to set
	 */
	public void setDatabaseType1(String databaseType1) {
		this.databaseType1 = databaseType1;
	}

	/**
	 * @return the databaseType2
	 */
	public String getDatabaseType2() {
		return databaseType2;
	}

	/**
	 * @param databaseType2 the databaseType2 to set
	 */
	public void setDatabaseType2(String databaseType2) {
		this.databaseType2 = databaseType2;
	}

	/**
	 * @return the jndiName1
	 */
	public String getJndiName1() {
		return jndiName1;
	}

	/**
	 * @param jndiName1 the jndiName1 to set
	 */
	public void setJndiName1(String jndiName1) {
		this.jndiName1 = jndiName1;
	}

	/**
	 * @return the jndiName2
	 */
	public String getJndiName2() {
		return jndiName2;
	}

	/**
	 * @param jndiName2 the jndiName2 to set
	 */
	public void setJndiName2(String jndiName2) {
		this.jndiName2 = jndiName2;
	}

	/**
	 * @return the connectionUrl1
	 */
	public String getConnectionUrl1() {
		return connectionUrl1;
	}

	/**
	 * @param connectionUrl1 the connectionUrl1 to set
	 */
	public void setConnectionUrl1(String connectionUrl1) {
		this.connectionUrl1 = connectionUrl1;
	}

	/**
	 * @return the connectionUrl2
	 */
	public String getConnectionUrl2() {
		return connectionUrl2;
	}

	/**
	 * @param connectionUrl2 the connectionUrl2 to set
	 */
	public void setConnectionUrl2(String connectionUrl2) {
		this.connectionUrl2 = connectionUrl2;
	}

	/**
	 * @return the userName1
	 */
	public String getUserName1() {
		return userName1;
	}

	/**
	 * @param userName1 the userName1 to set
	 */
	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	/**
	 * @return the userName2
	 */
	public String getUserName2() {
		return userName2;
	}

	/**
	 * @param userName2 the userName2 to set
	 */
	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	/**
	 * @return the password1
	 */
	public String getPassword1() {
		return password1;
	}

	/**
	 * @param password1 the password1 to set
	 */
	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	/**
	 * @return the password2
	 */
	public String getPassword2() {
		return password2;
	}

	/**
	 * @param password2 the password2 to set
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
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
	 * @return the fileLocation
	 */
	public String getFileLocation() {
		return fileLocation;
	}

	/**
	 * @param fileLocation the fileLocation to set
	 */
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the apacheHome
	 */
	public String getApacheHome() {
		return apacheHome;
	}

	/**
	 * @param apacheHome the apacheHome to set
	 */
	public void setApacheHome(String apacheHome) {
		this.apacheHome = apacheHome;
	}

	/**
	 * @return the httpPort
	 */
	public String getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(String httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the httpsPort
	 */
	public String getHttpsPort() {
		return httpsPort;
	}

	/**
	 * @param httpsPort the httpsPort to set
	 */
	public void setHttpsPort(String httpsPort) {
		this.httpsPort = httpsPort;
	}

	/**
	 * @return the uriworkermap
	 */
	public String getUriworkermap() {
		return uriworkermap;
	}

	/**
	 * @param uriworkermap the uriworkermap to set
	 */
	public void setUriworkermap(String uriworkermap) {
		this.uriworkermap = uriworkermap;
	}

	/**
	 * @return the workers
	 */
	public String getWorkers() {
		return workers;
	}

	/**
	 * @param workers the workers to set
	 */
	public void setWorkers(String workers) {
		this.workers = workers;
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
	 * @return the httpEnable
	 */
	public String getHttpEnable() {
		return httpEnable;
	}

	/**
	 * @param httpEnable the httpEnable to set
	 */
	public void setHttpEnable(String httpEnable) {
		this.httpEnable = httpEnable;
	}

	/**
	 * @return the highAvailability
	 */
	public String getHighAvailability() {
		return highAvailability;
	}

	/**
	 * @param highAvailability the highAvailability to set
	 */
	public void setHighAvailability(String highAvailability) {
		this.highAvailability = highAvailability;
	}

	/**
	 * @return the otherBindAddress
	 */
	public String getOtherBindAddress() {
		return otherBindAddress;
	}

	/**
	 * @param otherBindAddress the otherBindAddress to set
	 */
	public void setOtherBindAddress(String otherBindAddress) {
		this.otherBindAddress = otherBindAddress;
	}

	/**
	 * @return the localIPAddress
	 */
	public String getLocalIPAddress() {
		return localIPAddress;
	}

	/**
	 * @param localIPAddress the localIPAddress to set
	 */
	public void setLocalIPAddress(String localIPAddress) {
		this.localIPAddress = localIPAddress;
	}

	/**
	 * @return the maxIdle1
	 */
	public String getMaxIdle1() {
		return maxIdle1;
	}

	/**
	 * @param maxIdle1 the maxIdle1 to set
	 */
	public void setMaxIdle1(String maxIdle1) {
		this.maxIdle1 = maxIdle1;
	}

	/**
	 * @return the maxIdle2
	 */
	public String getMaxIdle2() {
		return maxIdle2;
	}

	/**
	 * @param maxIdle2 the maxIdle2 to set
	 */
	public void setMaxIdle2(String maxIdle2) {
		this.maxIdle2 = maxIdle2;
	}

	/**
	 * @return the maxActive1
	 */
	public String getMaxActive1() {
		return maxActive1;
	}

	/**
	 * @param maxActive1 the maxActive1 to set
	 */
	public void setMaxActive1(String maxActive1) {
		this.maxActive1 = maxActive1;
	}

	/**
	 * @return the maxActive2
	 */
	public String getMaxActive2() {
		return maxActive2;
	}

	/**
	 * @param maxActive2 the maxActive2 to set
	 */
	public void setMaxActive2(String maxActive2) {
		this.maxActive2 = maxActive2;
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
	 * @return the baseTemplate
	 */
	public String getBaseTemplate() {
		return baseTemplate;
	}

	/**
	 * @param baseTemplate the baseTemplate to set
	 */
	public void setBaseTemplate(String baseTemplate) {
		this.baseTemplate = baseTemplate;
	}

	/**
	 * @return the serverBase
	 */
	public String getServerBase() {
		return serverBase;
	}

	/**
	 * @param serverBase the serverBase to set
	 */
	public void setServerBase(String serverBase) {
		this.serverBase = serverBase;
	}

	/**
	 * @return the domainIp
	 */
	public String getDomainIp() {
		return domainIp;
	}

	/**
	 * @param domainIp the domainIp to set
	 */
	public void setDomainIp(String domainIp) {
		this.domainIp = domainIp;
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
	 * @return the udpGroupAddr
	 */
	public String getUdpGroupAddr() {
		return udpGroupAddr;
	}

	/**
	 * @param udpGroupAddr the udpGroupAddr to set
	 */
	public void setUdpGroupAddr(String udpGroupAddr) {
		this.udpGroupAddr = udpGroupAddr;
	}

	/**
	 * @return the multicastPort
	 */
	public String getMulticastPort() {
		return multicastPort;
	}

	/**
	 * @param multicastPort the multicastPort to set
	 */
	public void setMulticastPort(String multicastPort) {
		this.multicastPort = multicastPort;
	}

	/**
	 * @return the serverPeerID
	 */
	public String getServerPeerID() {
		return serverPeerID;
	}

	/**
	 * @param serverPeerID the serverPeerID to set
	 */
	public void setServerPeerID(String serverPeerID) {
		this.serverPeerID = serverPeerID;
	}

	/**
	 * @return the jvmRoute
	 */
	public String getJvmRoute() {
		return jvmRoute;
	}

	/**
	 * @param jvmRoute the jvmRoute to set
	 */
	public void setJvmRoute(String jvmRoute) {
		this.jvmRoute = jvmRoute;
	}

	/**
	 * @return the minPoolSize1
	 */
	public String getMinPoolSize1() {
		return minPoolSize1;
	}

	/**
	 * @param minPoolSize1 the minPoolSize1 to set
	 */
	public void setMinPoolSize1(String minPoolSize1) {
		this.minPoolSize1 = minPoolSize1;
	}

	/**
	 * @return the minPoolSize2
	 */
	public String getMinPoolSize2() {
		return minPoolSize2;
	}

	/**
	 * @param minPoolSize2 the minPoolSize2 to set
	 */
	public void setMinPoolSize2(String minPoolSize2) {
		this.minPoolSize2 = minPoolSize2;
	}

	/**
	 * @return the maxPoolSize1
	 */
	public String getMaxPoolSize1() {
		return maxPoolSize1;
	}

	/**
	 * @param maxPoolSize1 the maxPoolSize1 to set
	 */
	public void setMaxPoolSize1(String maxPoolSize1) {
		this.maxPoolSize1 = maxPoolSize1;
	}

	/**
	 * @return the maxPoolSize2
	 */
	public String getMaxPoolSize2() {
		return maxPoolSize2;
	}

	/**
	 * @param maxPoolSize2 the maxPoolSize2 to set
	 */
	public void setMaxPoolSize2(String maxPoolSize2) {
		this.maxPoolSize2 = maxPoolSize2;
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