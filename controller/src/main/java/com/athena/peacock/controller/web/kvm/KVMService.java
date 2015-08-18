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
 * Sang-cheon Park	2015. 8. 10.		First Draft.
 */
package com.athena.peacock.controller.web.kvm;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.libvirt.StoragePool;
import org.libvirt.StorageVol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.athena.peacock.controller.common.component.HypervisorClientManager;
import com.athena.peacock.controller.common.component.KVMVirshClient;
import com.athena.peacock.controller.web.kvm.dto.DiskDto;
import com.athena.peacock.controller.web.kvm.dto.DomainDto;
import com.athena.peacock.controller.web.kvm.dto.NetworkDto;

/**
 * <pre>
 * KVM Service Class
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("kvmService")
public class KVMService {
	
	protected final Logger logger = LoggerFactory.getLogger(KVMService.class);

	@Inject
	@Named("hypervisiorClientManager")
	private HypervisorClientManager manager;
	
	public KVMVirshClient getHypervisorClient(int hypervisorId) {
		return (KVMVirshClient) HypervisorClientManager.getHypervisorClient(hypervisorId);
	}
	
	public void init() {
		try {
			manager.afterPropertiesSet();
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred while initializing RHEVMRestTemplates.", e);
		}
	}

	public List<DomainDto> getDomainList(Integer hypervisorId, String name, int page) throws Exception {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		
		List<DomainDto> domainList = new ArrayList<DomainDto>();

        for (int id : connect.listDomains()) {
        	domainList.add(convertToDomainDto(connect.domainLookupByID(id)));
        }
        
        for (String domain : connect.listDefinedDomains()) {
        	domainList.add(convertToDomainDto(connect.domainLookupByName(domain)));
        }	
		
		return domainList;
	}

	public DomainDto getDomain(Integer hypervisorId, String vmId) throws Exception {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
		
		return convertToDomainDto(domain);
	}

	public List<NetworkDto> getInterfaces(Integer hypervisorId, String vmId) throws Exception {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		String xml = connect.domainLookupByUUIDString(vmId).getXMLDesc(0);
		
		List<NetworkDto> networkList = new ArrayList<NetworkDto>();
		NetworkDto dto = null;
		
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

		XPath xpath = XPathFactory.newInstance().newXPath();
		
		String expression = "//*/interface";
		NodeList cols = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);

		for (int idx = 0; idx < cols.getLength(); idx++) {
			Element element = (Element) cols.item(idx);
			
			dto = new NetworkDto();
			dto.setIfType(element.getAttributes().item(0).getNodeValue());
			dto.setMacAddress(element.getElementsByTagName("mac").item(0).getAttributes().item(0).getNodeValue());
			dto.setSourceBridge(element.getElementsByTagName("source").item(0).getAttributes().item(0).getNodeValue());
			dto.setTargetDev(element.getElementsByTagName("target").item(0).getAttributes().item(0).getNodeValue());
			dto.setModelType(element.getElementsByTagName("model").item(0).getAttributes().item(0).getNodeValue());
			dto.setAliasName(element.getElementsByTagName("alias").item(0).getAttributes().item(0).getNodeValue());
			dto.setAddressType(element.getElementsByTagName("address").item(0).getAttributes().item(0).getNodeValue());
			
			networkList.add(dto);
		}
		
		return networkList;
	}

	public List<DiskDto> getDisks(Integer hypervisorId, String vmId) throws Exception {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		String xml = connect.domainLookupByUUIDString(vmId).getXMLDesc(0);
		
		List<DiskDto> diskList = new ArrayList<DiskDto>();
		DiskDto dto = null;
		
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

		XPath xpath = XPathFactory.newInstance().newXPath();
		
		String expression = "//*/disk";
		NodeList cols = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);

		for (int idx = 0; idx < cols.getLength(); idx++) {
			Element element = (Element) cols.item(idx);
			
			dto = new DiskDto();
			dto.setDiskType(element.getAttributes().item(0).getNodeValue());
			dto.setDiskDevice(element.getAttributes().item(1).getNodeValue());
			dto.setDriverName(element.getElementsByTagName("driver").item(0).getAttributes().getNamedItem("name").getNodeValue());
			dto.setDriverType(element.getElementsByTagName("driver").item(0).getAttributes().getNamedItem("type").getNodeValue());
			dto.setDriverCache(element.getElementsByTagName("driver").item(0).getAttributes().getNamedItem("cache").getNodeValue());
			dto.setSourceFile(element.getElementsByTagName("source").item(0).getAttributes().getNamedItem("file").getNodeValue());
			dto.setTargetDev(element.getElementsByTagName("target").item(0).getAttributes().getNamedItem("dev").getNodeValue());
			dto.setTargetBus(element.getElementsByTagName("target").item(0).getAttributes().getNamedItem("bus").getNodeValue());
			dto.setAliasName(element.getElementsByTagName("alias").item(0).getAttributes().getNamedItem("name").getNodeValue());
			dto.setAddressType(element.getElementsByTagName("address").item(0).getAttributes().getNamedItem("type").getNodeValue());
			
			diskList.add(dto);
		}
		
		return diskList;
	}

	public Domain createDomain(Integer hypervisorId, DomainDto dto) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		
		/*
		 * clone disk
		 */
		String sourcePath = dto.getSourceFile();
		String sourceFileName = sourcePath.substring(sourcePath.lastIndexOf("/") + 1);
		String targetFileName = dto.getName() + ".img";
		String targetPath = sourcePath.substring(0, sourcePath.lastIndexOf("/") + 1) + targetFileName;
		
		StorageVol vol = cloneVol(hypervisorId, sourceFileName, targetFileName);
		
		if (vol != null) {
			targetPath = vol.getPath();
		}
		
		/*
		 * os element
		 */
		StringBuilder os = new StringBuilder("<os>");
		os.append("<type");
		
		if (dto.getOsArch() != null) {
			os.append(" arch='").append(dto.getOsArch()).append("'");
		}
		if (dto.getOsMachine() != null) {
			os.append(" machine='").append(dto.getOsMachine()).append("'");
		}
		os.append(">").append(dto.getOsType()).append("</type>");
		os.append("</os>");
		
		/*
		 * disk element
		 */
		StringBuilder disk = new StringBuilder("<disk type='");
		
		if (dto.getDiskDto().getDiskType() != null) {
			disk.append(dto.getDiskDto().getDiskType()).append("'");
		} else {
			disk.append("file").append("'");
		}
		
		disk.append(" device='");
		
		if (dto.getDiskDto().getDiskDevice() != null) {
			disk.append(dto.getDiskDto().getDiskDevice()).append("'>");
		} else {
			disk.append("disk").append("'>");
		}
		
		disk.append("<driver  name='").append(dto.getDiskDto().getDriverName()).append("'")
					.append(" type='").append(dto.getDiskDto().getDriverType()).append("'")
					.append(" cache='none' />");
		
		disk.append("<source file='").append(targetPath).append("'/>");
		
		disk.append("<target  dev='").append(dto.getDiskDto().getTargetDev()).append("'")
					.append(" bus='").append(dto.getDiskDto().getTargetBus()).append("'/>");
		
		disk.append("</disk>");
		
		/*
		 * interface elment
		 */
		StringBuilder iface = new StringBuilder("<interface type='");
		
		if (dto.getNetworkDto().getIfType() != null) {
			iface.append(dto.getNetworkDto().getIfType()).append("'>");
		} else {
			iface.append("bridge").append("'>");
		}
		
		iface.append("<source bridge='").append(dto.getNetworkDto().getSourceBridge()).append("'/>");
		iface.append("<model type='").append(dto.getNetworkDto().getModelType()).append("'/>");
		iface.append("</interface>");
		
		String domainXml =	"<domain type='kvm'>" +
							"  <name>" + dto.getName() + "</name>" +
							"  <memory unit='KiB'>" + dto.getMemory() + "</memory>" +
							"  <vcpu>" + dto.getVcpu() + "</vcpu>" +
							os.toString() +
							"  <features>" +
							"    <acpi/>" +
							"    <apic/>" +
							"    <pae/>" +
							"  </features>" +
							"  <clock offset='utc'/>" +
							"  <on_poweroff>destroy</on_poweroff>" +
							"  <on_reboot>restart</on_reboot>" +
							"  <on_crash>restart</on_crash>" +
							"  <devices>" +
							"    <emulator>/usr/libexec/qemu-kvm</emulator>" +
							disk.toString() +
							"    <controller type='usb' index='0' model='ich9-ehci1' />" +
							"    <controller type='usb' index='0' model='ich9-uhci1'>" +
							"      <master startport='0'/>" +
							"    </controller>" +
							"    <controller type='usb' index='0' model='ich9-uhci2'>" +
							"      <master startport='2'/>" +
							"    </controller>" +
							"    <controller type='usb' index='0' model='ich9-uhci3'>" +
							"      <master startport='4'/>" +
							"    </controller>" +
							iface.toString() +
							"    <serial type='pty'>" +
							"      <target port='0'/>" +
							"    </serial>" +
							"    <console type='pty'>" +
							"      <target type='serial' port='0'/>" +
							"    </console>" +
							"    <input type='tablet' bus='usb'/>" +
							"    <input type='mouse' bus='ps2'/>" +
							"    <graphics type='vnc' port='-1' autoport='yes'/>" +
							"    <sound model='ich6' />" +
							"    <video>" +
							"      <model type='cirrus' vram='9216' heads='1'/>" +
							"    </video>" +
							"    <memballoon model='virtio' />" +
							"  </devices>" +
							"</domain>";

		Domain domain = connect.domainDefineXML(domainXml);  // persistent domain
		domain.create(0);
		//connect.domainCreateXML(domainXml, 0);  // transient domain
		
		return domain;
	}

	public boolean startDomain(Integer hypervisorId, String vmId) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
		
		/*
		try {
			// TODO change 
			connect.restore("/var/lib/libvirt/qemu/save/" + domain.getName() + ".save");
		} catch (LibvirtException e) {
			// in case of save file doesn't exist.
	        if (domain.isPersistent() == 1) {
	        	domain.create();
	        }
		}
		*/
		domain.resume();
		
		return true;
	}

	public boolean stopDomain(Integer hypervisorId, String vmId) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
        domain.suspend();
        
		return true;
	}

	public boolean shutdownDomain(Integer hypervisorId, String vmId) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
        domain.shutdown();
        
		return true;
	}

	public boolean removeDomain(Integer hypervisorId, String vmId) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
		
        if (domain.isPersistent() == 1) {
        	domain.undefine();
        }
        
    	domain.destroy();
    	
		return true;
	}
	
	private DomainDto convertToDomainDto(Domain domain) throws Exception {
		DomainDto dto = new DomainDto();

		dto.setName(domain.getName());
		dto.setVmId(domain.getUUIDString());
		dto.setMemory(domain.getMaxMemory() / 1024);
		dto.setVcpu(domain.getMaxVcpus());
		dto.setOsType(domain.getOSType());
		
		if (domain.isActive() == 1) {
			dto.setStatus("Running");
		} else {
			dto.setStatus("Shutoff");
		}
		
		String xml = domain.getXMLDesc(0);
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

		XPath xpath = XPathFactory.newInstance().newXPath();
		
		NodeList cols = (NodeList) xpath.compile("//*/os").evaluate(document, XPathConstants.NODESET);
		Element os = (Element) cols.item(0);
		NodeList type = os.getElementsByTagName("type");

		String osArch = type.item(0).getAttributes().item(0).getNodeValue();
		String osMachine = type.item(0).getAttributes().item(1).getNodeValue();
		
		dto.setOsArch(osArch);
		dto.setOsMachine(osMachine);
		dto.setXmlDesc(xml);
		
		return dto;
	}
	
	private StorageVol cloneVol(Integer hypervisorId, String fromVolName, String newVolName) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		
		StoragePool pool = null;
		StorageVol vol = null;
		
		for (String poolName : connect.listStoragePools()) {
			pool = connect.storagePoolLookupByName(poolName);
			
			for (String volName : pool.listVolumes()) {
				if (volName.equals(fromVolName)) {
					vol = pool.storageVolLookupByName(volName);

					String volXml = vol.getXMLDesc(0);
					volXml = volXml.replaceAll(fromVolName, newVolName);
					
					return pool.storageVolCreateXMLFrom(volXml, vol, 0);
				}
			}
		}
		
		return null;
	}
}
//end of KVMService.java