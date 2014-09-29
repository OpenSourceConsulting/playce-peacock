package com.athena.peacock.controller.web.alm.jenkins.clinet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class JobNotificationBuildDto {

	private String full_url;
	private String number;
	private String phase;
	private String status;

	public String getFull_url() {
		return full_url;
	}

	public void setFull_url(String full_url) {
		this.full_url = full_url;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
