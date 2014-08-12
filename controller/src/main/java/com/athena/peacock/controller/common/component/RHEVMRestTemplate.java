/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 10. 31.		First Draft.
 */
package com.athena.peacock.controller.common.component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * <pre>
 * Spring RestTemplate을 이용하여 RHEV Manager의 RESTFul 서비스를 호출하기 위한 Component 클래스
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class RHEVMRestTemplate {
	
    private static final Logger logger = LoggerFactory.getLogger(RHEVMRestTemplate.class);
	
	private static final String HOST_HEADER_KEY = "Host";
	private static final String AUTH_HEADER_KEY = "Authorization";
	   
	private String protocol;
	private String host;
	private String domain;
	private String port;
	private String username;
	private String password;
	
	private String credential;
	
	public RHEVMRestTemplate(String protocol, String host, String domain, String port, String username, String password) {
		this.protocol = protocol;
		this.host = host;
		this.domain = domain;
		this.port = port;
		this.username = username;
		this.password = password;
		
		try {
			init();
		} catch (Exception e) {
			// ignore
		}
	}
	
	/**
	 * <pre>
	 * 유효하지 않은 인증서를 가진 호스트로 HTTPS 호출 시 HandShake Exception 및 인증서 관련 Exception이 발생하기 때문에
	 * RHEV Manager(host) 및 SSL 인증서 관련 검증 시 예외를 발생하지 않도록 추가됨.
	 * </pre>
	 * @throws Exception
	 */
	public void init() throws Exception {
		// Create a hostname verifier that does not validate hostname
	    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				if (hostname.equals(host)) {
                    return true;
				}
				
				return false;
			}
	    });
	    
	    // Create a trust manager that does not validate certificate chains
	    // Refer to https://code.google.com/p/misc-utils/wiki/JavaHttpsUrl
	    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	        @Override
	        public void checkClientTrusted(X509Certificate[] chain, String authType ) throws CertificateException {
	        	// nothing to do.
	        }
	        
	        @Override
	        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	        	// nothing to do.
	        }
	        
	        @Override
	        public X509Certificate[] getAcceptedIssuers() {
	            return null;
	        }
	    }};
	    
	    try {
			// Install the all-trusting trust manager
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException has occurred.", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException has occurred.", e);
		}
	}//end of init()
	
	/**
	 * <pre>
	 * HTTP Header에 인증 정보를 포함시킨다.
	 * </pre>
	 * @return
	 */
	private HttpEntity<Object> setHTTPEntity(Object body, String rootElementName) {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
	    acceptableMediaTypes.add(MediaType.APPLICATION_XML);
	    
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_XML);
		requestHeaders.setAccept(acceptableMediaTypes);
		requestHeaders.set(HOST_HEADER_KEY, host);
		requestHeaders.set(AUTH_HEADER_KEY, getCredential());
		
		if (body != null) {
			logger.debug("Content Body => {}", marshal(body, rootElementName));
			return new HttpEntity<Object>(marshal(body, rootElementName), requestHeaders);
		} else {
			return new HttpEntity<Object>(requestHeaders);
		}
	}//end of addAuth()
	
	/**
	 * <pre>
	 * RHEV Manager에 전달하기 위한 인증 정보를 생성한다. 
	 * </pre>
	 * @return
	 */
	public String getCredential() {
		if (credential == null) {
			try {
				String plain = username + "@" + domain + ":" + password;
				credential = "Basic " + Base64.encodeBase64String(plain.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error("UnsupportedEncodingException has occurred.", e);
			}
		}
		
		return credential;
	}//end of getCredential()
	
	/**
	 * <pre>
	 * RHEV Manager를 호출하기 위한 URL를 조합한다.
	 * </pre>
	 * @param api
	 * @return
	 */
	public String getUrl(String api) {
		StringBuilder sb = new StringBuilder();
		
		if (protocol.toLowerCase().equals("http")) {
			sb.append("http://");
		} else {
			sb.append("https://");
		}
		
		sb.append(host).append(":").append(port);
		
		if (!api.startsWith("/")) {
			sb.append("/");
		}
		
		sb.append(api);
		
		return sb.toString();
	}//end of getUrl()
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String marshal(Object obj, String rootElementName) {
		StringWriter sw = new StringWriter();
		try {
			JAXBContext jaxbCtx = JAXBContext.newInstance(obj.getClass().getPackage().getName());
			Marshaller marshaller = jaxbCtx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(new JAXBElement(new QName(rootElementName), obj.getClass(), obj), sw);
			sw.close();
			
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sw.toString();
	}
	
	/**
	 * <pre>
	 * RHEV Manager로 해당 API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param api RHEV Manager API (/api, /api/vms 등)
	 * @param clazz 변환될 Target Object Class
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public <T> T submit(String api, HttpMethod method, Class<T> clazz) throws RestClientException, Exception {
		Assert.isTrue(StringUtils.isNotEmpty(api), "api must not be null");
		Assert.notNull(clazz, "clazz must not be null.");
		
		return submit(api, method, null, null, clazz);
	}//end of submit()
	
	/**
	 * <pre>
	 * RHEV Manager로 해당 API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param api RHEV Manager API (/api, /api/vms 등)
	 * @param body xml contents
	 * @param clazz 변환될 Target Object Class
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public <T> T submit(String api, HttpMethod method, Object body, String rootElementName, Class<T> clazz) throws RestClientException, Exception {
		Assert.isTrue(StringUtils.isNotEmpty(api), "api must not be null");
		Assert.notNull(clazz, "clazz must not be null.");
		
		try {
			RestTemplate rt = new RestTemplate();
			
			ResponseEntity<?> response = rt.exchange(getUrl(api), method, setHTTPEntity(body, rootElementName), clazz);
			
			logger.debug("[Request URL] : {}", getUrl(api));
			logger.debug("[Response] : {}", response);
			
			return clazz.cast(response.getBody());
		} catch (RestClientException e) {
			logger.error("RestClientException has occurred.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred.", e);
			throw e;
		}
	}//end of submit()
}
//end of RHEVMRestTemplate.java