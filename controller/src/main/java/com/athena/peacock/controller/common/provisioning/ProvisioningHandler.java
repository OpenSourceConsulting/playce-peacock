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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.common.core.action.FileWriteAction;
import com.athena.peacock.common.core.action.ShellAction;
import com.athena.peacock.common.core.command.Command;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.common.netty.message.ProvisioningResponseMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.config.ConfigDto;
import com.athena.peacock.controller.web.config.ConfigService;
import com.athena.peacock.controller.web.software.SoftwareDto;
import com.athena.peacock.controller.web.software.SoftwareService;

/**
 * <pre>
 * Software 설치 및 Config 파일 변경 등 프로비저닝 관련 지원 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("provisioningHandler")
public class ProvisioningHandler {

    protected final Logger logger = LoggerFactory.getLogger(ProvisioningHandler.class);

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	@Inject
	@Named("softwareService")
	private SoftwareService softwareService;

	@Inject
	@Named("configService")
	private ConfigService configService;

	public void install(ProvisioningDetail provisioningDetail) throws Exception {
		if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("apache") > -1) {
			apacheInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("mysql") > -1) {
			mysqlInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("jboss") > -1) {
			jbossInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("tomcat") > -1) {
			tomcatInstall(provisioningDetail);
		}
	}

	public void remove(ProvisioningDetail provisioningDetail) throws Exception {
		if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("apache") > -1) {
			apacheRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("mysql") > -1) {
			mysqlRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("jboss") > -1) {
			jbossRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("tomcat") > -1) {
			tomcatRemove(provisioningDetail);
		}
	}
    
	private void apacheInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		String targetDir = (StringUtils.isEmpty(provisioningDetail.getTargetDir()) ? "/usr/local/apache" : provisioningDetail.getTargetDir());
		String serverRoot = (StringUtils.isEmpty(provisioningDetail.getServerRoot()) ? targetDir : provisioningDetail.getServerRoot());
		String port = (StringUtils.isEmpty(provisioningDetail.getPort()) ? "80" : provisioningDetail.getPort());
		String serverDomain = (StringUtils.isEmpty(provisioningDetail.getServerDomain()) ? "localhost" : provisioningDetail.getServerDomain());
		String version = provisioningDetail.getVersion();
		
		Command command = new Command("Apache INSTALL");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/" + version + "/httpd-" + version + ".tar.gz");
		s_action.addArguments("-O");
		s_action.addArguments("httpd-" + version + ".tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("tar");
		s_action.addArguments("xvzf");
		s_action.addArguments("httpd-" + version + ".tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version + "/srclib");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/apr-1.4.6.tar.gz");
		s_action.addArguments("-O");
		s_action.addArguments("apr-1.4.6.tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version + "/srclib");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/apr-util-1.5.2.tar.gz");
		s_action.addArguments("-O");
		s_action.addArguments("apr-util-1.5.2.tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version + "/srclib");
		s_action.setCommand("tar");
		s_action.addArguments("xvzf");
		s_action.addArguments("apr-1.4.6.tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version + "/srclib");
		s_action.setCommand("tar");
		s_action.addArguments("xvzf");
		s_action.addArguments("apr-util-1.5.2.tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version + "/srclib");
		s_action.setCommand("mv");
		s_action.addArguments("apr-1.4.6");
		s_action.addArguments("apr");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version + "/srclib");
		s_action.setCommand("mv");
		s_action.addArguments("apr-util-1.5.2");
		s_action.addArguments("apr-util");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/pcre-7.8-6.el6.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("pcre-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/pcre-devel-7.8-6.el6.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("pcre-devel-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("pcre-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("pcre-devel-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		/*
		s_action = new ShellAction(sequence++);
		s_action.setCommand("yum");
		s_action.addArguments("install");
		s_action.addArguments("-y");
		s_action.addArguments("pcre-devel.x86_64");
		//s_action.addArguments("apr-devel");
		//s_action.addArguments("apr-util-devel");
		//s_action.addArguments("gcc");
		//s_action.addArguments("zlib-devel");
		//s_action.addArguments("openssl-devel");
		command.addAction(s_action);
		*/
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version);
		s_action.setCommand("./configure");
		s_action.addArguments("--prefix=" + targetDir);
		s_action.addArguments("--enable-mods-shared=most");
		s_action.addArguments("--enable-ssl");
		s_action.addArguments("--with-ssl=/usr/local/openssl");
		s_action.addArguments("--enable-modules=ssl");
		s_action.addArguments("--enable-rewrite");
		s_action.addArguments("--with-included-apr");
		s_action.addArguments("--with-included-apr-util");
		s_action.addArguments("--enable-deflate");
		s_action.addArguments("--enable-expires");
		s_action.addArguments("--enable-headers");
		s_action.addArguments("--enable-proxy");
		
		if (version.startsWith("2.2")) {
			//s_action.addArguments("--with-mpm=prefork");
			s_action.addArguments("--enable-so");
			s_action.addArguments("--with-mpm=worker");
		} else if (version.startsWith("2.3") || version.startsWith("2.4")) {
			s_action.addArguments("--enable-mpms-shared=all");
		}
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version);
		s_action.setCommand("make");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/httpd-" + version);
		s_action.setCommand("make");
		s_action.addArguments("install");
		command.addAction(s_action);
		
		// Add Apache INSTALL Command
		cmdMsg.addCommand(command);
		
		command = new Command("JK Connector INSTALL");
		sequence = 0;
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/tomcat-connectors-1.2.37-src.tar.gz");
		s_action.addArguments("-O");
		s_action.addArguments("tomcat-connectors-1.2.37-src.tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("tar");
		s_action.addArguments("xvzf");
		s_action.addArguments("tomcat-connectors-1.2.37-src.tar.gz");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/tomcat-connectors-1.2.37-src/native");
		s_action.setCommand("./configure");
		s_action.addArguments("--with-apxs=" + targetDir + "/bin/apxs");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src/tomcat-connectors-1.2.37-src/native");
		s_action.setCommand("make");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("cp");
		s_action.addArguments("-f");
		s_action.addArguments("/usr/local/src/tomcat-connectors-1.2.37-src/native/apache-2.0/mod_jk.so");
		s_action.addArguments(targetDir + "/modules/mod_jk.so");
		command.addAction(s_action);
		
		// Add JK Connector INSTALL Command
		cmdMsg.addCommand(command);
		
		command = new Command("CONFIGURATION");
		sequence = 0;
		
		String httpdConf = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/apache/" + version + "/conf/httpd.conf"), "UTF-8");
		httpdConf = httpdConf.replaceAll("\\$\\{ServerRoot\\}", serverRoot)
					.replaceAll("\\$\\{Port\\}", port)
					.replaceAll("\\$\\{ServerName\\}", serverDomain);
		
		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(httpdConf);
		fw_action.setFileName(targetDir + "/conf/httpd.conf");
		command.addAction(fw_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(targetDir + "/conf");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/" + version + "/conf/mod-jk.conf");
		s_action.addArguments("-O");
		s_action.addArguments("mod-jk.conf");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(targetDir + "/conf");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/" + version + "/conf/workers.properties");
		s_action.addArguments("-O");
		s_action.addArguments("workers.properties");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(targetDir + "/conf");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/" + version + "/conf/uriworkermap.properties");
		s_action.addArguments("-O");
		s_action.addArguments("uriworkermap.properties");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(targetDir + "/conf/extra");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/apache/" + version + "/conf/httpd-mpm.conf");
		s_action.addArguments("-O");
		s_action.addArguments("httpd-mpm.conf");
		command.addAction(s_action);
		
		String httpd = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/apache/httpd"), "UTF-8");
		httpd = httpd.replaceAll("\\$\\{TARGET_DIR\\}", targetDir);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(httpd);
		fw_action.setFileName("/etc/init.d/httpd");
		command.addAction(fw_action);

		s_action = new ShellAction(sequence++);
		s_action.setCommand("chmod");
		s_action.addArguments("+x");
		s_action.addArguments("/etc/init.d/httpd");
		command.addAction(s_action);
		
		// Add CONFIGURATION Command
		cmdMsg.addCommand(command);
		
		if (provisioningDetail.getAutoStart().equals("Y")) {
			command = new Command("Service Start");
			sequence = 0;
			s_action = new ShellAction(sequence++);
			s_action.setCommand("service");
			s_action.addArguments("httpd");
			s_action.addArguments("start");
			command.addAction(s_action);
			
			cmdMsg.addCommand(command);
		}

		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(targetDir);
		software.setInstallStat("INSTALLING");
		software.setDescription("Apache Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(targetDir + "/conf");
		config.setConfigFileName("httpd.conf");
		config.setConfigFileContents(httpdConf);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(targetDir + "/conf");
		config.setConfigFileName("mod-jk.conf");
		config.setConfigFileContents(IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/apache/" + version + "/conf/mod-jk.conf"), "UTF-8"));
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(targetDir + "/conf");
		config.setConfigFileName("workers.properties");
		config.setConfigFileContents(IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/apache/" + version + "/conf/workers.properties"), "UTF-8"));
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(targetDir + "/conf");
		config.setConfigFileName("uriworkermap.properties");
		config.setConfigFileContents(IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/apache/" + version + "/conf/uriworkermap.properties"), "UTF-8"));
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(targetDir + "/conf/extra");
		config.setConfigFileName("httpd-mpm.conf");
		config.setConfigFileContents(IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/apache/" + version + "/conf/httpd-mpm.conf"), "UTF-8"));
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList).start();
	}
	
	private void mysqlInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);

		String dataDir = (StringUtils.isEmpty(provisioningDetail.getDataDir()) ? "/var/lib/mysql" : provisioningDetail.getDataDir());
		String port = (StringUtils.isEmpty(provisioningDetail.getPort()) ? "3306" : provisioningDetail.getPort());
		String version = provisioningDetail.getVersion();
		String password = provisioningDetail.getPassword();
		
		Command command = new Command("CONFIGURATION");
		int sequence = 0;
		
		String myCnf = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/mysql/" + version + "/my.cnf"), "UTF-8");
		myCnf = myCnf.replaceAll("\\$\\{mysql.datadir\\}", dataDir)
					.replaceAll("\\$\\{mysql.port\\}", port);
		
		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(myCnf);
		fw_action.setFileName("/etc/my.cnf");
		command.addAction(fw_action);
		
		// Add CONFIGURATION Command
		cmdMsg.addCommand(command);
		
		command = new Command("MySQL INSTALL");
		sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/mysql/" + version + "/MySQL-server.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("MySQL-server.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/mysql/" + version + "/MySQL-client.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("MySQL-client.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("MySQL-server.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("MySQL-client.rpm");
		command.addAction(s_action);
		
		// Add MySQL INSTALL Command
		cmdMsg.addCommand(command);
		
		if (!StringUtils.isEmpty(password)) {
			command = new Command("Change Password");
			sequence = 0;
			s_action = new ShellAction(sequence++);
			s_action.setCommand("service");
			s_action.addArguments("mysql");
			s_action.addArguments("start");
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setCommand("mysqladmin");
			s_action.addArguments("-u");
			s_action.addArguments("root");
			s_action.addArguments("password");
			s_action.addArguments(password);
			command.addAction(s_action);

			if (provisioningDetail.getAutoStart().equals("N")) {
				s_action = new ShellAction(sequence++);
				s_action.setCommand("service");
				s_action.addArguments("mysql");
				s_action.addArguments("stop");
				command.addAction(s_action);
			}
			
			// Add Change Password Command
			cmdMsg.addCommand(command);
		} else {
			if (provisioningDetail.getAutoStart().equals("Y")) {
				command = new Command("Service Start");
				sequence = 0;
				s_action = new ShellAction(sequence++);
				s_action.setCommand("service");
				s_action.addArguments("mysql");
				s_action.addArguments("start");
				command.addAction(s_action);

				// Add Service Start Command
				cmdMsg.addCommand(command);
			}
		}
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(dataDir);
		software.setInstallStat("INSTALLING");
		software.setDescription("MySQL Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation("/etc");
		config.setConfigFileName("my.cnf");
		config.setConfigFileContents(myCnf);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList).start();
	}
	
	private void jbossInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		/**
		 * JBoss Variables
		 */
		String javaHome = provisioningDetail.getJavaHome();
		String jbossHome = provisioningDetail.getJbossHome();
		String serverHome = provisioningDetail.getServerHome();
		String serverName = provisioningDetail.getServerName();
		String partitionName = (StringUtils.isEmpty(provisioningDetail.getPartitionName()) ? "partition" : provisioningDetail.getPartitionName());
		String bindAddress = (StringUtils.isEmpty(provisioningDetail.getBindAddress()) ? "0.0.0.0" : provisioningDetail.getBindAddress());
		String bindPort = (StringUtils.isEmpty(provisioningDetail.getBindPort()) ? "ports-default" : provisioningDetail.getBindPort());
		
		/**
		 * DataSource Variables
		 */
		String databaseType = provisioningDetail.getDatabaseType();
		String jndiName = provisioningDetail.getJndiName();
		String connectionUrl = provisioningDetail.getConnectionUrl();
		String userName = provisioningDetail.getUserName();
		String password = provisioningDetail.getPassword();
		String minPoolSize = (StringUtils.isEmpty(provisioningDetail.getMinPoolSize()) ? "10" : provisioningDetail.getMinPoolSize());
		String maxPoolSize = (StringUtils.isEmpty(provisioningDetail.getMaxPoolSize()) ? "20" : provisioningDetail.getMaxPoolSize());
		
		Command command = new Command("Pre-install");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setCommand("mkdir");
		s_action.addArguments("-p");
		s_action.addArguments(jbossHome);
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("mkdir");
		s_action.addArguments("-p");
		s_action.addArguments(serverHome);
		command.addAction(s_action);
		
		// Add Pre-install Command
		cmdMsg.addCommand(command);
		
		command = new Command("JBoss INSTALL");
		sequence = 0;
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/jboss/jboss-eap-5.2.0.zip");
		s_action.addArguments("-O");
		s_action.addArguments("jboss-eap-5.2.0.zip");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("unzip");
		s_action.addArguments("-o");
		s_action.addArguments("jboss-eap-5.2.0.zip");
		s_action.addArguments("-d");
		s_action.addArguments(jbossHome);
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/jboss/jboss-cluster-template-5.2.0.zip");
		s_action.addArguments("-O");
		s_action.addArguments("jboss-cluster-template-5.2.0.zip");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("unzip");
		s_action.addArguments("-o");
		s_action.addArguments("jboss-cluster-template-5.2.0.zip");
		s_action.addArguments("-d");
		s_action.addArguments(serverHome + "/" + serverName);
		command.addAction(s_action);
		
		String envSh = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/jboss/env.sh"), "UTF-8");
		envSh = envSh.replaceAll("\\$\\{java.home\\}", javaHome)
					.replaceAll("\\$\\{jboss.home\\}", jbossHome)
					.replaceAll("\\$\\{server.home\\}", serverHome)
					.replaceAll("\\$\\{server.name\\}", serverName)
					.replaceAll("\\$\\{partition.name\\}", partitionName)
					.replaceAll("\\$\\{bind.address\\}", bindAddress)
					.replaceAll("\\$\\{bind.port\\}", bindPort);
		
		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(serverHome + "/" + serverName + "/bin/env.sh");
		command.addAction(fw_action);
		
		// Add JBoss INSTALL Command
		cmdMsg.addCommand(command);

		String datasource = null;
		if (databaseType != null && (databaseType.equals("oracle") || databaseType.equals("mysql") || databaseType.equals("cubrid"))) {
			command = new Command("DataSource Configuration");
			sequence = 0;
			
			if (databaseType.equals("oracle")) {
				datasource = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/jboss/datasource/oracle-ds.xml"), "UTF-8");
			} else if (databaseType.equals("mysql")) {
				datasource = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/jboss/datasource/mysql-ds.xml"), "UTF-8");
			} else if (databaseType.equals("cubrid")) {
				datasource = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/jboss/datasource/cubrid-ds.xml"), "UTF-8");
			}

			datasource = datasource.replaceAll("\\$\\{jndi.name\\}", jndiName)
						.replaceAll("\\$\\{connection.url\\}", connectionUrl)
						.replaceAll("\\$\\{user.name\\}", userName)
						.replaceAll("\\$\\{user.password\\}", password)
						.replaceAll("\\$\\{pool.min\\}", minPoolSize)
						.replaceAll("\\$\\{pool.max\\}", maxPoolSize);
			
			fw_action = new FileWriteAction(sequence++);
			fw_action.setContents(datasource);
			fw_action.setFileName(serverHome + "/" + serverName + "/" + serverName + "-ds.xml");
			command.addAction(fw_action);
			
			// Add DataSource Configuration Command
			cmdMsg.addCommand(command);
		}
		
		if (provisioningDetail.getAutoStart().equals("Y")) {
			command = new Command("Service Start");
			sequence = 0;
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverHome + "/" + serverName + "/bin");
			s_action.setCommand("sh");
			s_action.addArguments("nohup.sh");
			command.addAction(s_action);
			
			cmdMsg.addCommand(command);
		}
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(jbossHome + "," + serverHome + "/" + serverName);
		software.setInstallStat("INSTALLING");
		software.setDescription("JBoss Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/" + serverName + "/bin");
		config.setConfigFileName("env.sh");
		config.setConfigFileContents(envSh);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		if (datasource != null) {
			config = new ConfigDto();
			config.setMachineId(provisioningDetail.getMachineId());
			config.setSoftwareId(provisioningDetail.getSoftwareId());
			config.setConfigFileLocation(serverHome + "/" + serverName);
			config.setConfigFileName(serverName + "-ds.xml");
			config.setConfigFileContents(datasource);
			config.setDeleteYn("N");
			config.setRegUserId(provisioningDetail.getUserId());
			config.setUpdUserId(provisioningDetail.getUserId());
			configList.add(config);
		}

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList).start();
	}
	
	private void tomcatInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);

		/**
		 * Tomcat Variables
		 */
		String javaHome = provisioningDetail.getJavaHome();
		String catalinaHome = provisioningDetail.getCatalinaHome();
		String catalinaBase = provisioningDetail.getCatalinaBase();
		String serverName = provisioningDetail.getServerName();
		String portOffset = (StringUtils.isEmpty(provisioningDetail.getPortOffset()) ? "0" : provisioningDetail.getPortOffset());
		String compUser = provisioningDetail.getCompUser();
		
		Command command = new Command("Pre-install");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setCommand("mkdir");
		s_action.addArguments("-p");
		s_action.addArguments(catalinaHome);
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("mkdir");
		s_action.addArguments("-p");
		s_action.addArguments(catalinaBase + "/" + serverName);
		command.addAction(s_action);
		
		// Add Pre-install Command
		cmdMsg.addCommand(command);
		
		command = new Command("Tomcat INSTALL");
		sequence = 0;

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/tomcat/apache-tomcat-6.0.37.zip");
		s_action.addArguments("-O");
		s_action.addArguments("apache-tomcat-6.0.37.zip");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("unzip");
		s_action.addArguments("-o");
		s_action.addArguments("apache-tomcat-6.0.37.zip");
		s_action.addArguments("-d");
		s_action.addArguments(catalinaHome);
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}/tomcat/tomcat-template-6.0.37.zip");
		s_action.addArguments("-O");
		s_action.addArguments("tomcat-template-6.0.37.zip");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/usr/local/src");
		s_action.setCommand("unzip");
		s_action.addArguments("-o");
		s_action.addArguments("tomcat-template-6.0.37.zip");
		s_action.addArguments("-d");
		s_action.addArguments(catalinaBase + "/" + serverName);
		command.addAction(s_action);
		
		String envSh = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/tomcat/env.sh"), "UTF-8");
		envSh = envSh.replaceAll("\\$\\{java.home\\}", javaHome)
					.replaceAll("\\$\\{server.name\\}", serverName)
					.replaceAll("\\$\\{catalina.home\\}", catalinaHome)
					.replaceAll("\\$\\{catalina.base\\}", catalinaBase)
					.replaceAll("\\$\\{port.offset\\}", portOffset)
					.replaceAll("\\$\\{comp.user\\}", compUser);
		
		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(catalinaBase + "/" + serverName + "/bin/env.sh");
		command.addAction(fw_action);
		
		// Add Tomcat INSTALL Command
		cmdMsg.addCommand(command);
		
		if (provisioningDetail.getAutoStart().equals("Y")) {
			command = new Command("Service Start");
			sequence = 0;
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(catalinaBase + "/" + serverName + "/bin");
			s_action.setCommand("sh");
			s_action.addArguments("nohup.sh");
			command.addAction(s_action);
			
			cmdMsg.addCommand(command);
		}
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(catalinaHome + "," + catalinaBase + "/" + serverName);
		software.setInstallStat("INSTALLING");
		software.setDescription("Tomcat Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(catalinaBase + "/" + serverName + "/bin");
		config.setConfigFileName("env.sh");
		config.setConfigFileContents(envSh);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList).start();
	}
	
	private void apacheRemove(ProvisioningDetail provisioningDetail) throws Exception {
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setDeleteYn("N");
		config.setStart(0);
		config.setLimit(100);
		config.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = configService.getConfigList(config);
		
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		Command command = new Command("Uninstall");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setCommand("service");
		s_action.addArguments("httpd");
		s_action.addArguments("stop");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("rm");
		s_action.addArguments("-f");
		s_action.addArguments("/etc/init.d/httpd");
		command.addAction(s_action);
		
		for (ConfigDto _config : configList) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-f");
			s_action.addArguments(_config.getConfigFileLocation() + "/" + _config.getConfigFileName());
			command.addAction(s_action);
		}
		
		String[] installLocation = software.getInstallLocation().split(",");
		for (String location : installLocation) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(location);
			command.addAction(s_action);
		}
		
		// Add Uninstall Command
		cmdMsg.addCommand(command);
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		software.setInstallStat("UNINSTALLING");

		new UninstallThread(peacockTransmitter, softwareService, configService, cmdMsg, software, config).start();
	}
	
	private void mysqlRemove(ProvisioningDetail provisioningDetail) throws Exception {
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setDeleteYn("N");
		config.setStart(0);
		config.setLimit(100);
		config.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = configService.getConfigList(config);
		
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		Command command = new Command("Uninstall");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setCommand("service");
		s_action.addArguments("mysql");
		s_action.addArguments("stop");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("rpm");
		s_action.addArguments("--erase");
		s_action.addArguments("MySQL-server");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("rpm");
		s_action.addArguments("--erase");
		s_action.addArguments("MySQL-client");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("rm");
		s_action.addArguments("-rf");
		s_action.addArguments("/usr/lib64/mysql");
		command.addAction(s_action);
		
		for (ConfigDto _config : configList) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-f");
			s_action.addArguments(_config.getConfigFileLocation() + "/" + _config.getConfigFileName());
			command.addAction(s_action);
		}
		
		String[] installLocation = software.getInstallLocation().split(",");
		for (String location : installLocation) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(location);
			command.addAction(s_action);
		}
		
		// Add Uninstall Command
		cmdMsg.addCommand(command);
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		software.setInstallStat("UNINSTALLING");

		new UninstallThread(peacockTransmitter, softwareService, configService, cmdMsg, software, config).start();
	}
	
	private void jbossRemove(ProvisioningDetail provisioningDetail) throws Exception {
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setDeleteYn("N");
		config.setStart(0);
		config.setLimit(100);
		config.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = configService.getConfigList(config);
		
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		Command command = new Command("Uninstall");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(configList.get(0).getConfigFileLocation());
		s_action.setCommand("sh");
		s_action.addArguments("kill.sh");
		command.addAction(s_action);
		
		for (ConfigDto _config : configList) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-f");
			s_action.addArguments(_config.getConfigFileLocation() + "/" + _config.getConfigFileName());
			command.addAction(s_action);
		}
		
		String[] installLocation = software.getInstallLocation().split(",");
		for (String location : installLocation) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(location);
			command.addAction(s_action);
		}
		
		// Add Uninstall Command
		cmdMsg.addCommand(command);
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		software.setInstallStat("UNINSTALLING");

		new UninstallThread(peacockTransmitter, softwareService, configService, cmdMsg, software, config).start();
	}
	
	private void tomcatRemove(ProvisioningDetail provisioningDetail) throws Exception {
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setDeleteYn("N");
		config.setStart(0);
		config.setLimit(100);
		config.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = configService.getConfigList(config);
		
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		Command command = new Command("Uninstall");
		int sequence = 0;
		
		ShellAction s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(configList.get(0).getConfigFileLocation());
		s_action.setCommand("sh");
		s_action.addArguments("kill.sh");
		command.addAction(s_action);
		
		for (ConfigDto _config : configList) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-f");
			s_action.addArguments(_config.getConfigFileLocation() + "/" + _config.getConfigFileName());
			command.addAction(s_action);
		}
		
		String[] installLocation = software.getInstallLocation().split(",");
		for (String location : installLocation) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(location);
			command.addAction(s_action);
		}
		
		// Add Uninstall Command
		cmdMsg.addCommand(command);
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		software.setInstallStat("UNINSTALLING");

		new UninstallThread(peacockTransmitter, softwareService, configService, cmdMsg, software, config).start();
	}
}
//end of ProvisioningHandler.java

class InstallThread extends Thread {

	private PeacockTransmitter peacockTransmitter;
	private SoftwareService softwareService;
	private ProvisioningCommandMessage cmdMsg;
	private SoftwareDto software;
	private List<ConfigDto> configList;
	
	public InstallThread(PeacockTransmitter peacockTransmitter, SoftwareService softwareService,
			ProvisioningCommandMessage cmdMsg, SoftwareDto software, List<ConfigDto> configList) {
		this.peacockTransmitter = peacockTransmitter;
		this.softwareService = softwareService;
		this.cmdMsg = cmdMsg;
		this.software = software;
		this.configList = configList;
	}
	
	@Override
	public void run() {
		try {
			softwareService.insertSoftware(software);
			
			PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
			ProvisioningResponseMessage response = peacockTransmitter.sendMessage(datagram);
			
			StringBuilder sb = new StringBuilder("");
			List<String> results = response.getResults();
			for (String result : results) {
				sb.append(result + "\n");
			}
			software.setInstallStat("COMPLETED");
			software.setInstallLog(sb.toString());
			
			softwareService.insertSoftware(software, configList);
		} catch (Exception e) {
			software.setInstallStat("INST_ERROR");
			software.setInstallLog(e.getMessage());
			
			e.printStackTrace();
		}
	}
}

class UninstallThread extends Thread {

	private PeacockTransmitter peacockTransmitter;
	private SoftwareService softwareService;
	private ConfigService configService;
	private ProvisioningCommandMessage cmdMsg;
	private SoftwareDto software;
	private ConfigDto config;
	
	public UninstallThread(PeacockTransmitter peacockTransmitter, SoftwareService softwareService,
			ConfigService configService, ProvisioningCommandMessage cmdMsg, SoftwareDto software, ConfigDto config) {
		this.peacockTransmitter = peacockTransmitter;
		this.softwareService = softwareService;
		this.configService = configService;
		this.cmdMsg = cmdMsg;
		this.software = software;
		this.config = config;
	}
	
	@Override
	public void run() {
		try {
			softwareService.updateSoftware(software);
			
			PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
			ProvisioningResponseMessage response = peacockTransmitter.sendMessage(datagram);
			
			configService.deleteConfig(config);
			
			StringBuilder sb = new StringBuilder("");
			List<String> results = response.getResults();
			for (String result : results) {
				sb.append(result + "\n");
			}
			software.setDeleteYn("Y");
			software.setInstallStat("DELETED");
			software.setInstallLog(sb.toString());
			
			softwareService.updateSoftware(software);
		} catch (Exception e) {
			software.setInstallStat("UNINST_ERROR");
			software.setInstallLog(e.getMessage());
			
			e.printStackTrace();
		}
	}
}