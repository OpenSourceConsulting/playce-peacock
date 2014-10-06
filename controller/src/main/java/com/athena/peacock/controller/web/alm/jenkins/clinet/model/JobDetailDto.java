package com.athena.peacock.controller.web.alm.jenkins.clinet.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDetailDto {

	private String description;
	private String displayName;
	private String displayNameOrNull;
	private String name;
	private String url;
	private boolean buildable;

	private List<JobDetailDto> builds;
	private JobDetailDto lastBuild;
	private JobDetailDto lastCompletedBuild;
	private JobDetailDto lastFailedBuild;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayNameOrNull() {
		return displayNameOrNull;
	}

	public void setDisplayNameOrNull(String displayNameOrNull) {
		this.displayNameOrNull = displayNameOrNull;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isBuildable() {
		return buildable;
	}

	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}

	public List<JobDetailDto> getBuilds() {
		return builds;
	}

	public void setBuilds(List<JobDetailDto> builds) {
		this.builds = builds;
	}

	public JobDetailDto getLastBuild() {
		return lastBuild;
	}

	public void setLastBuild(JobDetailDto lastBuild) {
		this.lastBuild = lastBuild;
	}

	public JobDetailDto getLastCompletedBuild() {
		return lastCompletedBuild;
	}

	public void setLastCompletedBuild(JobDetailDto lastCompletedBuild) {
		this.lastCompletedBuild = lastCompletedBuild;
	}

	public JobDetailDto getLastFailedBuild() {
		return lastFailedBuild;
	}

	public void setLastFailedBuild(JobDetailDto lastFailedBuild) {
		this.lastFailedBuild = lastFailedBuild;
	}

}
