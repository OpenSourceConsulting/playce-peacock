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
 * Sang-cheon Park	2015. 9. 23.		First Draft.
 */
package com.athena.peacock.common.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component("peacockRestTemplate")
public class PeacockRestTemplate {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PeacockRestTemplate.class);

    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";
    private static final String CALAMARI_SESSIONID = "calamari_sessionid";
    
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private String calamariToken;
	private String calamariSessionid;
	
	/**
	 * <pre>
	 * 지정된 호스트로 API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param uri REST API uri
	 * @param http method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public String submit(String uri, HttpMethod method) throws RestClientException, Exception {
		return submit(uri, null, method);
	}//end of submit()
	
	/**
	 * <pre>
	 * API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param uri REST API uri
	 * @param body http body contents
	 * @param http method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public String submit(String uri, Object body, HttpMethod method) throws RestClientException, Exception {
		return submit(uri, body, method, null, null);
	}//end of submit()
	
	/**
	 * <pre>
	 * API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param uri REST API uri
	 * @param body http body contents
	 * @param http method
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public String submit(String uri, Object body, HttpMethod method, List<MediaType> acceptableMediaTypes, MediaType contentType) throws RestClientException, Exception {
		Assert.isTrue(StringUtils.isNotEmpty(uri), "uri must not be null");
		
		try {
			RestTemplate rt = new RestTemplate();
			
			Map<String, String> header = null;
			List<String> cookie = null;
			
			if (calamariToken != null && calamariSessionid != null) {
				header = new HashMap<String, String>();
				header.put(X_XSRF_TOKEN, calamariToken);
				
				cookie = new ArrayList<String>();
				cookie.add(CALAMARI_SESSIONID + "=" + calamariSessionid + "; " + XSRF_TOKEN + "=" + calamariToken + ";");
			}
			
			ResponseEntity<String> response = rt.exchange(new URI(uri), method, setHTTPEntity(body, header, cookie, acceptableMediaTypes, contentType), String.class);
			
			LOGGER.debug("[Request URL] : {}", uri);
			LOGGER.debug("[Response] : {}", response);
			
			if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST) 
					|| response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)
					|| response.getStatusCode().equals(HttpStatus.PAYMENT_REQUIRED)
					|| response.getStatusCode().equals(HttpStatus.FORBIDDEN)
					|| response.getStatusCode().equals(HttpStatus.METHOD_NOT_ALLOWED)
					|| response.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)
					|| response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)
					|| response.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED)
					|| response.getStatusCode().equals(HttpStatus.BAD_GATEWAY)
					|| response.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)
					|| response.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT)) {
				throw new Exception(response.getStatusCode().value() + " " + response.getStatusCode().toString());
			}
			
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.error("RestClientException has occurred.", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Unhandled Exception has occurred.", e);
			throw e;
		}
	}//end of submit()
	
	public void calamariLogin(String uri, String username, String password) throws Exception {
		Assert.isTrue(StringUtils.isNotEmpty(uri), "uri must not be null");
		Assert.isTrue(StringUtils.isNotEmpty(username), "username must not be null");
		Assert.isTrue(StringUtils.isNotEmpty(password), "password must not be null");
		
		try {
			/*
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> response = rt.exchange(new URI(uri), HttpMethod.GET, null, String.class);
			
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				List<String> values = response.getHeaders().get("Set-Cookie");
				
				calamariToken = null;
				calamariSessionid = null;
				for (String value : values) {
					if (value.startsWith(XSRF_TOKEN)) {
						calamariToken = value.substring(11, value.indexOf(";"));
						//System.err.println("value : " + value + ", token : " + calamariToken);
					}
					if (value.startsWith(CALAMARI_SESSIONID)) {
						calamariSessionid = value.substring(19, value.indexOf(";"));
						//System.err.println("value : " + value + ", sessionId : " + calamariSessionid);
					}
				}
				
				if (calamariToken != null && calamariSessionid != null) {
					Map<String, String> body = new HashMap<String, String>();
					body.put("username", username);
					body.put("password", password);
					response = rt.exchange(new URI(uri), HttpMethod.POST, setCalamariHTTPEntity(body, calamariToken, calamariSessionid), String.class);

					if (response.getStatusCode().equals(HttpStatus.OK)) {
						values = response.getHeaders().get("Set-Cookie");
						
						calamariToken = null;
						calamariSessionid = null;
						for (String value : values) {
							if (value.startsWith(XSRF_TOKEN)) {
								calamariToken = value.substring(11, value.indexOf(";"));
								//System.err.println("value : " + value + ", token : " + calamariToken);
							}
							if (value.startsWith(CALAMARI_SESSIONID)) {
								calamariSessionid = value.substring(19, value.indexOf(";"));
								//System.err.println("value : " + value + ", sessionId : " + calamariSessionid);
							}
						}
					}
				}
			}
			/*/
			Map<String, String> body = new HashMap<String, String>();
			body.put("username", username);
			body.put("password", password);

			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> response = rt.exchange(new URI(uri), HttpMethod.POST, setHTTPEntity(body), String.class);

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				List<String> values = response.getHeaders().get("Set-Cookie");
				
				calamariToken = null;
				calamariSessionid = null;
				for (String value : values) {
					if (value.startsWith(XSRF_TOKEN)) {
						calamariToken = value.substring(11, value.indexOf(";"));
						//System.err.println("value : " + value + ", token : " + calamariToken);
					}
					if (value.startsWith(CALAMARI_SESSIONID)) {
						calamariSessionid = value.substring(19, value.indexOf(";"));
						//System.err.println("value : " + value + ", sessionId : " + calamariSessionid);
					}
				}
			}
			//*/
			LOGGER.debug("[Request URL] : {}", uri);
			LOGGER.debug("[Response] : {}", response);
		} catch (RestClientException e) {
			LOGGER.error("RestClientException has occurred.", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Unhandled Exception has occurred.", e);
			throw e;
		}
	}
	
	/**
	 * <pre>
	 * HTTP Body를 생성한다.
	 * </pre>
	 * @return
	 * @throws IOException 
	 */
	private HttpEntity<Object> setHTTPEntity(Object body) throws IOException {
		return setHTTPEntity(body, null, null);
	}//end of setHTTPEntity()
	
	/**
	 * <pre>
	 * HTTP Body를 생성한다.
	 * </pre>
	 * @return
	 * @throws IOException 
	 */
	private HttpEntity<Object> setHTTPEntity(Object body, Map<String, String> header, List<String> cookie) throws IOException {
		return setHTTPEntity(body, header, cookie, null, null);
	}//end of setHTTPEntity()
	
	/**
	 * <pre>
	 * HTTP Body를 생성한다.
	 * </pre>
	 * @return
	 * @throws IOException 
	 */
	private HttpEntity<Object> setHTTPEntity(Object body, Map<String, String> header, List<String> cookie, List<MediaType> acceptableMediaTypes, MediaType contentType) throws IOException {
		if (acceptableMediaTypes == null) {
			acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		}
		
		if (contentType == null) {
			contentType = MediaType.APPLICATION_JSON;
		}
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(contentType);
		requestHeaders.setAccept(acceptableMediaTypes);
		
		if (header != null) {
			Iterator<String> iter = header.keySet().iterator();
			
			String key = null;
			while (iter.hasNext()) {
				key = iter.next();
				requestHeaders.set(key, header.get(key));
			}
		}
		
		if (cookie != null) {
			for (String c : cookie) {
				requestHeaders.add("Cookie", c);
			}
		}
		
		if (body != null) {
			LOGGER.debug("Content Body => {}", objToJson(body));
			return new HttpEntity<Object>(objToJson(body), requestHeaders);
		} else {
			return new HttpEntity<Object>(requestHeaders);
		}
	}//end of setHTTPEntity()

	/**
	 * <pre>
	 * Object를 JSON String으로 변환한다.
	 * </pre>
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	private String objToJson(Object obj) throws IOException {
		if (obj instanceof String) {
			return (String) obj;
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonGenerator generator = MAPPER.getJsonFactory().createJsonGenerator(outputStream, JsonEncoding.UTF8);
		MAPPER.writeValue(generator, obj);
		
		return outputStream.toString(JsonEncoding.UTF8.getJavaName());
	}
	//end of objToJson()
}
//end of PeacockRestTemplate.java