package com.athena.peacock.controller.web.alm.jenkins.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JenkinsResponseDto;

@Component
public class JenkinsClient {

	@Value("#{contextProperties['alm.jenkins.url']}")
	private String JENKINS_URL;

	@Value("#{contextProperties['alm.jenkins.id']}")
	private String JENKINS_ID;

	@Value("#{contextProperties['alm.jenkins.pw']}")
	private String JENKINS_PW;

	@Value("#{contextProperties['alm.jenkins.copyjob']}")
	private String JENKINS_COPYJOB;

	@Value("#{contextProperties['alm.jenkins.permissionjob']}")
	private String JENKINS_PERMISSIONJOB;

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

	public JenkinsResponseDto getJobs() {

		try {
			String url = JENKINS_URL + "/api/json";

			/*
			 * HttpHeaders requestHeaders = new HttpHeaders();
			 * 
			 * List<MediaType> acceptableMediaTypes = new
			 * ArrayList<MediaType>();
			 * acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
			 * requestHeaders.setAccept(acceptableMediaTypes);
			 * requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			 * 
			 * // Populate the headers in an HttpEntity object to use for the //
			 * request HttpEntity<?> requestEntity = new
			 * HttpEntity<Object>(requestHeaders);
			 */
			// Create a new RestTemplate instance
			RestTemplate restTemplate = getRestTemplate();
			restTemplate.getMessageConverters().add(
					new MappingJackson2HttpMessageConverter());

			// Perform the HTTP GET request

			ResponseEntity<JenkinsResponseDto> responseEntity = restTemplate
					.exchange(url, HttpMethod.GET, null,
							JenkinsResponseDto.class);

			return responseEntity.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			// logger.debug("exception {}", e.getMessage());

		}

		return null;
	}

	public void copyJob(String templateName, String newJobName) {

		try {
			StringBuffer sb = new StringBuffer();

			sb.append(JENKINS_URL);
			sb.append("/job/");
			sb.append(JENKINS_COPYJOB);
			sb.append("/buildWithParameters?JOB_NAME=");
			sb.append(newJobName);

			String url = sb.toString();
			// JENKINS_URL + "/job/" + JENKINS_COPYJOB +
			// "/buildWithParameters?JOB_NAME=" + newJobName;
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

		/*
		 * public ClientHttpRequestFactory getClientHttpRequestFactory() {
		 * 
		 * HttpComponentsClientHttpRequestFactory factory = new
		 * HttpComponentsClientHttpRequestFactory(getHttpClient());
		 * 
		 * 
		 * 
		 * return factory; }
		 */

	}

	private RestTemplate getRestTemplate() {

		RestTemplate restTemplate = new RestTemplate(httpRequestFactory());

		return restTemplate;
	}

	private ClientHttpRequestFactory httpRequestFactory() {
		System.out.println("!!!!!!!!!!!!!");
		ClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory(
				httpClient());

		return rf;
	}

	private HttpClient httpClient() {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
				AuthScope.ANY_PORT), new UsernamePasswordCredentials("jshan",
				"gks778899"));
		httpClient.setCredentialsProvider(credentialsProvider);

		PreemptiveAuth auth = new PreemptiveAuth();
		httpClient.addRequestInterceptor(auth, 0);

		return httpClient;

	}

	public class PreemptiveAuth implements HttpRequestInterceptor {

		@Override
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			// TODO Auto-generated method stub
			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);

			if (authState.getAuthScheme() == null) {
				AuthScheme authScheme = (AuthScheme) context
						.getAttribute("preemptive-auth");
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context
						.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if (authScheme != null) {
					Credentials creds = credsProvider
							.getCredentials(new AuthScope(targetHost
									.getHostName(), targetHost.getPort()));
					if (creds == null) {
						throw new HttpException(
								"No credentials for preemptive authentication");
					}
					authState.update(authScheme, creds);
				}
			}
		}

	}

}
