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
 * Sang-cheon Park	2015. 7. 15.		First Draft.
 */
package com.athena.peacock.controller.web.kvm;

import org.libvirt.Connect;
import org.libvirt.ConnectAuth;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.libvirt.NodeInfo;
import org.libvirt.SchedBooleanParameter;
import org.libvirt.SchedDoubleParameter;
import org.libvirt.SchedIntParameter;
import org.libvirt.SchedLongParameter;
import org.libvirt.SchedParameter;
import org.libvirt.SchedUintParameter;
import org.libvirt.SchedUlongParameter;
import org.libvirt.StoragePool;
import org.libvirt.StorageVol;

/**
 * <pre>
 * http://www.doublecloud.org/2013/06/managing-kvm-with-libvirt-in-java-step-by-step-tutorial/
 * http://stackoverflow.com/questions/13136884/how-to-build-and-install-libvirt-on-mac
 * https://github.com/andreaturli/libvirt.git
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class KVMManager {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * src/main/resources에 등록할 경우 filtering 되면서 바이너리 파일이 변경됨.
		 * 따라서 src/main/java에 라이브러리를 등록.
		 */
		//String libvirtPath = KVMManager.class.getResource("/libvirt/lib").getFile();
		String libvirtPath = KVMManager.class.getResource("/com/athena/peacock/controller/web/kvm/lib").getFile();
		System.out.println("libvirtPath => " + libvirtPath);
		
		System.setProperty("jna.library.path", libvirtPath);
		
		/**
		 * certificate file locations
		 * 
		 * MAC OS X : /usr/local/etc/pki/CA/cacert.pem, /usr/local/etc/pki/libvirt/clientcert.pem, /usr/local/etc/pki/libvirt/private/clientkey.pem
		 * LINUX : /etc/pki/CA/cacert.pem, /etc/pki/libvirt/clientcert.pem, /etc/pki/libvirt/private/clientkey.pem
		 * Windows : C:\Users\${user}\AppData\Roaming\libvirt\pki\CA\cacert.pem, C:\Users\${user}\AppData\Roaming\libvirt\pki\libvirt\clientcert.pem, C:\Users\${user}\AppData\Roaming\libvirt\pki\libvirt\private\clientkey.pem
		 */

		try {
			//ConnectAuth ca = new org.libvirt.ConnectAuthDefault();
			ConnectAuth ca = new PeacockKvmAuth("root", "jan01jan");
			
			//Connect conn = new Connect("qemu://192.168.0.230/system?socket=/var/run/libvirt/libvirt-sock", ca, 0);
			Connect conn = new Connect("qemu+tcp://192.168.0.240/system", ca, 0);
			//Connect conn = new Connect("qemu://192.168.0.230/system");
			NodeInfo ni = conn.nodeInfo();

			System.out.println("model: " + ni.model + " mem(kb):" + ni.memory);

			int[] domainIds = conn.listDomains();
			
			Domain vm = null;
			for (int id : domainIds) {
				vm = conn.domainLookupByID(id);
				System.out.println("vm name: " + vm.getName() + "  type: "
						+ vm.getOSType() + " max mem: " + vm.getMaxMemory()
						+ " max cpu: " + vm.getMaxVcpus());
			}
			
			//String cap = conn.getCapabilities();
			//System.out.println("cap: " + cap);
			
			// Check nodeinfo
            NodeInfo nodeInfo = conn.nodeInfo();
            System.out.println("virNodeInfo.model:" + nodeInfo.model);
            System.out.println("virNodeInfo.memory:" + nodeInfo.memory);
            System.out.println("virNodeInfo.cpus:" + nodeInfo.cpus);
            System.out.println("virNodeInfo.nodes:" + nodeInfo.nodes);
            System.out.println("virNodeInfo.sockets:" + nodeInfo.sockets);
            System.out.println("virNodeInfo.cores:" + nodeInfo.cores);
            System.out.println("virNodeInfo.threads:" + nodeInfo.threads);

            // Exercise the information getter methods
            System.out.println("getHostName:" + conn.getHostName());
            System.out.println("getCapabilities:" + conn.getCapabilities());
            //System.out.println("getMaxVcpus:" + conn.getMaxVcpus("xen"));
            System.out.println("getType:" + conn.getType());
            System.out.println("getURI:" + conn.getURI());
            System.out.println("getVersion:" + conn.getVersion());
            System.out.println("getLibVirVersion:" + conn.getLibVirVersion());

            // By default, there are 1 created and 0 defined networks

            /*
            // Create a new network to test the create method
            System.out.println("conn.networkCreateXML: "
                    + conn.networkCreateXML("<network>" + "  <name>createst</name>"
                            + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e68</uuid>" + "  <bridge name='createst'/>"
                            + "  <forward dev='eth0'/>" + "  <ip address='192.168.66.1' netmask='255.255.255.0'>"
                            + "    <dhcp>" + "      <range start='192.168.66.128' end='192.168.66.253'/>"
                            + "    </dhcp>" + "  </ip>" + "</network>"));

            // Same for the define method
            System.out.println("conn.networkDefineXML: "
                    + conn.networkDefineXML("<network>" + "  <name>deftest</name>"
                            + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e67</uuid>" + "  <bridge name='deftest'/>"
                            + "  <forward dev='eth0'/>" + "  <ip address='192.168.88.1' netmask='255.255.255.0'>"
                            + "    <dhcp>" + "      <range start='192.168.88.128' end='192.168.88.253'/>"
                            + "    </dhcp>" + "  </ip>" + "</network>"));
            //*/

            // We should have 2:1 but it shows up 3:0 hopefully a bug in the
            // test driver
            System.out.println("numOfDefinedNetworks:" + conn.numOfDefinedNetworks());
            System.out.println("listDefinedNetworks:" + conn.listDefinedNetworks());
            for (String c : conn.listDefinedNetworks()) {
                System.out.println("	-> " + c);
            }
            System.out.println("numOfNetworks:" + conn.numOfNetworks());
            System.out.println("listNetworks:" + conn.listNetworks());
            for (String c : conn.listNetworks()) {
                System.out.println("	-> " + c);
            }

            // Look at the interfaces
            // TODO Post 0.5.1
            // System.out.println("numOfInterfaces:" + conn.numOfInterfaces());
            // System.out.println("listDefinedInterfaces:" +
            // conn.listInterfaces());
            // for(String c: conn.listInterfaces())
            // System.out.println("    -> "+c);

            /*
            // Define a new Domain
            System.out.println("conn.domainDefineXML:"
                    + conn.domainDefineXML("<domain type='test' id='2'>" + "  <name>deftest</name>"
                            + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e70</uuid>" + "  <memory>8388608</memory>"
                            + "  <vcpu>2</vcpu>" + "  <os><type arch='i686'>hvm</type></os>"
                            + "  <on_reboot>restart</on_reboot>" + "  <on_poweroff>destroy</on_poweroff>"
                            + "  <on_crash>restart</on_crash>" + "</domain>"));

            System.out.println("conn.domainCreateLinux:"
                    + conn.domainCreateLinux("<domain type='test' id='3'>" + "  <name>createst</name>"
                            + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e67</uuid>" + "  <memory>8388608</memory>"
                            + "  <vcpu>2</vcpu>" + "  <os><type arch='i686'>hvm</type></os>"
                            + "  <on_reboot>restart</on_reboot>" + "  <on_poweroff>destroy</on_poweroff>"
                            + "  <on_crash>restart</on_crash>" + "</domain>", 0));
			//*/
            
            // Domain enumeration stuff
            System.out.println("numOfDefinedDomains:" + conn.numOfDefinedDomains());
            System.out.println("listDefinedDomains:" + conn.listDefinedDomains());
            for (String c : conn.listDefinedDomains()) {
                System.out.println("	-> " + c);
            }
            
            System.out.println("numOfDomains:" + conn.numOfDomains());
            System.out.println("listDomains:" + conn.listDomains());
            for (int c : conn.listDomains()) {
            	vm = conn.domainLookupByID(c);
                System.out.println("	-> " + vm.getName());
            }	
            
            // Domain lookup
            Domain testDomain = conn.domainLookupByID(12);
            System.out.println("domainLookupByID: " + testDomain.getName());
            testDomain = conn.domainLookupByName("ceph-admin");
            System.out.println("domainLookupByName: " + testDomain.getName());

            // Exercise the getter methods on the default domain
            System.out.println("virDomainGetXMLDesc:" + testDomain.getXMLDesc(0));
            System.out.println("virDomainGetAutostart:" + testDomain.getAutostart());
            System.out.println("virDomainGetConnect:" + testDomain.getConnect());
            System.out.println("virDomainGetID:" + testDomain.getID());
            System.out.println("virDomainGetInfo:" + testDomain.getInfo());
            System.out.println("virDomainGetMaxMemory:" + testDomain.getMaxMemory());
            
            // Should fail, test driver does not support it
            try {
                System.out.println("virDomainGetMaxVcpus:" + testDomain.getMaxVcpus());
            } catch (LibvirtException e) {

            }
            System.out.println("virDomainGetName:" + testDomain.getName());
            System.out.println("virDomainGetOSType:" + testDomain.getOSType());
            System.out.println("virDomainGetSchedulerType:" + testDomain.getSchedulerType());
            System.out.println("virDomainGetSchedulerParameters:" + testDomain.getSchedulerParameters());
            
            // Iterate over the parameters the painful way
            for (SchedParameter c : testDomain.getSchedulerParameters()) {
                if (c instanceof SchedIntParameter)
                    System.out.println("Int:" + ((SchedIntParameter) c).field + ":" + ((SchedIntParameter) c).value);
                if (c instanceof SchedUintParameter)
                    System.out.println("Uint:" + ((SchedUintParameter) c).field + ":" + ((SchedUintParameter) c).value);
                if (c instanceof SchedLongParameter)
                    System.out.println("Long:" + ((SchedLongParameter) c).field + ":" + ((SchedLongParameter) c).value);
                if (c instanceof SchedUlongParameter)
                    System.out.println("Ulong:" + ((SchedUlongParameter) c).field + ":"
                            + ((SchedUlongParameter) c).value);
                if (c instanceof SchedDoubleParameter)
                    System.out.println("Double:" + ((SchedDoubleParameter) c).field + ":"
                            + ((SchedDoubleParameter) c).value);
                if (c instanceof SchedBooleanParameter)
                    System.out.println("Boolean:" + ((SchedBooleanParameter) c).field + ":"
                            + ((SchedBooleanParameter) c).value);
            }
            // Iterate over the parameters the easy way
            for (SchedParameter c : testDomain.getSchedulerParameters()) {
                System.out.println(c.getTypeAsString() + ":" + c.field + ":" + c.getValueAsString());
            }

            /*
            // test setting a scheduled parameter
            SchedUintParameter[] pars = new SchedUintParameter[1];
            pars[0] = new SchedUintParameter();
            pars[0].field = "weight";
            pars[0].value = 100;
            testDomain.setSchedulerParameters(pars);
            */

            System.out.println("virDomainGetUUID:" + testDomain.getUUID());
            for (int c : testDomain.getUUID()) {
                System.out.print(String.format("%02x", c));
            }
            System.out.println();
            System.out.println("virDomainGetUUIDString:" + testDomain.getUUIDString());
			
            System.out.println("====================================================================");
            
            String[] storagePools = conn.listStoragePools();
            System.out.println("storagePools : " + storagePools);
            System.out.println("storagePools.length : " + storagePools.length);
            StoragePool storagePool = null;
            for (String name : storagePools) {
            	storagePool = conn.storagePoolLookupByName(name);
            	System.out.println(storagePool.getName() + ", numOfVolumes : " + storagePool.numOfVolumes());
            	
            	String[] volumes = storagePool.listVolumes();
            	StorageVol vol = null;
            	for (String volume : volumes) {
            		vol = storagePool.storageVolLookupByName(volume);
            		System.out.println("     -> " + vol.getName() + ", path : " + vol.getPath());
            	}
            }
            
			conn.close();
		} catch (LibvirtException le) {
			le.printStackTrace();
		}
	}

}
// end of KVMManager.java