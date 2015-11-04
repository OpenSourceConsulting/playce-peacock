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
 * Sang-cheon Park	2015. 8. 16.		First Draft.
 */
package com.athena.peacock.controller.common.component;

import org.apache.commons.lang.StringUtils;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.peacock.controller.web.kvm.LibvirtTest;
import com.athena.peacock.controller.web.kvm.PeacockKvmAuth;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class KVMVirshClient extends HypervisorClient {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(KVMVirshClient.class);
    
    private String url;
	private Connect connect;

	public KVMVirshClient(String protocol, String host, String domain, String port, String username, String password) {
		this.protocol = protocol;
		this.host = host;
		this.domain = domain;
		this.port = port;
		this.username = username;
		this.password = password;
		
		try {
			init();
		} catch (Exception e) {
			// ignore
		}
	}
	
	public void init() throws Exception {
		/**
		 * src/main/resources에 등록할 경우 filtering 되면서 바이너리 파일이 변경됨.
		 * 따라서 src/main/java에 라이브러리를 등록.
		 */
		String libvirtPath = LibvirtTest.class.getResource("/com/athena/peacock/controller/web/kvm/lib").getFile();
		System.setProperty("jna.library.path", libvirtPath);
		
		/**
		 * certificate file locations
		 * 
		 * MAC OS X : /usr/local/etc/pki/CA/cacert.pem, /usr/local/etc/pki/libvirt/clientcert.pem, /usr/local/etc/pki/libvirt/private/clientkey.pem
		 * LINUX : /etc/pki/CA/cacert.pem, /etc/pki/libvirt/clientcert.pem, /etc/pki/libvirt/private/clientkey.pem
		 * Windows : C:\Users\${user}\AppData\Roaming\libvirt\pki\CA\cacert.pem, 
		 *           C:\Users\${user}\AppData\Roaming\libvirt\pki\libvirt\clientcert.pem, 
		 *           C:\Users\${user}\AppData\Roaming\libvirt\pki\libvirt\private\clientkey.pem
		 */
		url = "qemu+tcp://" + host;
		
		if (StringUtils.isNotEmpty(port)) {
			url += ":" + port;
		}
		
		url += "/system";
	}
	
	public Connect getConnect() throws LibvirtException {
		try {
			if (connect == null) {
				connect = new Connect(url, new PeacockKvmAuth(username, password), 0);
			}
		} catch (LibvirtException e) {
			LOGGER.error("LibvirtException has occurred.", e);
			throw e;
		}
		
		return connect;
	}
}
//end of KVMVirshClient.java