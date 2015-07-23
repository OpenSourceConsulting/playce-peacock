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
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.libvirt.NodeInfo;
import org.libvirt.StoragePool;
import org.libvirt.StorageVol;

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
	
	public void getStoreagePoolInfo() throws LibvirtException {
		if (connect == null) {
			connect();
		}
		
		StoragePool pool = null;
		StorageVol vol = null;
		for (String poolName : connect.listStoragePools()) {
			pool = connect.storagePoolLookupByName(poolName);
			
			System.out.println("StoragePool's name : " + pool.getName() + ", numOfVolumes : " + pool.numOfVolumes());
			System.out.println(pool.getXMLDesc(0));
			
			int cnt = 1;
			for (String volName : pool.listVolumes()) {
				vol = pool.storageVolLookupByName(volName);
				
				System.out.println("\t[" + cnt++ + "] \tname : " + vol.getName() + "\n\t\tpath : " + vol.getPath());
			}
		}
	}
	
	public void addStoragePool(String poolName) throws LibvirtException {
		if (connect == null) {
			connect();
		}
		
		// virsh # pool-create-as test dir --target /test
		String poolXml = 	"<pool type='dir'>" +
							"  <name>" + poolName + "</name>" +
							"  <target>" +
							"    <path>/test</path>" +
							"  </target>" +
							"</pool>";
		
		StoragePool pool = connect.storagePoolCreateXML(poolXml, 0);

		System.out.println("StoragePool's name : " + pool.getName() + ", numOfVolumes : " + pool.numOfVolumes());
		System.out.println(pool.getXMLDesc(0));
	}
	
	public void addStorageVol(String poolName, String volName) throws LibvirtException {
		if (connect == null) {
			connect();
		}
		
		StoragePool pool = connect.storagePoolLookupByName(poolName);
		
		// virsh # vol-create-as test-pool test.img 104857600 --format raw
		// virsh # vol-info test.img --pool test-pool
		String vollXml = 	"<volume>" +
							"  <name>" + volName + "</name>" +
							"  <capacity unit='bytes'>104857600</capacity>" +
							//"  <allocation unit='bytes'>104857600</allocation>" +
							"  <target>" +
							"    <format type='raw'/>" +
							"  </target>" +
							"</volume>";	
		
		StorageVol vol = pool.storageVolCreateXML(vollXml, 0);
		System.out.println("name : " + vol.getName() + ", path : " + vol.getPath());
		System.out.println(vol.getXMLDesc(0));
	}
	
	public void deleteStorageVol(String poolName, String name) throws LibvirtException {
		if (connect == null) {
			connect();
		}
		
		StoragePool pool = null;
		StorageVol vol = null;
		
		pool = connect.storagePoolLookupByName(poolName);
		
		for (String volName : pool.listVolumes()) {
			if (volName.equals(name)) {
				vol = pool.storageVolLookupByName(volName);

				// virsh # vol-delete test.img --pool test-pool
				vol.delete(0);
				break;
			}
		}
	}
	
	public void deleteStoragePool(String name) throws LibvirtException {
		if (connect == null) {
			connect();
		}

		StoragePool pool = null;
		for (String poolName : connect.listStoragePools()) {
			if (poolName.equals(name)) {
				pool = connect.storagePoolLookupByName(poolName);

				// virsh # pool-destroy test
				pool.destroy();
				break;
			}
		}
	}

	public void getDomainInfo() throws LibvirtException {
		if (connect == null) {
			connect();
		}

        Domain domain = null;
        
        System.out.println("================================[Active Domain]================================");
        for (int id : connect.listDomains()) {
        	domain = connect.domainLookupByID(id);

        	//*
			System.out.println("domain name : " + domain.getName() + ", type : " + domain.getOSType() + ", max_mem : " + domain.getMaxMemory() + ", max_cpu : " + domain.getMaxVcpus());
			/*/
            System.out.println("virDomainGetXMLDesc:" + domain.getXMLDesc(0));
            System.out.println("virDomainGetAutostart:" + domain.getAutostart());
            System.out.println("virDomainGetConnect:" + domain.getConnect());
            System.out.println("virDomainGetID:" + domain.getID());
            System.out.println("virDomainGetInfo:" + domain.getInfo());
            System.out.println("virDomainGetMaxMemory:" + domain.getMaxMemory());
            System.out.println("virDomainGetMaxVcpus:" + domain.getMaxVcpus());
            System.out.println("virDomainGetName:" + domain.getName());
            System.out.println("virDomainGetOSType:" + domain.getOSType());
            System.out.println("virDomainGetSchedulerType:" + domain.getSchedulerType());
            System.out.println("virDomainGetSchedulerParameters:" + domain.getSchedulerParameters());
            //*/
        }	
        
        System.out.println("================================[Inactive Domain]================================");
        for (String name : connect.listDefinedDomains()) {
        	domain = connect.domainLookupByName(name);

			System.out.println("domain name : " + domain.getName() + ", type : " + domain.getOSType());
        }	
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
		test.getStoreagePoolInfo();
		
		test.addStoragePool("test-pool");
		test.addStorageVol("test-pool", "test.img");
		test.deleteStorageVol("test-pool", "test.img");
		test.deleteStoragePool("test-pool");
		
		test.getDomainInfo();
	}
}
//end of LibvirtTest.java