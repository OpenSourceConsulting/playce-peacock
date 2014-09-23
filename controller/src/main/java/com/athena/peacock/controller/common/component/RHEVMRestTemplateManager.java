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
	 * 주어진 정보를 이용하여 RHEVMRestTemplate 객체를 생성하고 Map에 저장한다.
	 * </pre>
	 * @param hypervisor
	 */
	public synchronized static void setRHEVMRestTemplate(HypervisorDto hypervisor) {
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
	public synchronized static void removeRHEVMRestTemplate(int hypervisorId) {
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
	
	public static void main(String[] args) throws Exception {
		String protocol = "HTTPS";
		String host = "";
		String domain = "internal";
		String port = "8443";
		String username = "admin";
		String password = "";
		
		RHEVMRestTemplate rhevTemplate = new RHEVMRestTemplate(protocol, host, domain, port, username, password);
		
		String callUrl = RHEVApi.VMS + "?search=192.168.0.99+page+1";
		VMs vms = rhevTemplate.submit(callUrl, HttpMethod.GET, VMs.class);
		
		System.err.println(vms.getVMs().size());
		
		JAXBContext context = JAXBContext.newInstance(VMs.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter writer = new StringWriter();
		
		QName qName = new QName("com.redhat.rhevm.api.model", "vms");
	    JAXBElement<VMs> root = new JAXBElement<VMs>(qName, VMs.class, vms);
		
	    //*
	    marshaller.marshal(root, writer);
	    /*/
		marshaller.marshal(nics, writer);
		//*/
	    
		System.out.println(writer.toString());
		
		callUrl = RHEVApi.TEMPLATES + "/48eec0f7-9c97-415d-8749-3cda47ead3a1";
		Template template = rhevTemplate.submit(callUrl, HttpMethod.GET, Template.class);
		
		context = JAXBContext.newInstance(Template.class);
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		writer = new StringWriter();
		
		qName = new QName("com.redhat.rhevm.api.model", "template");
	    JAXBElement<Template> root2 = new JAXBElement<Template>(qName, Template.class, template);
		
	    //*
	    marshaller.marshal(root2, writer);
	    /*/
		marshaller.marshal(nics, writer);
		//*/
	    
		System.out.println(writer.toString());
		

		// RestTemplate
        /*
		callUrl = RHEVApi.VMS + "/ca6c6d92-9eb0-40bf-9dcc-890e6136cc15";
		
		Action action = new Action();
		action.setForce(true);
		action = rhevTemplate.submit(callUrl, HttpMethod.DELETE, null, null, Action.class);
		
		System.out.println(action);
		//*/

	}
}
//end of RHEVRestTemplateManager.java