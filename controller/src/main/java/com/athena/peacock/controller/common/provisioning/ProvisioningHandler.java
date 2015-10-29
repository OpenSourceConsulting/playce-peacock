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

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.jboss.ews.dbcp.BlowfishEncrypter;
import org.picketbox.datasource.security.SecureIdentityLoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.common.core.action.ConfigAction;
import com.athena.peacock.common.core.action.FileWriteAction;
import com.athena.peacock.common.core.action.ShellAction;
import com.athena.peacock.common.core.action.support.Property;
import com.athena.peacock.common.core.command.Command;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.common.netty.message.ProvisioningResponseMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.ceph.CephService;
import com.athena.peacock.controller.web.ceph.base.CephDto;
import com.athena.peacock.controller.web.config.ConfigDto;
import com.athena.peacock.controller.web.config.ConfigService;
import com.athena.peacock.controller.web.machine.MachineService;
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

	@Inject
	@Named("machineService")
	private MachineService machineService;
	
	@Inject
	@Named("cephService")
	private CephService cephService;

	public void install(ProvisioningDetail provisioningDetail) throws Exception {
		if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("httpd") > -1) {
			httpdInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("tomcat") > -1) {
			tomcatInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("jboss") > -1) {
			jbossInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("mysql") > -1) {
			mysqlInstall(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("ceph") > -1) {
			cephInstall(provisioningDetail);
		}
	}

	public void remove(ProvisioningDetail provisioningDetail) throws Exception {
		if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("httpd") > -1) {
			httpdRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("tomcat") > -1) {
			tomcatRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("jboss") > -1) {
			jbossRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("mysql") > -1) {
			mysqlRemove(provisioningDetail);
		} else if (provisioningDetail.getSoftwareName().toLowerCase().indexOf("ceph") > -1) {
			//cephRemove(provisioningDetail);
			throw new Exception("Ceph clustering does not support delete operation.");
		}
	}
    
	private void httpdInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);

		String fileLocation = provisioningDetail.getFileLocation();
		String fileName = provisioningDetail.getFileName();
		
		String user = provisioningDetail.getUser();
		String group = provisioningDetail.getGroup();
		String version = provisioningDetail.getVersion();
		String apacheHome = provisioningDetail.getApacheHome();
		String serverHome = provisioningDetail.getServerHome();
		String httpPort = provisioningDetail.getHttpPort();
		String httpsPort = provisioningDetail.getHttpsPort();
		String uriworkermap = provisioningDetail.getUriworkermap();
		String workers = provisioningDetail.getWorkers();
		
		logger.debug("fileLocation : " + fileLocation);
		logger.debug("fileName : " + fileName);
		logger.debug("user : " + user);
		logger.debug("group : " + group);
		logger.debug("version : " + version);
		logger.debug("apacheHome : " + apacheHome);
		logger.debug("serverHome : " + serverHome);
		logger.debug("httpPort : " + httpPort);
		logger.debug("httpsPort : " + httpsPort);
		logger.debug("uriworkermap : " + uriworkermap);
		logger.debug("workers : " + workers);
		logger.debug("machineId : " + provisioningDetail.getMachineId());
		logger.debug("softwareId : " + provisioningDetail.getSoftwareId());
		logger.debug("softwareName : " + provisioningDetail.getSoftwareName());
		logger.debug("autoStart : " + provisioningDetail.getAutoStart());
		
		Command command = new Command("Apache INSTALL");
		ShellAction s_action = null;
		int sequence = 0;
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/apr-1.3.9-5.el6_2.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("apr-1.3.9-5.el6_2.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/pcre-7.8-6.el6.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("pcre-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/pcre-devel-7.8-6.el6.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("pcre-devel-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("apr-1.3.9-5.el6_2.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("pcre-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("pcre-devel-7.8-6.el6.x86_64.rpm");
		command.addAction(s_action);
		
		if (version.equals("2.2.26")) {
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("wget");
			s_action.addArguments("${RepositoryUrl}" + fileLocation + "/openssl-devel-1.0.1e-16.el6_5.15.x86_64.rpm");
			s_action.addArguments("-O");
			s_action.addArguments("openssl-devel-1.0.1e-16.el6_5.15.x86_64.rpm");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("wget");
			s_action.addArguments("${RepositoryUrl}" + fileLocation + "/openssl-1.0.1e-16.el6_5.15.x86_64.rpm");
			s_action.addArguments("-O");
			s_action.addArguments("openssl-1.0.1e-16.el6_5.15.x86_64.rpm");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("rpm");
			s_action.addArguments("-Uvh");
			s_action.addArguments("--nodeps");
			s_action.addArguments("openssl-devel-1.0.1e-16.el6_5.15.x86_64.rpm");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("rpm");
			s_action.addArguments("-Uvh");
			s_action.addArguments("--nodeps");
			s_action.addArguments("openssl-1.0.1e-16.el6_5.15.x86_64.rpm");
			command.addAction(s_action);
		}
		
		String[] fileNames = fileName.split(",");
		
		for (int i = 0; i < fileNames.length; i++) {
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("wget");
			s_action.addArguments("${RepositoryUrl}" + fileLocation + "/" + fileNames[i]);
			s_action.addArguments("-O");
			s_action.addArguments(fileNames[i]);
			command.addAction(s_action);
			
			if (i == 0) {
				s_action = new ShellAction(sequence++);
				s_action.setCommand("mkdir");
				s_action.addArguments("-p");
				s_action.addArguments(apacheHome);
				command.addAction(s_action);

				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory("/tmp");
				s_action.setCommand("unzip");
				s_action.addArguments("-o");
				s_action.addArguments(fileNames[i]);
				s_action.addArguments("-d");
				s_action.addArguments(apacheHome);
				command.addAction(s_action);

				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory(apacheHome);
				s_action.setCommand("sh");
				s_action.addArguments(".postinstall");
				command.addAction(s_action);
			} else if (i == 1) {
				s_action = new ShellAction(sequence++);
				s_action.setCommand("mkdir");
				s_action.addArguments("-p");
				s_action.addArguments(serverHome);
				command.addAction(s_action);

				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory("/tmp");
				s_action.setCommand("unzip");
				s_action.addArguments("-o");
				s_action.addArguments(fileNames[i]);
				s_action.addArguments("-d");
				s_action.addArguments(serverHome);
				command.addAction(s_action);
			}
		}
		
		// Add Apache INSTALL Command
		cmdMsg.addCommand(command);
		
		command = new Command("Save Configuration Files");
		sequence = 0;
		
		String httpdConf = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/httpd/conf/httpd.conf"), "UTF-8");
		httpdConf = httpdConf.replaceAll("\\$\\{apache.home\\}", apacheHome)
							.replaceAll("\\$\\{server.home\\}", serverHome)
							.replaceAll("\\$\\{http.port\\}", httpPort)
							.replaceAll("\\$\\{https.port\\}", httpsPort)
							.replaceAll("\\$\\{user\\}", user)
							.replaceAll("\\$\\{group\\}", group);
		
		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(httpdConf);
		fw_action.setFileName(serverHome + "/conf/httpd.conf");
		command.addAction(fw_action);
		
		String httpdInfoConf = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/httpd/conf/extra/httpd-info.conf"), "UTF-8");
		httpdInfoConf = httpdInfoConf.replaceAll("\\$\\{apache.home\\}", apacheHome)
									.replaceAll("\\$\\{server.home\\}", serverHome)
									.replaceAll("\\$\\{http.port\\}", httpPort)
									.replaceAll("\\$\\{https.port\\}", httpsPort)
									.replaceAll("\\$\\{user\\}", user)
									.replaceAll("\\$\\{group\\}", group);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(httpdInfoConf);
		fw_action.setFileName(serverHome + "/conf/extra/httpd-info.conf");
		command.addAction(fw_action);
		
		String httpdSslConf = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/httpd/conf/extra/httpd-ssl.conf"), "UTF-8");
		httpdSslConf = httpdSslConf.replaceAll("\\$\\{apache.home\\}", apacheHome)
									.replaceAll("\\$\\{server.home\\}", serverHome)
									.replaceAll("\\$\\{http.port\\}", httpPort)
									.replaceAll("\\$\\{https.port\\}", httpsPort)
									.replaceAll("\\$\\{user\\}", user)
									.replaceAll("\\$\\{group\\}", group);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(httpdSslConf);
		fw_action.setFileName(serverHome + "/conf/extra/httpd-ssl.conf");
		command.addAction(fw_action);

		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(uriworkermap);
		fw_action.setFileName(serverHome + "/conf/extra/uriworkermap.properties");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(workers);
		fw_action.setFileName(serverHome + "/conf/extra/workers.properties");
		command.addAction(fw_action);	
		
		// Add Save Configuration Files
		cmdMsg.addCommand(command);	
		
		/*
		echo "set Directory & Permission ..."
		cp -r /home2/template/* /home2/${USER}
		mv /home2/${USER}/bin/apachectl /home2/${USER}/bin/${USER}ctl
		mv /home2/${USER}/jbossews /etc/init.d/${USER}_httpd
		
		chown -R root.sys /home2/${USER}
		chmod 755 /home2/${USER}
		chown -R ${USERID}:${GROUPID} /home2/${USER}/www
		chmod 700 /home2/${USER}/www
		chown -R ${USERID}:${GROUPID} /home2/${USER}/.bash*
		chown -R ${USERID}:${GROUPID} /home2/${USER}/.*rc
		*/
		command = new Command("Set Directory & Permission");
		sequence = 0;
		
		/** mv /home2/${USER}/bin/apachectl /home2/${USER}/bin/${USER}ctl */
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverHome + "/bin");
		s_action.setCommand("mv");
		s_action.addArguments("apachectl");
		s_action.addArguments(user + "ctl");
		command.addAction(s_action);

		/** mv /home2/${USER}/jbossews /etc/init.d/${USER}_httpd */
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverHome);
		s_action.setCommand("mv");
		s_action.addArguments("jbossews");
		s_action.addArguments("/etc/init.d/" + user + "_httpd");
		command.addAction(s_action);

		/** chown -R root.sys /home2/${USER}
		 *  ~/bin, ~/conf, ~/log, ~/run */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments("root.sys");
		s_action.addArguments(serverHome + "/bin");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments("root.sys");
		s_action.addArguments(serverHome + "/conf");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments("root.sys");
		s_action.addArguments(serverHome + "/log");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments("root.sys");
		s_action.addArguments(serverHome + "/run");
		command.addAction(s_action);

		/** chmod 755 /home2/${USER} */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chmod");
		s_action.addArguments("755");
		s_action.addArguments(serverHome);
		command.addAction(s_action);

		/** chown -R ${USERID}:${GROUPID} /home2/${USER}/www */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/www");
		command.addAction(s_action);

		/** chmod 700 /home2/${USER}/www */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chmod");
		s_action.addArguments("700");
		s_action.addArguments(serverHome + "/www");
		command.addAction(s_action);

		/** chown -R ${USERID}:${GROUPID} /home2/${USER}/.bash*
		 *  chown -R ${USERID}:${GROUPID} /home2/${USER}/.*rc
		 *  .bash_logout, .bash_profile, .bashrc, .kshrc, .mkshrc, .zshrc 
		 */
		/*
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/.bash_logout");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/.bash_profile");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/.bashrc");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/.kshrc");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/.mkshrc");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome + "/.zshrc");
		command.addAction(s_action);
		*/
		
		// Add Set Directory & Permission
		cmdMsg.addCommand(command);
		
		command = new Command("Set Configurations");
		sequence = 0;
		
		List<Property> properties = new ArrayList<Property>();
		Property property = null;
		
		property = new Property();
		property.setKey("apache.home");
		property.setValue(apacheHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("server.home");
		property.setValue(serverHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("http.port");
		property.setValue(httpPort);
		properties.add(property);
		
		property = new Property();
		property.setKey("https.port");
		property.setValue(httpsPort);
		properties.add(property);
		
		property = new Property();
		property.setKey("user");
		property.setValue(user);
		properties.add(property);
		
		property = new Property();
		property.setKey("group");
		property.setValue(group);
		properties.add(property);
		
		/*
		echo "Set Configuration ..."
		sed -i "s/template/${USER}/g" /home2/${USER}/bin/${USER}ctl		
		sed -i "s/template/${USER}/g" /etc/init.d/${USER}_httpd
		sed -i "s/jbossews/${USER}ews/g" /etc/init.d/${USER}_httpd
		
		chkconfig --add ${USER}_httpd 
		chkconfig ${USER}_httpd on 
		*/
		
		/** sed -i "s/template/${USER}/g" /home2/${USER}/bin/${USER}ctl */
		ConfigAction c_action = new ConfigAction(sequence++);
		c_action.setFileName(serverHome + "/bin/" + user + "ctl");
		c_action.setProperties(properties);
		command.addAction(c_action);
		
		/** 
		 * sed -i "s/template/${USER}/g" /etc/init.d/${USER}_httpd
		 * sed -i "s/jbossews/${USER}ews/g" /etc/init.d/${USER}_httpd
		 */
		c_action = new ConfigAction(sequence++);
		c_action.setFileName("/etc/init.d/" + user + "_httpd");
		c_action.setProperties(properties);
		command.addAction(c_action);

		/** chkconfig --add ${USER}_httpd */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chkconfig");
		s_action.addArguments("--add");
		s_action.addArguments(user + "_httpd");
		command.addAction(s_action);

		/** chkconfig ${USER}_httpd on */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chkconfig");
		s_action.addArguments(user + "_httpd");
		s_action.addArguments("on");
		command.addAction(s_action);
		
		// Add Set Configurations
		cmdMsg.addCommand(command);
		
		if (provisioningDetail.getAutoStart().equals("Y")) {
			command = new Command("Service Start");
			sequence = 0;
			s_action = new ShellAction(sequence++);
			s_action.setCommand("service");
			s_action.addArguments(user + "_httpd");
			s_action.addArguments("start");
			command.addAction(s_action);
			
			// Add Service Start
			cmdMsg.addCommand(command);
		}

		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(apacheHome + "," + serverHome + "/bin," + serverHome + "/conf," + serverHome + "/log," + serverHome + "/run," + serverHome + "/www");
		software.setInstallStat("INSTALLING");
		software.setServiceStopCmd("WORKING_DIR:,CMD:service,ARGS:" + user + "_httpd stop");
		software.setServiceStartCmd("WORKING_DIR:,CMD:service,ARGS:" + user + "_httpd start");
		software.setDescription("Apache Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/conf");
		config.setConfigFileName("httpd.conf");
		config.setConfigFileContents(httpdConf);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/conf/extra");
		config.setConfigFileName("httpd-info.conf");
		config.setConfigFileContents(httpdInfoConf);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/conf/extra");
		config.setConfigFileName("httpd-ssl.conf");
		config.setConfigFileContents(httpdSslConf);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/conf/extra");
		config.setConfigFileName("uriworkermap.properties");
		config.setConfigFileContents(uriworkermap);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/conf/extra");
		config.setConfigFileName("workers.properties");
		config.setConfigFileContents(workers);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList, null, null).start();
	}
	
	private void tomcatInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		/**
		 * Tomcat Variables
		 */
		String fileLocation = provisioningDetail.getFileLocation();
		String fileName = provisioningDetail.getFileName();
		
		String user = provisioningDetail.getUser();
		String group = provisioningDetail.getGroup();
		String javaHome = provisioningDetail.getJavaHome();
		String serverHome = provisioningDetail.getServerHome();
		String serverName = provisioningDetail.getServerName();
		String catalinaHome = provisioningDetail.getCatalinaHome();
		String catalinaBase = provisioningDetail.getCatalinaBase();
		String portOffset = (StringUtils.isEmpty(provisioningDetail.getPortOffset()) ? "0" : provisioningDetail.getPortOffset());
		String encoding = provisioningDetail.getEncoding();
		String heapSize = (StringUtils.isEmpty(provisioningDetail.getHeapSize()) ? "1024" : provisioningDetail.getHeapSize());
		String permgenSize = (StringUtils.isEmpty(provisioningDetail.getPermgenSize()) ? "256" : provisioningDetail.getPermgenSize());
		String httpEnable = provisioningDetail.getHttpEnable();
		String highAvailability = provisioningDetail.getHighAvailability();
		String bindAddress = provisioningDetail.getBindAddress();
		String otherBindAddress = provisioningDetail.getOtherBindAddress();
		String localIPAddress = provisioningDetail.getLocalIPAddress();
		String hostName = provisioningDetail.getHostName();

		logger.debug("fileLocation : " + fileLocation);
		logger.debug("fileName : " + fileName);
		logger.debug("user : " + user);
		logger.debug("group : " + group);
		logger.debug("serverHome : " + serverHome);
		logger.debug("serverName : " + serverName);
		logger.debug("catalinaHome : " + catalinaHome);
		logger.debug("catalinaBase : " + catalinaBase);
		logger.debug("portOffset : " + portOffset);
		logger.debug("encoding : " + encoding);
		logger.debug("heapSize : " + heapSize);
		logger.debug("permgenSize : " + permgenSize);
		logger.debug("httpEnable : " + httpEnable);
		logger.debug("highAvailability : " + highAvailability);
		logger.debug("bindAddress : " + bindAddress);
		logger.debug("otherBindAddress : " + otherBindAddress);
		logger.debug("localIPAddress : " + localIPAddress);
		logger.debug("hostName : " + hostName);
		logger.debug("machineId : " + provisioningDetail.getMachineId());
		logger.debug("softwareId : " + provisioningDetail.getSoftwareId());
		logger.debug("softwareName : " + provisioningDetail.getSoftwareName());
		logger.debug("autoStart : " + provisioningDetail.getAutoStart());
		
		/**
		 * DataSource Variables
		 */
		String[] databaseType = provisioningDetail.getDatabaseType();
		String[] jndiName = provisioningDetail.getJndiName();
		String[] connectionUrl = provisioningDetail.getConnectionUrl();
		String[] userName = provisioningDetail.getUserName();
		String[] password = provisioningDetail.getPassword();
		String[] maxIdle = provisioningDetail.getMaxIdle();
		String[] maxActive = provisioningDetail.getMaxActive();
		
		logger.debug("databaseType : " + databaseType);
		logger.debug("jndiName : " + jndiName);
		logger.debug("connectionUrl : " + connectionUrl);
		logger.debug("userName : " + userName);
		logger.debug("password : " + password);
		logger.debug("maxIdle : " + maxIdle);
		logger.debug("maxActive : " + maxActive);
		
		Command command = new Command("Tomcat INSTALL");
		ShellAction s_action = null;
		int sequence = 0;
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/apr-1.3.9-5.el6_2.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("apr-1.3.9-5.el6_2.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		s_action.addArguments("-O");
		s_action.addArguments("apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("apr-1.3.9-5.el6_2.x86_64.rpm");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/tmp");
		s_action.setCommand("rpm");
		s_action.addArguments("-Uvh");
		s_action.addArguments("apr-util-1.3.9-3.el6_0.1.x86_64.rpm");
		command.addAction(s_action);
		
		String[] fileNames = fileName.split(",");
		
		for (int i = 0; i < fileNames.length; i++) {
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("wget");
			s_action.addArguments("${RepositoryUrl}" + fileLocation + "/" + fileNames[i]);
			s_action.addArguments("-O");
			s_action.addArguments(fileNames[i]);
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setCommand("mkdir");
			s_action.addArguments("-p");
			s_action.addArguments(serverHome);
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("unzip");
			s_action.addArguments("-o");
			s_action.addArguments(fileNames[i]);
			s_action.addArguments("-d");
			s_action.addArguments(serverHome);
			command.addAction(s_action);
		}
		
		// Add Tomcat INSTALL Command
		cmdMsg.addCommand(command);
		
		command = new Command("Directory & File Setting");
		sequence = 0;
		
		// mv /home/${user}/jbossews /etc/init.d/jbossews
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverHome);
		s_action.setCommand("mv");
		s_action.addArguments("jbossews");
		s_action.addArguments("/etc/init.d/jbossews");
		command.addAction(s_action);
		
		// mv /home/${user}/Servers/codeServer21 /home/${user}/Servers/${user}Server21
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverHome + "/Servers");
		s_action.setCommand("mv");
		s_action.addArguments("codeServer21");
		s_action.addArguments(serverName);
		command.addAction(s_action);
		
		// Add Directory & File Setting Command
		cmdMsg.addCommand(command);
		
		command = new Command("Set Configurations");
		sequence = 0;

		String envSh = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/tomcat/conf/env.sh"), "UTF-8");
		envSh = envSh.replaceAll("\\$\\{user\\}", user)
					.replaceAll("\\$\\{java.home\\}", javaHome)
					.replaceAll("\\$\\{server.home\\}", serverHome)
					.replaceAll("\\$\\{server.name\\}", serverName)
					.replaceAll("\\$\\{catalina.home\\}", catalinaHome)
					.replaceAll("\\$\\{catalina.base\\}", catalinaBase)
					.replaceAll("\\$\\{port.offset\\}", portOffset)
					.replaceAll("\\$\\{encoding\\}", encoding)
					.replaceAll("\\$\\{heap.size\\}", heapSize)
					.replaceAll("\\$\\{permgen.size\\}", permgenSize)
					.replaceAll("\\$\\{bind.addr\\}", bindAddress)
					.replaceAll("\\$\\{other.bind.addr\\}", otherBindAddress);

		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(catalinaBase + "/env.sh");
		command.addAction(fw_action);
		
		List<Property> properties = new ArrayList<Property>();
		Property property = null;
		
		property = new Property();
		property.setKey("user");
		property.setValue(user);
		properties.add(property);
		
		property = new Property();
		property.setKey("java.home");
		property.setValue(javaHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("server.home");
		property.setValue(serverHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("server.name");
		property.setValue(serverName);
		properties.add(property);
		
		property = new Property();
		property.setKey("catalina.home");
		property.setValue(catalinaHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("catalina.base");
		property.setValue(catalinaBase);
		properties.add(property);

		/** 
		 * sed -i 's/code/'${USER}'/g' /etc/init.d/jbossews
		 */
		ConfigAction c_action = new ConfigAction(sequence++);
		c_action.setFileName("/etc/init.d/jbossews");
		c_action.setProperties(properties);
		command.addAction(c_action);
		
		// Set server.xml
		String _fileName = "server-all.xml";
		String serverXml = null;
		
		if (httpEnable != null && httpEnable.toUpperCase().equals("Y")) {
			if (highAvailability != null && highAvailability.toUpperCase().equals("Y")) {
				_fileName = "server-all.xml";
			} else {
				_fileName = "server-http.xml";
			}
		} else {
			if (highAvailability != null && highAvailability.toUpperCase().equals("Y")) {
				_fileName = "server-cluster.xml";
			} else {
				_fileName = "server-none.xml";
			}
		}
		
		serverXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/tomcat/conf/" + _fileName), "UTF-8");
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(serverXml);
		fw_action.setFileName(catalinaBase + "/conf/server.xml");
		command.addAction(fw_action);
		
		// Set context.xml
		StringBuilder datasource = new StringBuilder();
		String driverClassName = null;
		String encPasswd = null;
		String query = null;
		if (databaseType != null) {
			for (int i = 0; i < databaseType.length; i++) {
				if (StringUtils.isNotEmpty(databaseType[i]) && StringUtils.isNotEmpty(jndiName[i]) && StringUtils.isNotEmpty(connectionUrl[i])
						 && StringUtils.isNotEmpty(userName[i]) && StringUtils.isNotEmpty(password[i])) {
					
					if (databaseType[i].toLowerCase().equals("oracle")) {
						driverClassName = "oracle.jdbc.driver.OracleDriver";
						query = "SELECT 1 FROM DUAL";
					} else if (databaseType[i].toLowerCase().equals("mysql")) {
						driverClassName = "com.mysql.jdbc.Driver";
						query = "SELECT 1";
					} else if (databaseType[i].toLowerCase().equals("mssql")) {
						driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
						query = "SELECT 1";
					} else if (databaseType[i].toLowerCase().equals("db2")) {
						driverClassName = "com.ibm.db2.jcc.DB2Driver";
						query = "VALUES 1";
					}
					
					encPasswd = ewsPasswordEncrypt(password[i]);
	
					datasource.append("	<Resource name=\"" + jndiName[i] + "\" auth=\"Container\"").append("\n");
					datasource.append("	          type=\"javax.sql.DataSource\" driverClassName=\"" + driverClassName + "\"").append("\n");
					datasource.append("	          url=\"" + connectionUrl[i] + "\"").append("\n");
					datasource.append("	          factory=\"org.jboss.ews.dbcp.EncryptDataSourceFactory\"").append("\n");
					datasource.append("	          username=\"" + userName[i] + "\" password=\"" + encPasswd + "\"").append("\n");
					datasource.append("	          initialSize=\"2\"").append("\n");
					datasource.append("	          maxActive=\"" + maxActive[i] + "\"").append("\n");
					datasource.append("	          maxIdle=\"" + maxIdle[i] + "\"").append("\n");
					datasource.append("	          maxWait=\"-1\"").append("\n");
					datasource.append("	          validationQuery=\"" + query + "\"").append("\n");
					datasource.append("	          testOnBorrow=\"true\"").append("\n");
					datasource.append("	          poolPreparedStatements=\"true\"").append("\n");
					datasource.append("	          maxOpenPreparedStatements=\"10\"").append("\n");
					datasource.append("	          removeAbandoned=\"true\"").append("\n");
					datasource.append("	          removeAbandonedTimeout=\"60\"").append("\n");
					datasource.append("	          logAbandoned=\"true\"").append("\n");
					datasource.append("	/>").append("\n");
				}
			}
		}

		String contextXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/tomcat/conf/context.xml"), "UTF-8");
		contextXml = contextXml.replaceAll("\\$\\{datasource\\}", datasource.toString());
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(contextXml);
		fw_action.setFileName(catalinaBase + "/conf/context.xml");
		command.addAction(fw_action);
		
		// Add Set Configurations Command
		cmdMsg.addCommand(command);
		
		command = new Command("Set Owner");
		sequence = 0;
		
		// chonw -R ${user}:${group} /home/${user}
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome);
		command.addAction(s_action);
		
		// Add Set IP & Hostname Command
		cmdMsg.addCommand(command);
		
		command = new Command("Set chkconfig & Start service");
		sequence = 0;
		
		/** chkconfig --add jbossews */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chkconfig");
		s_action.addArguments("--add");
		s_action.addArguments("jbossews");
		command.addAction(s_action);

		/** chkconfig jbossews on */
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chkconfig");
		s_action.addArguments("jbossews");
		s_action.addArguments("on");
		command.addAction(s_action);
		
		if (provisioningDetail.getAutoStart().equals("Y")) {
			s_action = new ShellAction(sequence++);
			s_action.setCommand("service");
			s_action.addArguments("jbossews");
			s_action.addArguments("start");
			command.addAction(s_action);
		}
		
		// Add Set chkconfig & Start service Command
		cmdMsg.addCommand(command);
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(serverHome + "/jboss-ews-2.1," + serverHome + "/apps," + serverHome + "/bin," + serverHome + "/Servers," + serverHome + "/svrlogs," + serverHome + "/wily");
		software.setInstallStat("INSTALLING");
		software.setServiceStopCmd("WORKING_DIR:,CMD:service,ARGS:jbossews stop");
		software.setServiceStartCmd("WORKING_DIR:,CMD:service,ARGS:jbossews start");
		software.setDescription("Tomcat Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(catalinaBase);
		config.setConfigFileName("env.sh");
		config.setConfigFileContents(envSh);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(catalinaBase + "/conf");
		config.setConfigFileName("server.xml");
		config.setConfigFileContents(serverXml);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(catalinaBase + "/conf");
		config.setConfigFileName("context.xml");
		config.setConfigFileContents(contextXml);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList, null, null).start();
	}
	
	private void jbossInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		/**
		 * JBoss Variables
		 */
		String fileLocation = provisioningDetail.getFileLocation();
		String fileName = provisioningDetail.getFileName();
		
		String user = provisioningDetail.getUser();
		String group = provisioningDetail.getGroup();
		String javaHome = provisioningDetail.getJavaHome();
		String jbossHome = provisioningDetail.getJbossHome();
		String baseTemplate = provisioningDetail.getBaseTemplate();
		String serverHome = provisioningDetail.getServerHome();
		String serverBase = provisioningDetail.getServerBase();
		String serverName = provisioningDetail.getServerName();
		String domainIp = (StringUtils.isEmpty(provisioningDetail.getDomainIp()) ? "0.0.0.0" : provisioningDetail.getDomainIp());
		String encoding = provisioningDetail.getEncoding();
		String bindPort = (StringUtils.isEmpty(provisioningDetail.getBindPort()) ? "ports-default" : provisioningDetail.getBindPort());
		String udpGroupAddr = provisioningDetail.getUdpGroupAddr();
		String multicastPort = provisioningDetail.getMulticastPort();
		String serverPeerID = provisioningDetail.getServerPeerID();
		String jvmRoute = provisioningDetail.getJvmRoute();
		String heapSize = provisioningDetail.getHeapSize();
		String permgenSize = provisioningDetail.getPermgenSize();
		String httpEnable = provisioningDetail.getHttpEnable();
		String localIPAddress = provisioningDetail.getLocalIPAddress();
		String hostName = provisioningDetail.getHostName();

		logger.debug("fileLocation : " + fileLocation);
		logger.debug("fileName : " + fileName);
		logger.debug("user : " + user);
		logger.debug("group : " + group);
		logger.debug("javaHome : " + javaHome);
		logger.debug("jbossHome : " + jbossHome);
		logger.debug("baseTemplate : " + baseTemplate);
		logger.debug("serverHome : " + serverHome);
		logger.debug("serverBase : " + serverBase);
		logger.debug("serverName : " + serverName);
		logger.debug("domainIp : " + domainIp);
		logger.debug("encoding : " + encoding);
		logger.debug("bindPort : " + bindPort);
		logger.debug("udpGroupAddr : " + udpGroupAddr);
		logger.debug("multicastPort : " + multicastPort);
		logger.debug("serverPeerID : " + serverPeerID);
		logger.debug("jvmRoute : " + jvmRoute);
		logger.debug("heapSize : " + heapSize);
		logger.debug("heapSize : " + heapSize);
		logger.debug("permgenSize : " + permgenSize);
		logger.debug("httpEnable : " + httpEnable);
		logger.debug("localIPAddress : " + localIPAddress);
		logger.debug("hostName : " + hostName);
		logger.debug("machineId : " + provisioningDetail.getMachineId());
		logger.debug("softwareId : " + provisioningDetail.getSoftwareId());
		logger.debug("softwareName : " + provisioningDetail.getSoftwareName());
		logger.debug("autoStart : " + provisioningDetail.getAutoStart());
		
		/**
		 * DataSource Variables
		 */
		String[] databaseType = provisioningDetail.getDatabaseType();
		String[] jndiName = provisioningDetail.getJndiName();
		String[] connectionUrl = provisioningDetail.getConnectionUrl();
		String[] userName = provisioningDetail.getUserName();
		String[] password = provisioningDetail.getPassword();
		String[] minPoolSize = provisioningDetail.getMinPoolSize();
		String[] maxPoolSize = provisioningDetail.getMaxPoolSize();

		logger.debug("databaseType : " + databaseType);
		logger.debug("jndiName : " + jndiName);
		logger.debug("connectionUrl : " + connectionUrl);
		logger.debug("userName : " + userName);
		logger.debug("password : " + password);
		logger.debug("minPoolSize : " + minPoolSize);
		logger.debug("maxPoolSize : " + maxPoolSize);
		
		Command command = new Command("JBoss INSTALL");
		int sequence = 0;
		ShellAction s_action = null;
		
		String[] fileNames = fileName.split(",");
		
		for (int i = 0; i < fileNames.length; i++) {
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("wget");
			s_action.addArguments("${RepositoryUrl}" + fileLocation + "/" + fileNames[i]);
			s_action.addArguments("-O");
			s_action.addArguments(fileNames[i]);
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setCommand("mkdir");
			s_action.addArguments("-p");
			s_action.addArguments(serverHome);
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory("/tmp");
			s_action.setCommand("unzip");
			s_action.addArguments("-o");
			s_action.addArguments(fileNames[i]);
			s_action.addArguments("-d");
			s_action.addArguments(serverHome);
			command.addAction(s_action);
		}
		
		// ${server.base}/codeServer11_cs/lib, ${server.base}/codeServer21_cs/lib 에는 ${jboss.home}/server/all/lib에 있는 라이브러리 파일을 복사해 넣는다.
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverHome);
		s_action.setCommand("sh");
		s_action.addArguments("eap_init.sh");
		s_action.addArguments(jbossHome + "/server/all/lib");
		s_action.addArguments(serverBase);
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverHome);
		s_action.setCommand("rm");
		s_action.addArguments("-f");
		s_action.addArguments("eap_init.sh");
		command.addAction(s_action);
		
		// Add JBoss INSTALL Command
		cmdMsg.addCommand(command);
		
		command = new Command("Set Configurations");
		sequence = 0;
		
		// env.sh 변환
		String envSh = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/jboss/conf/env.sh"), "UTF-8");
		envSh = envSh.replaceAll("\\$\\{user\\}", user)
					.replaceAll("\\$\\{java.home\\}", javaHome)
					.replaceAll("\\$\\{jboss.home\\}", jbossHome)
					.replaceAll("\\$\\{server.home\\}", serverHome)
					.replaceAll("\\$\\{server.base\\}", serverBase)
					.replaceAll("\\$\\{server.name\\}", serverName)
					.replaceAll("\\$\\{domain.ip\\}", domainIp)
					.replaceAll("\\$\\{encoding\\}", encoding)
					.replaceAll("\\$\\{bind.port\\}", bindPort)
					.replaceAll("\\$\\{udp.group.addr\\}", udpGroupAddr)
					.replaceAll("\\$\\{multicast.port\\}", multicastPort)
					.replaceAll("\\$\\{server.peer.id\\}", serverPeerID)
					.replaceAll("\\$\\{jvm.route\\}", jvmRoute)
					.replaceAll("\\$\\{heap.size\\}", heapSize)
					.replaceAll("\\$\\{permgen.size\\}", permgenSize);		
		
		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(serverBase + "/codeServer11/env.sh");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(serverBase + "/codeServer11_cs/env.sh");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(serverBase + "/codeServer21/env.sh");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(envSh);
		fw_action.setFileName(serverBase + "/codeServer21_cs/env.sh");
		command.addAction(fw_action);

		// Set server.xml
		String _fileName = "server-http.xml";
		String serverXml = null;
		
		if (httpEnable != null && httpEnable.toUpperCase().equals("N")) {
			_fileName = "server-none.xml";
		} else {
			_fileName = "server-http.xml";
		}
		
		serverXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/jboss/conf/" + _fileName), "UTF-8");
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(serverXml);
		fw_action.setFileName(serverBase + "/codeServer11/deploy/jbossweb.sar/server.xml");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(serverXml);
		fw_action.setFileName(serverBase + "/codeServer11_cs/deploy/jbossweb.sar/server.xml");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(serverXml);
		fw_action.setFileName(serverBase + "/codeServer21/deploy/jbossweb.sar/server.xml");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(serverXml);
		fw_action.setFileName(serverBase + "/codeServer21_cs/deploy/jbossweb.sar/server.xml");
		command.addAction(fw_action);
		
		// jboss_init 변환
		List<Property> properties = new ArrayList<Property>();
		Property property = null;
		
		property = new Property();
		property.setKey("user");
		property.setValue(user);
		properties.add(property);
		
		property = new Property();
		property.setKey("java.home");
		property.setValue(javaHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("jboss.home");
		property.setValue(jbossHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("server.home");
		property.setValue(serverHome);
		properties.add(property);
		
		property = new Property();
		property.setKey("server.base");
		property.setValue(serverBase);
		properties.add(property);
		
		property = new Property();
		property.setKey("server.name");
		property.setValue(serverName);
		properties.add(property);

		/** 
		 * sed -i 's/code/'${USER}'/g' /home/$USER/Servers/jboss_init
		 */
		ConfigAction c_action = new ConfigAction(sequence++);
		c_action.setFileName(serverBase + "/jboss_init");
		c_action.setProperties(properties);
		command.addAction(c_action);
		
		// Add Set Configurations Command
		cmdMsg.addCommand(command);
		
		command = new Command("DataSource Configurations");
		sequence = 0;
		
		// <local-tx-datasource />, <application-policy /> 생성
		StringBuilder localTxDatasource = new StringBuilder();
		StringBuilder applicationPolicy = new StringBuilder();
		String driverClassName = null;
		String exceptionSorter = null;
		String connectionChecker = null;
		String encPasswd = null;
		String query = null;
		if (databaseType != null) {
			for (int i = 0; i < databaseType.length; i++) {
				if (StringUtils.isNotEmpty(databaseType[i]) && StringUtils.isNotEmpty(jndiName[i]) && StringUtils.isNotEmpty(connectionUrl[i])
						 && StringUtils.isNotEmpty(userName[i]) && StringUtils.isNotEmpty(password[i])) {
					
					if (databaseType[i].toLowerCase().equals("oracle")) {
						driverClassName = "oracle.jdbc.driver.OracleDriver";
						exceptionSorter = "org.jboss.resource.adapter.jdbc.vendor.OracleExceptionSorter";
						connectionChecker = "org.jboss.resource.adapter.jdbc.vendor.OracleValidConnectionChecker";
						query = "SELECT 1 FROM DUAL";
					} else if (databaseType[i].toLowerCase().equals("mysql")) {
						driverClassName = "com.mysql.jdbc.Driver";
						exceptionSorter = "org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter";
						connectionChecker = "org.jboss.resource.adapter.jdbc.vendor.MySQLValidConnectionChecker";
						query = "SELECT 1";
					} else if (databaseType[i].toLowerCase().equals("mssql")) {
						driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
						query = "SELECT 1";
					} else if (databaseType[i].toLowerCase().equals("db2")) {
						driverClassName = "com.ibm.db2.jcc.DB2Driver";
						query = "VALUES 1";
					}
				
					localTxDatasource.append("	<local-tx-datasource>").append("\n");
					localTxDatasource.append("		<jndi-name>" + jndiName[i] + "</jndi-name>").append("\n");
					localTxDatasource.append("		<use-java-context>false</use-java-context>").append("\n");
					localTxDatasource.append("		<connection-url>" + connectionUrl[i] + "</connection-url>").append("\n");
					localTxDatasource.append("		<driver-class>" + driverClassName + "</driver-class>").append("\n");
					localTxDatasource.append("		<security-domain>" + jndiName[i] + "</security-domain>").append("\n");
					localTxDatasource.append("		<min-pool-size>" + minPoolSize[i] + "</min-pool-size>").append("\n");
					localTxDatasource.append("		<max-pool-size>" + maxPoolSize[i] + "</max-pool-size>").append("\n\n");
					localTxDatasource.append("		<!--").append("\n");
					if (connectionChecker == null) {
						localTxDatasource.append("		<valid-connection-checker-class-name/>").append("\n");
					} else {
						localTxDatasource.append("		<valid-connection-checker-class-name>" + connectionChecker + "</valid-connection-checker-class-name>").append("\n");
					}
					localTxDatasource.append("		-->").append("\n\n");
					localTxDatasource.append("		<!-- Checks the Oracle error codes and messages for fatal errors -->").append("\n");
					if (exceptionSorter == null) {
						localTxDatasource.append("		<exception-sorter-class-name/>").append("\n\n");
					} else {
						localTxDatasource.append("		<exception-sorter-class-name>" + exceptionSorter + "</exception-sorter-class-name>").append("\n\n");
					}
					localTxDatasource.append("		<new-connection-sql>" + query + "</new-connection-sql>").append("\n");
					localTxDatasource.append("		<check-valid-connection-sql>" + query + "</check-valid-connection-sql>").append("\n");
					localTxDatasource.append("	</local-tx-datasource>").append("\n");
		
					encPasswd = eapPasswordEncrypt(password[i]);
						
					applicationPolicy.append("  <application-policy name=\"" + jndiName[i] + "\">").append("\n");
					applicationPolicy.append("    <authentication>").append("\n");
					applicationPolicy.append("     <login-module code=\"org.jboss.resource.security.SecureIdentityLoginModule\" flag=\"required\">").append("\n");
					applicationPolicy.append("       <module-option name=\"username\">" + userName[i] + "</module-option>").append("\n");
					applicationPolicy.append("       <module-option name=\"password\">" + encPasswd + "</module-option>").append("\n");
					applicationPolicy.append("       <module-option name=\"managedConnectionFactoryName\">jboss.jca:name=" + jndiName[i] + ",service=LocalTxCM</module-option>").append("\n");
					applicationPolicy.append("     </login-module>").append("\n");
					applicationPolicy.append("    </authentication>").append("\n");
					applicationPolicy.append("  </application-policy>").append("\n");
				}
			}
		}

		String dsXml = null;
		if (StringUtils.isNotEmpty(localTxDatasource.toString())) {
			dsXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/jboss/conf/user-ds.xml"), "UTF-8");
			dsXml = dsXml.replaceAll("\\$\\{local.tx.datasource\\}", localTxDatasource.toString());
		} else {
			dsXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/jboss/conf/sample-ds.xml"), "UTF-8");
		}
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(dsXml);
		fw_action.setFileName(serverHome + "/apps/" + user + "-ds.xml");
		command.addAction(fw_action);

		String loginConfigXml = null;
		loginConfigXml = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/jboss/conf/login-config.xml"), "UTF-8");
		loginConfigXml = loginConfigXml.replaceAll("\\$\\{application.policy\\}", applicationPolicy.toString());
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(loginConfigXml);
		fw_action.setFileName(serverBase + "/codeServer11/conf/login-config.xml");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(loginConfigXml);
		fw_action.setFileName(serverBase + "/codeServer11_cs/conf/login-config.xml");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(loginConfigXml);
		fw_action.setFileName(serverBase + "/codeServer21/conf/login-config.xml");
		command.addAction(fw_action);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(loginConfigXml);
		fw_action.setFileName(serverBase + "/codeServer21_cs/conf/login-config.xml");
		command.addAction(fw_action);
		
		// Add Set DataSource Configurations
		cmdMsg.addCommand(command);
		
		command = new Command("Server Configurations");
		sequence = 0;
		
		// codeServerXX => ${user}ServerXX
		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverBase);
		s_action.setCommand("mv");
		s_action.addArguments("codeServer11");
		s_action.addArguments(user + "Server11");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverBase);
		s_action.setCommand("mv");
		s_action.addArguments("codeServer11_cs");
		s_action.addArguments(user + "Server11_cs");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverBase);
		s_action.setCommand("mv");
		s_action.addArguments("codeServer21");
		s_action.addArguments(user + "Server21");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory(serverBase);
		s_action.setCommand("mv");
		s_action.addArguments("codeServer21_cs");
		s_action.addArguments(user + "Server21_cs");
		command.addAction(s_action);
		
		if (baseTemplate.endsWith("Server11")) {
			if (!serverName.equals(user + "Server11")) {
				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory(serverBase);
				s_action.setCommand("mv");
				s_action.addArguments(user + "Server11");
				s_action.addArguments(serverName);
				command.addAction(s_action);
			}
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server11_cs");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server21");
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server21_cs");
			command.addAction(s_action);			
		} else if (baseTemplate.endsWith("Server11_cs")) {
			if (!serverName.equals(user + "Server11_cs")) {
				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory(serverBase);
				s_action.setCommand("mv");
				s_action.addArguments(user + "Server11_cs");
				s_action.addArguments(serverName);
				command.addAction(s_action);
			}
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server11");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server21");
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server21_cs");
			command.addAction(s_action);			
		} else if (baseTemplate.endsWith("Server21")) {
			if (!serverName.equals(user + "Server21")) {
				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory(serverBase);
				s_action.setCommand("mv");
				s_action.addArguments(user + "Server21");
				s_action.addArguments(serverName);
				command.addAction(s_action);
			}
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server11");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server11_cs");
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server21_cs");
			command.addAction(s_action);			
		} else if (baseTemplate.endsWith("Server21_cs")) {
			if (!serverName.equals(user + "Server11_cs")) {
				s_action = new ShellAction(sequence++);
				s_action.setWorkingDiretory(serverBase);
				s_action.setCommand("mv");
				s_action.addArguments(user + "Server21_cs");
				s_action.addArguments(serverName);
				command.addAction(s_action);
			}
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server11");
			command.addAction(s_action);
			
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server11_cs");
			command.addAction(s_action);

			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase);
			s_action.setCommand("rm");
			s_action.addArguments("-rf");
			s_action.addArguments(user + "Server21");
			command.addAction(s_action);			
		}

		// chonw -R ${user}:${group} /home/${user}
		s_action = new ShellAction(sequence++);
		s_action.setCommand("chown");
		s_action.addArguments("-R");
		s_action.addArguments(user + ":" + group);
		s_action.addArguments(serverHome);
		command.addAction(s_action);
		
		// Add JBoss INSTALL Command
		cmdMsg.addCommand(command);

		String startArgs = "-l " + user + " -c " + "'cd " + serverBase + "/" + serverName + " && sh startNode.sh notail'";
		String stopArgs = "-l " + user + " -c " + "'cd " + serverBase + "/" + serverName + " && sh kill.sh'";
		
		if (provisioningDetail.getAutoStart().equals("Y")) {
			command = new Command("Service Start");
			sequence = 0;
			s_action = new ShellAction(sequence++);
			s_action.setWorkingDiretory(serverBase + "/" + serverName);
			s_action.setCommand("runuser");
			s_action.addArguments(startArgs);
			command.addAction(s_action);
			
			cmdMsg.addCommand(command);
		}
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/		
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallLocation(jbossHome + "," + serverBase + ", " + serverHome + "/apps," + serverHome + "/svrlogs," + serverHome + "/wily");
		software.setInstallStat("INSTALLING");
		software.setServiceStopCmd("WORKING_DIR:" + serverBase + "/" + serverName + ",CMD:runuser,ARGS:" + stopArgs);
		software.setServiceStartCmd("WORKING_DIR:" + serverBase + "/" + serverName + ",CMD:runuser,ARGS:" + startArgs);
		software.setDescription("JBoss Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		ConfigDto config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverBase + "/" + serverName);
		config.setConfigFileName("env.sh");
		config.setConfigFileContents(envSh);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverHome + "/apps");
		config.setConfigFileName(user + "-ds.xml");
		config.setConfigFileContents(dsXml);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);
		
		config = new ConfigDto();
		config.setMachineId(provisioningDetail.getMachineId());
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setConfigFileLocation(serverBase + "/" + serverName + "/conf");
		config.setConfigFileName("login-config.xml");
		config.setConfigFileContents(loginConfigXml);
		config.setDeleteYn("N");
		config.setRegUserId(provisioningDetail.getUserId());
		config.setUpdUserId(provisioningDetail.getUserId());
		configList.add(config);

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList, null, null).start();
	}
	
	private void mysqlInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);

		String dataDir = (StringUtils.isEmpty(provisioningDetail.getDataDir()) ? "/var/lib/mysql" : provisioningDetail.getDataDir());
		String port = (StringUtils.isEmpty(provisioningDetail.getPort()) ? "3306" : provisioningDetail.getPort());
		String version = provisioningDetail.getVersion();
		String password = provisioningDetail.getPassword()[0];
		
		Command command = new Command("CONFIGURATION");
		int sequence = 0;
		
		String myCnf = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/mysql/" + version + "/my.cnf"), "UTF-8");
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

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList, null, null).start();
	}
	
	private void cephInstall(ProvisioningDetail provisioningDetail) throws Exception {
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(provisioningDetail.getMachineId());
		cmdMsg.setBlocking(true);
		
		/**
		 * Ceph Variables
		 */
		String fileLocation = provisioningDetail.getFileLocation();
		String fileName = provisioningDetail.getFileName();

		String fsid = UUID.randomUUID().toString();
		String journalSize = provisioningDetail.getJournalSize();
		String monNetworkInterface = provisioningDetail.getMonNetworkInterface();
		String pgNum = provisioningDetail.getPgNum();
		String pgpNum = provisioningDetail.getPgNum();
		String replicaSize = provisioningDetail.getReplicaSize();
		String publicNetwork = provisioningDetail.getPublicNetwork();
		String clusterNetwork = provisioningDetail.getClusterNetwork();
		String fileSystem = provisioningDetail.getFileSystem();
		String calamariServer = null;
		String ntpServer = provisioningDetail.getNtpServer();
		String[] hostname = provisioningDetail.getHostname();
		String[] ip = provisioningDetail.getIp();
		String[] type = provisioningDetail.getType();
		String[] userName = provisioningDetail.getUserName();
		String[] password = provisioningDetail.getPassword();
		String[] devicePaths = provisioningDetail.getDevicePaths();
		
		ClusterServer[] servers = null;
		if (hostname != null && hostname.length > 0) {
			servers = new ClusterServer[hostname.length];
			ClusterServer cs = null;
			for (int i = 0; i < hostname.length; i++) {
				cs = new ClusterServer();
				cs.setHostname(hostname[i]);
				cs.setIp(ip[i]);
				cs.setType(type[i]);
				cs.setUsername(userName[i]);
				cs.setPassword(password[i]);
				
				servers[i] = cs;
				
				if (type[i].equals("management")) {
					calamariServer = hostname[i];
				}
			}
		}

		logger.debug("fileLocation : " + fileLocation);
		logger.debug("fileName : " + fileName);
		logger.debug("fsid : " + fsid);
		logger.debug("journalSize : " + journalSize);
		logger.debug("monNetworkInterface : " + monNetworkInterface);
		logger.debug("pgNum : " + pgNum);
		logger.debug("pgpNum : " + pgpNum);
		logger.debug("replicaSize : " + replicaSize);
		logger.debug("publicNetwork : " + publicNetwork);
		logger.debug("clusterNetwork : " + clusterNetwork);
		logger.debug("fileSystem : " + fileSystem);
		logger.debug("calamariServer : " + calamariServer);
		logger.debug("ntpServer : " + ntpServer);
		logger.debug("hostname : " + Arrays.toString(hostname));
		logger.debug("ip : " + Arrays.toString(ip));
		logger.debug("type : " + Arrays.toString(type));
		logger.debug("userName : " + Arrays.toString(userName));
		logger.debug("password : " + Arrays.toString(password));
		logger.debug("devicePaths : " + Arrays.toString(devicePaths));
		logger.debug("machineId : " + provisioningDetail.getMachineId());
		logger.debug("softwareId : " + provisioningDetail.getSoftwareId());
		logger.debug("softwareName : " + provisioningDetail.getSoftwareName());

		Command command = new Command("Install RPM Packages");
		ShellAction s_action = null;
		int sequence = 0;

		s_action = new ShellAction(sequence++);
		s_action.setCommand("yum");
		s_action.addArguments("install");
		s_action.addArguments("-y");
		s_action.addArguments("epel-release");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setCommand("yum");
		s_action.addArguments("install");
		s_action.addArguments("-y");
		s_action.addArguments("wget");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setCommand("yum");
		s_action.addArguments("install");
		s_action.addArguments("-y");
		s_action.addArguments("ansible");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setCommand("yum");
		s_action.addArguments("install");
		s_action.addArguments("-y");
		s_action.addArguments("sshpass");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/etc/ansible");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/ansible.cfg");
		s_action.addArguments("-O");
		s_action.addArguments("ansible.cfg");
		command.addAction(s_action);
		
		// Add Install RPM Packages Command
		cmdMsg.addCommand(command);
		
		command = new Command("ceph-ansible INSTALL");
		sequence = 0;

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/opt");
		s_action.setCommand("wget");
		s_action.addArguments("${RepositoryUrl}" + fileLocation + "/" + fileName);
		s_action.addArguments("-O");
		s_action.addArguments(fileName);
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/opt");
		s_action.setCommand("rm");
		s_action.addArguments("-rf");
		s_action.addArguments("ceph-ansible");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setWorkingDiretory("/opt");
		s_action.setCommand("tar");
		s_action.addArguments("-xvzf");
		s_action.addArguments(fileName);
		command.addAction(s_action);
		
		// Add ceph-ansible INSTALL Command
		cmdMsg.addCommand(command);
		
		command = new Command("Set /opt/ceph-ansible/group_vars/all, osds");
		sequence = 0;
		
		String all = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/ceph/ceph-ansible/group_vars/all"), "UTF-8");
		all = all.replaceAll("\\$\\{fsid\\}", fsid)
					.replaceAll("\\$\\{journal_size\\}", journalSize)
					.replaceAll("\\$\\{pool_default_pg_num\\}", pgNum)
					.replaceAll("\\$\\{pool_default_pgp_num\\}", pgpNum)
					.replaceAll("\\$\\{pool_default_size\\}", replicaSize)
					.replaceAll("\\$\\{public_network\\}", publicNetwork)
					.replaceAll("\\$\\{filesystem\\}", fileSystem)
					.replaceAll("\\$\\{calamari_server\\}", calamariServer)
					.replaceAll("\\$\\{ntp-server\\}", ntpServer);

		FileWriteAction fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(all);
		fw_action.setFileName("/opt/ceph-ansible/group_vars/all");
		command.addAction(fw_action);
		
		String osd = IOUtils.toString(new URL(provisioningDetail.getUrlPrefix() + "/repo/ceph/ceph-ansible/group_vars/osds"), "UTF-8");
		
		StringBuilder sb = new StringBuilder();
		String path = null;
		for (int i = 0; i < devicePaths.length; i++) {
			path = devicePaths[i];
			
			if (i > 0) {
				sb.append("\n  - ");
			}
			
			sb.append(path);
		}
		
		osd = osd.replaceAll("\\$\\{path_of_devices\\}", sb.toString());

		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(osd);
		fw_action.setFileName("/opt/ceph-ansible/group_vars/osds");
		command.addAction(fw_action);
		
		// Add Set /opt/ceph-ansible/group_vars/all, osds Command
		cmdMsg.addCommand(command);
		
		command = new Command("Set /etc/ansible/hosts");
		sequence = 0;
		
		StringBuilder rgws = new StringBuilder("[rgws]").append("\n");
		StringBuilder restapi = new StringBuilder("[restapi]").append("\n");
		StringBuilder calamari = new StringBuilder("[calamari]").append("\n");
		StringBuilder saltServer = new StringBuilder("[salt-server]").append("\n");
		StringBuilder mons = new StringBuilder("[mons]").append("\n");
		StringBuilder osds = new StringBuilder("[osds]").append("\n");
		StringBuilder saltClient = new StringBuilder("[salt-client]").append("\n");
		StringBuilder ntpClient = new StringBuilder("[ntp-client]").append("\n");
		
		for (ClusterServer server : servers) {
			if (server.getType().equals("radosgw")) {
				rgws.append(server.getHostname()).append("\n");
				ntpClient.append(server.getHostname()).append("\n");
			} else if (server.getType().equals("management")) {
				restapi.append(server.getHostname()).append("\n");
				calamari.append(server.getHostname()).append("\n");
				saltServer.append(server.getHostname()).append("\n");
				ntpClient.append(server.getHostname()).append("\n");
			} else if (server.getType().equals("mon")) {
				mons.append(server.getHostname()).append("\n");
				saltClient.append(server.getHostname()).append("\n");
				ntpClient.append(server.getHostname()).append("\n");
			} else if (server.getType().equals("osd")) {
				osds.append(server.getHostname()).append("\n");
				saltClient.append(server.getHostname()).append("\n");
				ntpClient.append(server.getHostname()).append("\n");
			} 
		}

		sb = new StringBuilder();
		sb.append(rgws).append("\n");
		sb.append(restapi).append("\n");
		sb.append(calamari).append("\n");
		sb.append(saltServer).append("\n");
		sb.append(mons).append("\n");
		sb.append(osds).append("\n");
		sb.append(saltClient).append("\n");
		sb.append(ntpClient);
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(sb.toString());
		fw_action.setFileName("/etc/ansible/hosts");
		command.addAction(fw_action);
		
		// Add Set /etc/ansible/hosts Command
		cmdMsg.addCommand(command);

		command = new Command("Set script & run");
		sequence = 0;

		StringBuilder script = new StringBuilder("#!/bin/bash").append("\n");
		script.append("echo -e \"" + ntpServer + "\\tntp-server\" >> /etc/hosts").append("\n");
		
		for (ClusterServer server : servers) {
			script.append("echo -e \"" + server.getIp() + "\\t" + server.getHostname() + "\" >> /etc/hosts").append("\n");
		}

		script.append("rm -f ~/.ssh/id_rsa*").append("\n");
		script.append("ssh-keygen -t rsa -f ~/.ssh/id_rsa -q -N \"\"");
		
		fw_action = new FileWriteAction(sequence++);
		fw_action.setContents(script.toString());
		fw_action.setFileName("/tmp/ceph-init.sh");
		command.addAction(fw_action);

		s_action = new ShellAction(sequence++);
		s_action.setCommand("chmod");
		s_action.addArguments("+x");
		s_action.addArguments("/tmp/ceph-init.sh");
		command.addAction(s_action);

		s_action = new ShellAction(sequence++);
		s_action.setCommand("sh");
		s_action.addArguments("/tmp/ceph-init.sh");
		command.addAction(s_action);
		
		// Add Set script & run Command
		cmdMsg.addCommand(command);
		
		command = new Command("Copy ssh key files and /etc/hosts");
		sequence = 0;
		
		for (ClusterServer server : servers) {
			//sshpass -p{password} ssh -o StrictHostKeyChecking=no {username}@{hostname} "mkdir -p ~/.ssh"
			s_action = new ShellAction(sequence++);
			s_action.setCommand("sshpass");
			s_action.addArguments("-p" + server.getPassword());
			s_action.addArguments("ssh");
			s_action.addArguments("-o");
			s_action.addArguments("StrictHostKeyChecking=no");
			s_action.addArguments(server.getUsername() + "@" + server.getHostname());
			s_action.addArguments("\"mkdir -p ~/.ssh\"");
			command.addAction(s_action);

			//sshpass -p{password} ssh -o StrictHostKeyChecking=no {username}@{hostname} "chmod 700 ~/.ssh"
			s_action = new ShellAction(sequence++);
			s_action.setCommand("sshpass");
			s_action.addArguments("-p" + server.getPassword());
			s_action.addArguments("ssh");
			s_action.addArguments("-o");
			s_action.addArguments("StrictHostKeyChecking=no");
			s_action.addArguments(server.getUsername() + "@" + server.getHostname());
			s_action.addArguments("\"chmod 700 ~/.ssh\"");
			command.addAction(s_action);
			
			//sshpass -p{password} scp -o StrictHostKeyChecking=no ~/.ssh/id_rsa.pub {username}@{hostname}:~/.ssh/authorized_keys
			s_action = new ShellAction(sequence++);
			s_action.setCommand("sshpass");
			s_action.addArguments("-p" + server.getPassword());
			s_action.addArguments("scp");
			s_action.addArguments("-o");
			s_action.addArguments("StrictHostKeyChecking=no");
			s_action.addArguments("~/.ssh/id_rsa.pub");
			s_action.addArguments(server.getUsername() + "@" + server.getHostname() + ":~/.ssh/authorized_keys");
			command.addAction(s_action);

			//sshpass -p{password} ssh -o StrictHostKeyChecking=no {username}@{hostname} "chmod 600 ~/.ssh/authorized_keys"
			s_action = new ShellAction(sequence++);
			s_action.setCommand("sshpass");
			s_action.addArguments("-p" + server.getPassword());
			s_action.addArguments("ssh");
			s_action.addArguments("-o");
			s_action.addArguments("StrictHostKeyChecking=no");
			s_action.addArguments(server.getUsername() + "@" + server.getHostname());
			s_action.addArguments("\"chmod 600 ~/.ssh/authorized_keys\"");
			command.addAction(s_action);
			
			//sshpass -p{password} scp -o StrictHostKeyChecking=no ~/.ssh/id_rsa* {username}@{hostname}:~/.ssh/
			s_action = new ShellAction(sequence++);
			s_action.setCommand("sshpass");
			s_action.addArguments("-p" + server.getPassword());
			s_action.addArguments("scp");
			s_action.addArguments("-o");
			s_action.addArguments("StrictHostKeyChecking=no");
			s_action.addArguments("/etc/hosts");
			s_action.addArguments(server.getUsername() + "@" + server.getHostname() + ":/etc/hosts");
			command.addAction(s_action);
		}
		
		// Add Make and copy ssh key files, copy /etc/hosts Command
		cmdMsg.addCommand(command);
		
		command = new Command("Run ansible-playbook");
		sequence = 0;
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("ansible-playbook");
		s_action.addArguments("/opt/ceph-ansible/site.yml");
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("ceph");
		s_action.addArguments("status");
		command.addAction(s_action);

		// Add Run ansible-playbook Command
		cmdMsg.addCommand(command);
		
		command = new Command("Add user for object storage");
		sequence = 0;
		
		// radosgw-admin user create --uid=osc --display-name="osc"
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("user");
		s_action.addArguments("create");
		s_action.addArguments("--uid=osc");
		s_action.addArguments("--display-name=\"osc\"");
		command.addAction(s_action);

		// # radosgw-admin caps add --uid=osc --caps="usage=read, write"
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("caps");
		s_action.addArguments("add");
		s_action.addArguments("--uid=osc");
		s_action.addArguments("--caps=\"usage=read, write\"");
		command.addAction(s_action);

		// # radosgw-admin caps add --uid=osc --caps="buckets=read, write"
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("caps");
		s_action.addArguments("add");
		s_action.addArguments("--uid=osc");
		s_action.addArguments("--caps=\"buckets=read, write\"");
		command.addAction(s_action);

		// # radosgw-admin caps add --uid=osc --caps="users=read, write"
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("caps");
		s_action.addArguments("add");
		s_action.addArguments("--uid=osc");
		s_action.addArguments("--caps=\"users=read, write\"");
		command.addAction(s_action);

		// # radosgw-admin caps add --uid=osc --caps="zone=read, write"
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("caps");
		s_action.addArguments("add");
		s_action.addArguments("--uid=osc");
		s_action.addArguments("--caps=\"zone=read, write\"");
		command.addAction(s_action);

		// # radosgw-admin caps add --uid=osc --caps="metadata=read, write"
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("caps");
		s_action.addArguments("add");
		s_action.addArguments("--uid=osc");
		s_action.addArguments("--caps=\"metadata=read, write\"");
		command.addAction(s_action);

		// Add user for object storage Command
		cmdMsg.addCommand(command);
		
		command = new Command("Get S3 Credeitials");
		sequence = 0;
		
		// radosgw-admin user info --uid=osc
		s_action = new ShellAction(sequence++);
		s_action.setCommand("radosgw-admin");
		s_action.addArguments("user");
		s_action.addArguments("info");
		s_action.addArguments("--uid=osc");
		command.addAction(s_action);

		// Add Get S3 Credeitials Command
		cmdMsg.addCommand(command);
		
		/***************************************************************
		 *  software_tbl에 소프트웨어 설치 정보 및 config_tbl에 설정파일 정보 추가
		 ***************************************************************/
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallStat("INSTALLING");
		software.setDescription("Ceph Provisioning");
		software.setDeleteYn("N");
		software.setRegUserId(provisioningDetail.getUserId());
		software.setUpdUserId(provisioningDetail.getUserId());
		
		List<ConfigDto> configList = new ArrayList<ConfigDto>();
		
		CephDto cephDto = new CephDto();
		cephDto.setMachineId(provisioningDetail.getMachineId());
		
		for (ClusterServer server : servers) {
			if (server.getType().equals("radosgw")) {
				cephDto.setRadosgwApiPrefix("http://" + server.getIp());
			} else if (server.getType().equals("management")) {
				cephDto.setMgmtHost(server.getIp());
				cephDto.setMgmtPort("22");
				cephDto.setMgmtUsername(server.getUsername());
				cephDto.setMgmtPassword(server.getPassword());
				cephDto.setMgmtApiPrefix("http://" + server.getIp() + ":5000/api/v0.1");
				
				cephDto.setCalamariApiPrefix("http://" + server.getIp() + "/api/v2");
				cephDto.setCalamariUsername("root");
				cephDto.setCalamariPassword("calamaripw");
			}
		}
		
		cephDto.setRegUserId(provisioningDetail.getUserId());
		cephDto.setUpdUserId(provisioningDetail.getUserId());

		new InstallThread(peacockTransmitter, softwareService, cmdMsg, software, configList, cephService, cephDto).start();
	}
	
	private void httpdRemove(ProvisioningDetail provisioningDetail) throws Exception {
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallSeq(provisioningDetail.getInstallSeq());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setInstallSeq(software.getInstallSeq());
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
		
		String stopCmd = software.getServiceStopCmd();
		
		String workingDir = null;
		String cmd = null;
		String args = null;
		
		/**
		 * Stop Service
		 */
		if (stopCmd.split(",")[0].split(":").length == 2) {
			workingDir = stopCmd.split(",")[0].split(":")[1];
		}
		if (stopCmd.split(",")[1].split(":").length == 2) {
			cmd = stopCmd.split(",")[1].split(":")[1];
		}
		if (stopCmd.split(",")[2].split(":").length == 2) {
			args = stopCmd.split(",")[2].split(":")[1];
		}
		
		ShellAction s_action = new ShellAction(sequence++);
		if (workingDir != null) {
			s_action.setWorkingDiretory("");
		}
		s_action.setCommand(cmd);
		if (args != null) {
			s_action.addArguments(args);
		}
		command.addAction(s_action);
		
		s_action = new ShellAction(sequence++);
		s_action.setCommand("rm");
		s_action.addArguments("-f");
		s_action.addArguments("/etc/init.d/" + args.split(" ")[0]);
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
		software.setInstallSeq(provisioningDetail.getInstallSeq());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setInstallSeq(software.getInstallSeq());
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
		s_action.addArguments("jbossews");
		s_action.addArguments("stop");
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
		software.setInstallSeq(provisioningDetail.getInstallSeq());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setInstallSeq(software.getInstallSeq());
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
	
	private void mysqlRemove(ProvisioningDetail provisioningDetail) throws Exception {
		SoftwareDto software = new SoftwareDto();
		software.setSoftwareId(provisioningDetail.getSoftwareId());
		software.setMachineId(provisioningDetail.getMachineId());
		software.setInstallSeq(provisioningDetail.getInstallSeq());
		
		software = softwareService.getSoftware(software);
		software.setUpdUserId(provisioningDetail.getUserId());
		
		ConfigDto config = new ConfigDto();
		config.setSoftwareId(provisioningDetail.getSoftwareId());
		config.setMachineId(provisioningDetail.getMachineId());
		config.setInstallSeq(software.getInstallSeq());
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
	
	private String ewsPasswordEncrypt(String password) {
		return BlowfishEncrypter.encrypt(password);
	}
	
	private String eapPasswordEncrypt(String password) throws Exception {
		SecureIdentityLoginModule module = new SecureIdentityLoginModule();
		
		Method method = module.getClass().getDeclaredMethod("encode", String.class);
		method.setAccessible(true);
		
		return (String) method.invoke(module, password);
	}
}
//end of ProvisioningHandler.java

class InstallThread extends Thread {

    protected final Logger logger = LoggerFactory.getLogger(InstallThread.class);

	private PeacockTransmitter peacockTransmitter;
	private SoftwareService softwareService;
	private ProvisioningCommandMessage cmdMsg;
	private SoftwareDto software;
	private List<ConfigDto> configList;
	private CephService cephService;
	private CephDto cephDto;
	
	public InstallThread(PeacockTransmitter peacockTransmitter, SoftwareService softwareService,
			ProvisioningCommandMessage cmdMsg, SoftwareDto software, List<ConfigDto> configList,
			CephService cephService, CephDto cephDto) {
		this.peacockTransmitter = peacockTransmitter;
		this.softwareService = softwareService;
		this.cmdMsg = cmdMsg;
		this.software = software;
		this.configList = configList;
		this.cephService = cephService;
		this.cephDto = cephDto;
	}
	
	@Override
	public void run() {
		try {
			softwareService.insertSoftware(software);
			
			PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
			ProvisioningResponseMessage response = peacockTransmitter.sendMessage(datagram);
			
			logger.debug("[{}] Installed.", software.getSoftwareName());
			
			StringBuilder sb = new StringBuilder("");
			List<String> commands = response.getCommands();
			List<String> results = response.getResults();
			
			for (int i = 0; i < results.size(); i++) {
				if (i < commands.size()) {
					if (commands.get(i) != null) {
						sb.append("[Command] : " + commands.get(i) + "\n");
						
						if (commands.get(i).equals("Get S3 Credeitials")) {
							try {
								ObjectMapper mapper = new ObjectMapper();
								JsonNode node = mapper.readTree(results.get(i));
								JsonNode keys = node.get("keys");
								
								String accessKey = null;
								String secretKey = null;
								if (keys.isArray()) {
									for (JsonNode key : (ArrayNode) keys) {
										if (key.get("user").asText().equals("osc")) {
											accessKey = key.get("access_key").asText();
											secretKey = key.get("secret_key").asText();
										}
									}
								} else {
									if (keys.get("user").asText().equals("osc")) {
										accessKey = keys.get("access_key").asText();
										secretKey = keys.get("secret_key").asText();
									}
								}
								
								cephDto.setS3AccessKey(accessKey);
								cephDto.setS3SecretKey(secretKey);
							} catch (Exception e) {
								logger.error("Unhandled Exception has occurred during parsing S3 credentials.", e);
							}
						}
					}
				}

				sb.append(results.get(i) + "\n");
			}
			
			software.setInstallStat("COMPLETED");
			software.setInstallLog(sb.toString());

			logger.debug("Install Log : [{}]", sb.toString());
			
			softwareService.insertSoftware(software, configList);
			
			if (cephService != null && cephDto != null) {
				cephService.insertCeph(cephDto);
			}
		} catch (Exception e) {
			software.setInstallStat("INST_ERROR");
			software.setInstallLog(e.getMessage());
			
			try {
				softwareService.insertSoftware(software);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			logger.error("Unhandled Exception has occurred.", e);
		}
	}
}

class UninstallThread extends Thread {

    protected final Logger logger = LoggerFactory.getLogger(UninstallThread.class);

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
			
			logger.debug("[{}] Uninstalled.", software.getSoftwareName());
			
			configService.deleteConfig(config);
			
			StringBuilder sb = new StringBuilder("");
			List<String> commands = response.getCommands();
			List<String> results = response.getResults();
			
			for (int i = 0; i < results.size(); i++) {
				if (commands.get(i) != null) {
					sb.append("[Command] : " + commands.get(i) + "\n");
				}

				sb.append(results.get(i) + "\n");
			}
			software.setDeleteYn("Y");
			software.setInstallStat("DELETED");
			software.setInstallLog(sb.toString());

			logger.debug("Uninstall Log : [{}]", sb.toString());
			
			softwareService.updateSoftware(software);
		} catch (Exception e) {
			software.setInstallStat("UNINST_ERROR");
			software.setInstallLog(e.getMessage());
			
			try {
				softwareService.updateSoftware(software);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			logger.error("Unhandled Exception has occurred.", e);
		}
	}
}