package com.athena.peacock.controller.web.alm.jenkins.client;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JenkinsHttpClient {

	private URI uri;
	private BasicHttpContext localContext;
	private HttpResponseValidator httpResponseValidator;
	private HttpResponseContent contentExtractor;

	private ObjectMapper mapper;
	private String context;
	
	private String username;
	private String password;
	
	private final PoolingClientConnectionManager cm = new PoolingClientConnectionManager();

	public JenkinsHttpClient(URI uri) {
		this.context = uri.getPath();
		if (!context.endsWith("/")) {
			context += "/";
		}
		this.uri = uri;
		this.mapper = getDefaultMapper();
		this.httpResponseValidator = new HttpResponseValidator();
		this.contentExtractor = new HttpResponseContent();
	}

	public JenkinsHttpClient(URI uri, String username, String password) {
		this(uri);
		
		this.username = username;
		this.password = password;
		
		if (isNotBlank(username)) {
			CredentialsProvider provider = getHttpClient().getCredentialsProvider();
			AuthScope scope = new AuthScope(uri.getHost(), uri.getPort(), "realm");
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
			provider.setCredentials(scope, credentials);

			localContext = new BasicHttpContext();
			localContext.setAttribute("preemptive-auth", new BasicScheme());

			getHttpClient().addRequestInterceptor(new PreemptiveAuth(), 0);
		}
	}


	public <T> T get(String path, Class<T> cls) throws IOException {

		HttpGet getMethod = new HttpGet(api(path));
		HttpResponse response = getHttpClient().execute(getMethod, localContext);
		try {
			httpResponseValidator.validateResponse(response);

			return objectFromResponse(cls, response);
		} finally {
			EntityUtils.consume(response.getEntity());
			releaseConnection(getMethod);
		}
	}

	public String get(String path) throws IOException {
		HttpGet getMethod = new HttpGet(api(path));
		HttpResponse response = getHttpClient().execute(getMethod, localContext);
		try {
			httpResponseValidator.validateResponse(response);
			return contentExtractor.contentAsString(response);
		} finally {
			EntityUtils.consume(response.getEntity());
			releaseConnection(getMethod);
		}
	}

	public void post(String path, Map<String, String> parameter)
			throws IOException {

		path = path + "?" + getParameter(parameter);
		System.out.println(path);

		HttpPost request = new HttpPost(path);
		HttpResponse response = getHttpClient().execute(request, localContext);

		try {

			httpResponseValidator.validateResponse(response);

		} finally {
			EntityUtils.consume(response.getEntity());
			releaseConnection(request);
		}
	}

	private String urlJoin(String path1, String path2) {
		if (!path1.endsWith("/")) {
			path1 += "/";
		}
		if (path2.startsWith("/")) {
			path2 = path2.substring(1);
		}
		return path1 + path2;
	}

	private URI api(String path) {
		if (!path.toLowerCase().matches("https?://.*")) {
			path = urlJoin(this.context, path);
		}
		if (!path.contains("?")) {
			path = urlJoin(path, "api/json");
		} else {
			String[] components = path.split("\\?", 2);
			path = urlJoin(components[0], "api/json") + "?" + components[1];
		}
		return uri.resolve("/").resolve(path);
	}

	private <T> T objectFromResponse(Class<T> cls, HttpResponse response)
			throws IOException {

		InputStream content = contentExtractor.contentAsInputStream(response);
		T result = mapper.readValue(content, cls);
		return result;
	}

	private ObjectMapper getDefaultMapper() {
		ObjectMapper mapper = new ObjectMapper();
		DeserializationConfig deserializationConfig = mapper
				.getDeserializationConfig();
		mapper.setDeserializationConfig(deserializationConfig
				.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
		return mapper;
	}

	private void releaseConnection(HttpRequestBase httpRequestBase) {
		httpRequestBase.releaseConnection();
	}

	private String getParameter(Map<String, String> parameter) {

		StringBuffer sb = new StringBuffer();

		Iterator<String> keys = parameter.keySet().iterator();

		while (keys.hasNext()) {
			String key = keys.next();

			String value = parameter.get(key);
			sb.append(key);
			sb.append("=");
			sb.append(value);

			if (keys.hasNext()) {
				sb.append("&");
			}

		}

		return sb.toString();

	}
	
	private DefaultHttpClient getHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient(cm);
		
		if (isNotBlank(username) && client.getRequestInterceptorCount() == 0) {
			AuthScope scope = new AuthScope(uri.getHost(), uri.getPort(), "realm");
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
			getHttpClient().getCredentialsProvider().setCredentials(scope, credentials);
			
			client.addRequestInterceptor(new PreemptiveAuth(), 0);
		}
		
		return client;
	}
}
