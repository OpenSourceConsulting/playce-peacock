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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.common.component.HypervisorClientManager;
import com.athena.peacock.controller.common.component.KVMVirshClient;
import com.athena.peacock.controller.web.kvm.dto.DomainDto;

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

	public List<DomainDto> getDomainList(Integer hypervisorId, String name, int page) throws LibvirtException {
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

	public DomainDto getDomain(Integer hypervisorId, String vmId) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
		
		return convertToDomainDto(domain);
	}

	public List<?> getInterfaces(Integer hypervisorId, String vmId) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		Domain domain = connect.domainLookupByUUIDString(vmId);
		
		return null;
	}

	public List<?> getDisks(Integer hypervisorId, String vmId) throws LibvirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public Domain createDomain(Integer hypervisorId, DomainDto dto) throws LibvirtException {
		// TODO Auto-generated method stub
		return null;
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
	
	private DomainDto convertToDomainDto(Domain domain) throws LibvirtException {
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
		
		// TODO osArch, osMachine
		dto.setXmlDesc(domain.getXMLDesc(0));
		
		return dto;
	}
}
//end of KVMService.java