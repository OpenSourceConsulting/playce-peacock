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

	public List<Domain> getDomainList(Integer hypervisorId, String name, int page) throws LibvirtException {
		Connect connect = getHypervisorClient(hypervisorId).getConnect();
		
		List<Domain> domainList = new ArrayList<Domain>();

        for (int id : connect.listDomains()) {
        	domainList.add(connect.domainLookupByID(id));
        }
        
        for (String domain : connect.listDefinedDomains()) {
        	domainList.add(connect.domainLookupByName(domain));
        }	
		
		return domainList;
	}

	public Domain getDomain(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<?> getInterfaces(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<?> getDisks(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Domain createDomain(Integer hypervisorId, DomainDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object startDomain(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object stopDomain(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object shutdownDomain(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object removeDomain(Integer hypervisorId, String vmId) {
		// TODO Auto-generated method stub
		return null;
	}
}
//end of KVMService.java