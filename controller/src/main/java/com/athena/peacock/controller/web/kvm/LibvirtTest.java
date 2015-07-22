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
 * Sang-cheon Park	2015. 7. 22.		First Draft.
 */
package com.athena.peacock.controller.web.kvm;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.libvirt.NodeInfo;

/**
 * <pre>
 * http://www.doublecloud.org/2013/06/managing-kvm-with-libvirt-in-java-step-by-step-tutorial/
 * http://stackoverflow.com/questions/13136884/how-to-build-and-install-libvirt-on-mac
 * https://github.com/andreaturli/libvirt.git
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class LibvirtTest {
	
	private static final String KVM_QEMU_URL = "qemu+tcp://192.168.0.240/system";
	private Connect connect;
	
	static {
		/**
		 * src/main/resources에 등록할 경우 filtering 되면서 바이너리 파일이 변경됨.
		 * 따라서 src/main/java에 라이브러리를 등록.
		 */
		String libvirtPath = KVMManager.class.getResource("/com/athena/peacock/controller/web/kvm/lib").getFile();
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
	}
	
	public void connect() throws LibvirtException {
		try {
			connect = new Connect(KVM_QEMU_URL, new PeacockKvmAuth("root", "jan01jan"), 0);
		} catch (LibvirtException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void getSystemInfo() throws LibvirtException {
		if (connect == null) {
			connect();
		}

        NodeInfo nodeInfo = connect.nodeInfo();
        System.out.println("virNodeInfo.model:" + nodeInfo.model);
        System.out.println("virNodeInfo.memory:" + nodeInfo.memory);
        System.out.println("virNodeInfo.cpus:" + nodeInfo.cpus);
        System.out.println("virNodeInfo.nodes:" + nodeInfo.nodes);
        System.out.println("virNodeInfo.sockets:" + nodeInfo.sockets);
        System.out.println("virNodeInfo.cores:" + nodeInfo.cores);
        System.out.println("virNodeInfo.threads:" + nodeInfo.threads);

        System.out.println("getHostName:" + connect.getHostName());
        System.out.println("getCapabilities:" + connect.getCapabilities());
        System.out.println("getType:" + connect.getType());
        System.out.println("getURI:" + connect.getURI());
        System.out.println("getVersion:" + connect.getVersion());
        System.out.println("getLibVirVersion:" + connect.getLibVirVersion());
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 * @throws LibvirtException 
	 */
	public static void main(String[] args) throws LibvirtException {
		LibvirtTest test = new LibvirtTest();
		test.getSystemInfo();
	}

}
//end of LibvirtTest.java