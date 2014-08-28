package com.athena.peacock.controller.web.alm.jenkins.clinet.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsResponseDto {

	private int numExecutors;
	private List<JobDto> jobs;

	public int getNumExecutors() {
		return numExecutors;
	}

	public void setNumExecutors(int numExecutors) {
		this.numExecutors = numExecutors;
	}

	public List<JobDto> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobDto> jobs) {
		this.jobs = jobs;
	}

}
