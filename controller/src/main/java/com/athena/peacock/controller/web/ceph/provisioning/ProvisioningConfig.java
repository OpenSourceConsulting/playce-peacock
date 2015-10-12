package com.athena.peacock.controller.web.ceph.provisioning;

public class ProvisioningConfig {
	private String ansibleConfig;

	public String getAnsibleConfig() {
		return ansibleConfig;
	}

	public void setAnsibleConfig(String ansibleConfig) {
		this.ansibleConfig = ansibleConfig;
	}

	@Override
	public String toString() {
		return "ProvisioningConfig [ansibleConfig=" + ansibleConfig + "]";
	}
}
