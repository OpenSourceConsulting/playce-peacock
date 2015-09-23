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
import java.util.List;

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
	
    private static final Logger logger = LoggerFactory.getLogger(PeacockRestTemplate.class);

	//private static final String HOST_HEADER_KEY = "Host";
	//private static final String AUTH_HEADER_KEY = "Authorization";
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * <pre>
	 * 지정된 호스트로 API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param uri REST API uri
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public String submit(String uri, HttpMethod method) throws RestClientException, Exception {
		Assert.isTrue(StringUtils.isNotEmpty(uri), "uri must not be null");
		
		return submit(uri, method, null);
	}//end of submit()
	
	/**
	 * <pre>
	 * API를 호출하고 결과를 반환한다.
	 * </pre>
	 * @param uri REST API uri
	 * @param body http body contents
	 * @return
	 * @throws RestClientException
	 * @throws Exception
	 */
	public synchronized String submit(String uri, HttpMethod method, Object body) throws RestClientException, Exception {
		Assert.isTrue(StringUtils.isNotEmpty(uri), "uri must not be null");
		
		try {
			RestTemplate rt = new RestTemplate();
			
			ResponseEntity<String> response = rt.exchange(new URI(uri), method, setHTTPEntity(body), String.class);
			
			logger.debug("[Request URL] : {}", uri);
			logger.debug("[Response] : {}", response);
			
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
			logger.error("RestClientException has occurred.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred.", e);
			throw e;
		}
	}//end of submit()
	
	/**
	 * <pre>
	 * HTTP Body를 생성한다.
	 * </pre>
	 * @return
	 * @throws IOException 
	 */
	private HttpEntity<Object> setHTTPEntity(Object body) throws IOException {
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
	    acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
	    
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(acceptableMediaTypes);
		//requestHeaders.set(HOST_HEADER_KEY, host);
		//requestHeaders.set(AUTH_HEADER_KEY, getCredential());
		
		if (body != null) {
			logger.debug("Content Body => {}", objToJson(body));
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
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonGenerator generator = MAPPER.getJsonFactory().createJsonGenerator(outputStream, JsonEncoding.UTF8);
		MAPPER.writeValue(generator, obj);
		
		return outputStream.toString(JsonEncoding.UTF8.getJavaName());
	}
	//end of objToJson()
}
//end of PeacockRestTemplate.java