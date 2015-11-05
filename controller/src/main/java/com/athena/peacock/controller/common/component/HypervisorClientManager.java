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


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.controller.web.hypervisor.HypervisorDto;
import com.athena.peacock.controller.web.hypervisor.HypervisorService;
import com.athena.peacock.controller.web.rhevm.RHEVApi;
import com.redhat.rhevm.api.model.API;
import com.redhat.rhevm.api.model.Template;
import com.redhat.rhevm.api.model.VMs;
import com.redhat.rhevm.api.model.Version;

/**
 * <pre>
 * Multi RHEV-M 사용을 위해 각 RHEV-M에 해당하는 RHEVRestTemplate을 관리한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component("hypervisiorClientManager")
public class HypervisorClientManager implements InitializingBean {

	private static Map<Integer, HypervisorClient> clients = new ConcurrentHashMap<Integer, HypervisorClient>();
	
	@Inject
	@Named("hypervisorService")
	private HypervisorService hypervisorService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<HypervisorDto> hypervisorList = hypervisorService.getHypervisorList();
		
		for (HypervisorDto hypervisor : hypervisorList) {
			setHypervisorClient(hypervisor);
		}
	}
	
	/**
	 * <pre>
	 * 주어진 hypervisorId에 해당하는 HypervisorClient 객체를 가져온다.
	 * </pre>
	 * @param hypervisorId
	 * @return
	 */
	public static HypervisorClient getHypervisorClient(int hypervisorId) {
		return clients.get(hypervisorId);
	}//end of getHypervisorClient()
	
	/**
	 * <pre>
	 * 주어진 정보를 이용하여 HypervisorClient 객체를 초기화한다.
	 * </pre>
	 * @param hypervisorList
	 */
	public static void resetHypervisorClient(List<HypervisorDto> hypervisorList) {
		Map<Integer, HypervisorClient> newClients = new ConcurrentHashMap<Integer, HypervisorClient>();
		
		for (HypervisorDto hypervisor : hypervisorList) {
			if (hypervisor.getHypervisorType().toUpperCase().startsWith("RHEV")) {
				RHEVMRestTemplate client = new RHEVMRestTemplate(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
						hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
				
				client.setHypervisorId(hypervisor.getHypervisorId());
				client.setRhevmName(hypervisor.getRhevmName());
				
				try {
					API api = client.submit(RHEVApi.API, HttpMethod.GET, API.class);
					
					if (api != null) {
						Version version = api.getProductInfo().getVersion();
						
						if (version != null) {
							client.setMajor(version.getMajor());
							client.setMinor(version.getMinor());
						}
					}
				} catch (RestClientException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				newClients.put(hypervisor.getHypervisorId(), client);
			} else if (hypervisor.getHypervisorType().toUpperCase().startsWith("KVM")) {
//				KVMVirshClient client = new KVMVirshClient(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
//						hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
//
//				client.setHypervisorId(hypervisor.getHypervisorId());
//				client.setRhevmName(hypervisor.getRhevmName());
//				
//				newClients.put(hypervisor.getHypervisorId(), client);
			}
		}
		
		clients = newClients;
	}//end of resetHypervisorClient()
	
	/**
	 * <pre>
	 * 주어진 정보를 이용하여 HypervisorClient 객체를 생성하고 Map에 저장한다.
	 * </pre>
	 * @param hypervisor
	 */
	public static void setHypervisorClient(HypervisorDto hypervisor) {
		if (hypervisor.getHypervisorType().toUpperCase().startsWith("RHEV")) {
			RHEVMRestTemplate client = new RHEVMRestTemplate(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
					hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
			
			client.setHypervisorId(hypervisor.getHypervisorId());
			client.setRhevmName(hypervisor.getRhevmName());
			
			try {
				API api = client.submit(RHEVApi.API, HttpMethod.GET, API.class);
				
				if (api != null) {
					Version version = api.getProductInfo().getVersion();
					
					if (version != null) {
						client.setMajor(version.getMajor());
						client.setMinor(version.getMinor());
					}
				}
			} catch (RestClientException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			clients.put(hypervisor.getHypervisorId(), client);
		} else if (hypervisor.getHypervisorType().toUpperCase().startsWith("KVM")) {
//			KVMVirshClient client = new KVMVirshClient(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
//					hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
//
//			client.setHypervisorId(hypervisor.getHypervisorId());
//			client.setRhevmName(hypervisor.getRhevmName());
//			
//			clients.put(hypervisor.getHypervisorId(), client);
		}
	}//end of setHypervisorClient()
	
	/**
	 * <pre>
	 * 주어진 hypervisorId에 해당하는 HypervisorClient 객체를 제거한다.
	 * </pre>
	 * @param hypervisorId
	 */
	public static void removeHypervisorClient(int hypervisorId) {
		clients.remove(hypervisorId);
	}//end of removeHypervisorClient()
	
	/**
	 * <pre>
	 * Map에 존재하는 모든 HypervisorClient 객체를 반환한다.
	 * </pre>
	 * @return
	 */
	public static List<HypervisorClient> getAllTemplates() {
		return new ArrayList<HypervisorClient>(clients.values());
	}//end of getAllTemplates()
}
//end of HypervisorClientManager.java