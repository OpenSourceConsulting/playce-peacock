package com.athena.peacock.controller.web.alm.jenkins.client;

import java.io.IOException;
import java.net.URI;

import com.athena.peacock.controller.web.alm.jenkins.clinet.model.JenkinsResponseDto;

public class JenkinsServer {

	private final JenkinsHttpClient client;

	public JenkinsServer(URI serverUri) {
		this(new JenkinsHttpClient(serverUri));
	}

	public JenkinsServer(URI serverUri, String username, String passwordOrToken) {
		this(new JenkinsHttpClient(serverUri, username, passwordOrToken));
	}

	public JenkinsServer(JenkinsHttpClient client) {
		this.client = client;
	}

	public boolean isRunning() {
		try {
			client.get("/");
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public JenkinsResponseDto getJobs() throws IOException {

		return client.get("/api/json", JenkinsResponseDto.class);

	}

}
