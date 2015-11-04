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
 * Sang-cheon Park	2014. 8. 12.		First Draft.
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
@Component("rhevmRestTemplateManager")
public class RHEVMRestTemplateManager implements InitializingBean {

	private static Map<Integer, RHEVMRestTemplate> templates = new ConcurrentHashMap<Integer, RHEVMRestTemplate>();
	
	@Inject
	@Named("hypervisorService")
	private HypervisorService hypervisorService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<HypervisorDto> hypervisorList = hypervisorService.getHypervisorList();
		
		for (HypervisorDto hypervisor : hypervisorList) {
			setRHEVMRestTemplate(hypervisor);
		}
	}
	
	/**
	 * <pre>
	 * 주어진 hypervisorId에 해당하는 RHEVMRestTemplate 객체를 가져온다.
	 * </pre>
	 * @param hypervisorId
	 * @return
	 */
	public static RHEVMRestTemplate getRHEVMRestTemplate(int hypervisorId) {
		return templates.get(hypervisorId);
	}//end of getRHEVMRestTemplate()
	
	/**
	 * <pre>
	 * 주어진 정보를 이용하여 RHEVMRestTemplate 객체를 초기화한다.
	 * </pre>
	 * @param hypervisorList
	 */
	public static void resetRHEVMRestTemplate(List<HypervisorDto> hypervisorList) {
		Map<Integer, RHEVMRestTemplate> newTemplates = new ConcurrentHashMap<Integer, RHEVMRestTemplate>();
		
		for (HypervisorDto hypervisor : hypervisorList) {
			RHEVMRestTemplate template = new RHEVMRestTemplate(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
					hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
			
			template.setHypervisorId(hypervisor.getHypervisorId());
			template.setRhevmName(hypervisor.getRhevmName());
			
			try {
				API api = template.submit(RHEVApi.API, HttpMethod.GET, API.class);
				
				if (api != null) {
					Version version = api.getProductInfo().getVersion();
					
					if (version != null) {
						template.setMajor(version.getMajor());
						template.setMinor(version.getMinor());
					}
				}
			} catch (RestClientException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			newTemplates.put(hypervisor.getHypervisorId(), template);
		}
		
		templates = newTemplates;
	}//end of resetRHEVMRestTemplate()
	
	/**
	 * <pre>
	 * 주어진 정보를 이용하여 RHEVMRestTemplate 객체를 생성하고 Map에 저장한다.
	 * </pre>
	 * @param hypervisor
	 */
	public static void setRHEVMRestTemplate(HypervisorDto hypervisor) {
		RHEVMRestTemplate template = new RHEVMRestTemplate(hypervisor.getRhevmProtocol(), hypervisor.getRhevmHost(), hypervisor.getRhevmDomain(), 
				hypervisor.getRhevmPort(), hypervisor.getRhevmUsername(), hypervisor.getRhevmPassword());
		
		template.setHypervisorId(hypervisor.getHypervisorId());
		template.setRhevmName(hypervisor.getRhevmName());
		
		try {
			API api = template.submit(RHEVApi.API, HttpMethod.GET, API.class);
			
			if (api != null) {
				Version version = api.getProductInfo().getVersion();
				
				if (version != null) {
					template.setMajor(version.getMajor());
					template.setMinor(version.getMinor());
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		templates.put(hypervisor.getHypervisorId(), template);
	}//end of setRHEVMRestTemplate()
	
	/**
	 * <pre>
	 * 주어진 hypervisorId에 해당하는 RHEVMRestTemplate 객체를 제거한다.
	 * </pre>
	 * @param hypervisorId
	 */
	public static void removeRHEVMRestTemplate(int hypervisorId) {
		templates.remove(hypervisorId);
	}//end of removeRHEVMRestTemplate()
	
	/**
	 * <pre>
	 * Map에 존재하는 모든 RHEVMRestTemplate 객체를 반환한다.
	 * </pre>
	 * @return
	 */
	public static List<RHEVMRestTemplate> getAllTemplates() {
		return new ArrayList<RHEVMRestTemplate>(templates.values());
	}//end of getAllTemplates()
	
}
//end of RHEVRestTemplateManager.java