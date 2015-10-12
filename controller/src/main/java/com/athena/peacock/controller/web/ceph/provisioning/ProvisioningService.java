package com.athena.peacock.controller.web.ceph.provisioning;

import org.springframework.stereotype.Service;

@Service
public class ProvisioningService {
	
	public void installManagement(ProvisioningConfig config) {
		System.out.println(config);		
	}

}
