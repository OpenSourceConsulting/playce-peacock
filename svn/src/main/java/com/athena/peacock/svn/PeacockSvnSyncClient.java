package com.athena.peacock.svn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.athena.peacock.svn.service.SvnService;

@Component
public class PeacockSvnSyncClient {

	@Autowired
	private SvnService svnService;

	public static void main(String[] args) {

		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/context-*.xml");
		applicationContext.registerShutdownHook();

		PeacockSvnSyncClient p = applicationContext.getBean(PeacockSvnSyncClient.class);
		p.start();
	}

	private void start() {
		svnService.sync();
	}

}
