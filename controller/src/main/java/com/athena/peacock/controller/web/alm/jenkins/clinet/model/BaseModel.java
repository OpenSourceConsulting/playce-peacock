package com.athena.peacock.controller.web.alm.jenkins.clinet.model;

import com.athena.peacock.controller.web.alm.jenkins.client.JenkinsClient;

public class BaseModel {
	
	JenkinsClient client;

	public JenkinsClient getClient() {
		return client;
	}

	public void setClient(JenkinsClient client) {
		this.client = client;
	}
}
