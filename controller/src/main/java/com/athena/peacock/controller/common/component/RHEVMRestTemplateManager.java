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

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.athena.peacock.controller.web.hypervisor.HypervisorDto;
import com.athena.peacock.controller.web.hypervisor.HypervisorService;
import com.athena.peacock.controller.web.rhevm.RHEVApi;
import com.athena.peacock.controller.web.rhevm.domain.Nics;
import com.redhat.rhevm.api.model.Action;
import com.redhat.rhevm.api.model.VM;

/**
 * <pre>
 * Multi RHEV-M 사용을 위해 각 RHEV-M에 해당하는 RHEVRestTemplate을 관리한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
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
		RHEVMRestTemplate rhevTemplate = new RHEVMRestTemplate("HTTPS", "121.138.109.61", "internal", "8543", "admin", "redhat");

		String callUrl = RHEVApi.VMS + "/907df084-d7d6-4ecd-bf23-20531df58922/nics";
		Nics nics = rhevTemplate.submit(callUrl, HttpMethod.GET, Nics.class);
		
		JAXBContext context = JAXBContext.newInstance(Nics.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter writer = new StringWriter();
		
		QName qName = new QName("com.redhat.rhevm.api.model", "nics");
	    JAXBElement<Nics> root = new JAXBElement<Nics>(qName, Nics.class, nics);
		
	    //*
	    marshaller.marshal(root, writer);
	    /*/
		marshaller.marshal(nics, writer);
		//*/
	    
		System.out.println(writer.toString());
		
		
		
		// RestTemplate
        //*
		callUrl = RHEVApi.VMS + "/338547d8-f7c8-4397-856e-af4bd81c4939";
		
		Action action = new Action();
		action.setForce(true);
		String body = "<action><force>true</force></action>";
		action = rhevTemplate.submit(callUrl, HttpMethod.DELETE, body, "action", Action.class);
		
		System.out.println(action);
		//*/
		
		
		// HttpClient
		/*
		DeleteMethod delete = new DeleteMethod("https://121.138.109.61:8543" + RHEVApi.VMS + "/338547d8-f7c8-4397-856e-af4bd81c4939");
		delete.setRequestHeader("Host", "121.138.109.61");
		delete.setRequestHeader("Authorization", "Basic YWRtaW5AaW50ZXJuYWw6cmVkaGF0");
        // execute the method and handle any error responses.
		

		System.out.println("Send a [" + delete.getName() + "] method request to [" + delete.getURI().toString() + "].");
		
		Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory)new SecureProtocolSocketFactory() {

	        private SSLContext sslcontext = null;

	        private SSLContext createEasySSLContext() {
	            try {
	                SSLContext context = SSLContext.getInstance("SSL");
	                context.init(
	                  null, 
	                  new TrustManager[] {new X509TrustManager() {

						@Override
						public void checkClientTrusted(X509Certificate[] arg0,
								String arg1) throws CertificateException {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void checkServerTrusted(X509Certificate[] arg0,
								String arg1) throws CertificateException {
							// TODO Auto-generated method stub
							
						}

						@Override
						public X509Certificate[] getAcceptedIssuers() {
							// TODO Auto-generated method stub
							return null;
						}}}, 
	                  null);
	                return context;
	            } catch (Exception e) {
	                throw new HttpClientError(e.toString());
	            }
	        }

	        private SSLContext getSSLContext() {
	            if (this.sslcontext == null) {
	                this.sslcontext = createEasySSLContext();
	            }
	            return this.sslcontext;
	        }

	        public Socket createSocket(
	            String host,
	            int port,
	            InetAddress clientHost,
	            int clientPort)
	            throws IOException, UnknownHostException {

	            return getSSLContext().getSocketFactory().createSocket(
	                host,
	                port,
	                clientHost,
	                clientPort
	            );
	        }

	        public Socket createSocket(
	            final String host,
	            final int port,
	            final InetAddress localAddress,
	            final int localPort,
	            final HttpConnectionParams params
	        ) throws IOException, UnknownHostException, ConnectTimeoutException {
	            if (params == null) {
	                throw new IllegalArgumentException("Parameters may not be null");
	            }
	            int timeout = params.getConnectionTimeout();
	            SocketFactory socketfactory = getSSLContext().getSocketFactory();
	            if (timeout == 0) {
	                return socketfactory.createSocket(host, port, localAddress, localPort);
	            } else {
	                Socket socket = socketfactory.createSocket();
	                SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
	                SocketAddress remoteaddr = new InetSocketAddress(host, port);
	                socket.bind(localaddr);
	                socket.connect(remoteaddr, timeout);
	                return socket;
	            }
	        }

	        public Socket createSocket(String host, int port)
	            throws IOException, UnknownHostException {
	            return getSSLContext().getSocketFactory().createSocket(
	                host,
	                port
	            );
	        }

	        public Socket createSocket(
	            Socket socket,
	            String host,
	            int port,
	            boolean autoClose)
	            throws IOException, UnknownHostException {
	            return getSSLContext().getSocketFactory().createSocket(
	                socket,
	                host,
	                port,
	                autoClose
	            );
	        }

	        public boolean equals(Object obj) {
	            return ((obj != null) && obj.getClass().equals(this));
	        }

	        public int hashCode() {
	            return this.hashCode();
	        }
		}, 8543));
		
		int statusCode = new HttpClient().executeMethod(delete);
		
		System.out.println(statusCode);
		//*/
		
		
		// URL
		/*
		SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = new URL("https://121.138.109.61:8543" + RHEVApi.VMS + "/338547d8-f7c8-4397-856e-af4bd81c4939");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Basic YWRtaW5AaW50ZXJuYWw6cmVkaGF0");
        conn.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
        });
        System.out.println(conn.getResponseCode());
        conn.disconnect();
        //*/
	}
}
//end of RHEVRestTemplateManager.java