package com.athena.peacock.controller.web.alm.jenkins.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JenkinsResponseDto;

@Component
public class JenkinsClient {
	
	
	@Value("#{contextProperties['alm.jenkins.url']}")
	private String JENKINS_URL;

	static RestTemplate restTemplate = new RestTemplate();

	public JenkinsResponseDto getJobs() {

		try {
			String url = JENKINS_URL+"/api/json";

			HttpHeaders requestHeaders = new HttpHeaders();

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(acceptableMediaTypes);
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);

			// Populate the headers in an HttpEntity object to use for the
			// request
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(
					new MappingJackson2HttpMessageConverter());

			// Perform the HTTP GET request
			ResponseEntity<JenkinsResponseDto> responseEntity = restTemplate
					.exchange(url, HttpMethod.GET, requestEntity,
							JenkinsResponseDto.class);

			return responseEntity.getBody();
		} catch (Exception e) {
			// logger.debug("exception {}", e.getMessage());

		}

		return null;
	}

	public JenkinsResponseDto createJob(String name) {

		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append(JENKINS_URL);
			
			final String url = JENKINS_URL+"//createItem?name="
					+ name;
			// http://119.81.162.221:8080/jenkins/createItem?name=Test069&mode=copy&from=Test007

			HttpHeaders requestHeaders = new HttpHeaders();

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.APPLICATION_XML);
			requestHeaders.setAccept(acceptableMediaTypes);
			requestHeaders.setContentType(MediaType.APPLICATION_XML);

			// Populate the headers in an HttpEntity object to use for the
			// request
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(
					new MappingJackson2HttpMessageConverter());

			// Perform the HTTP GET request
			ResponseEntity<JenkinsResponseDto> responseEntity = restTemplate
					.exchange(url, HttpMethod.GET, requestEntity,
							JenkinsResponseDto.class);

			return responseEntity.getBody();
		} catch (Exception e) {
			// logger.debug("exception {}", e.getMessage());

		}

		return null;
	}

	public void copyJob(String jobName, String templateName, String newJobName) {

		try {
			final String url = JENKINS_URL+"/job/"
					+ jobName + "/buildWithParameters?JOB_NAME=" + newJobName;
			HttpHeaders requestHeaders = new HttpHeaders();

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(acceptableMediaTypes);
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);

			// Populate the headers in an HttpEntity object to use for the
			// request
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(
					new MappingJackson2HttpMessageConverter());

			// Perform the HTTP GET request
			ResponseEntity<String> responseEntity = restTemplate.exchange(url,
					HttpMethod.POST, requestEntity, String.class);

		} catch (Exception e) {
			// logger.debug("exception {}", e.getMessage());
			e.printStackTrace();
		}

	}

}
