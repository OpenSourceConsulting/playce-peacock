package com.athena.peacock.controller.common.provisioning;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.athena.peacock.controller.web.BaseControllerTest;

public class ProvisioningHandlerTest extends BaseControllerTest {
	
	private ProvisioningDetail detail;
	
	@Before
	public void setProvisioningDetail() {
		detail = new ProvisioningDetail();
		detail.setApacheHome("/usr/local/apache");
		detail.setAutoStart("Y");
		detail.setBindAddress("0.0.0.0");
		detail.setBindPort("80");
		detail.setCatalinaBase("/opt/tomcat");
		detail.setCatalinaHome("/opt/tomcat");
		detail.setClusterNetwork("224.0.0.1");
		detail.setConnectionUrl(new String[]{"127.0.0.1"});
		detail.setDatabaseType(new String[]{"mysql"});
		detail.setDataDir("/data");
		detail.setDevicePaths(new String[]{"/device"});
		detail.setDomainIp("127.0.0.1");
		detail.setEncoding("UTF-8");
		detail.setFileLocation("/home/jboss");
		detail.setFileName("catalina.txt");
		detail.setFileSystem("Windows");
		detail.setFsid("000");
		detail.setGroup("mysql");
		detail.setHeapSize("1024M");
		detail.setHighAvailability("true");
		detail.setHostName("jboss");
		detail.setHttpEnable("true");
		detail.setHttpPort("80");
		detail.setHttpsPort("443");
		detail.setInstallSeq(1);
		detail.setIp(new String[]{"127.0.0.1"});
		detail.setJavaHome("/opt/java");
		detail.setJbossHome("/opt/jboss");
		detail.setJndiName(new String[]{"home/jndi"});
		detail.setJournalSize("1024");
		detail.setJournalSize("host1");
		detail.setJvmRoute("127.0.0.1");
		detail.setLocalIPAddress("127.0.0.1");
		detail.setMachineId("20103203");
		detail.setMaxActive(new String[]{"300"});
		detail.setMaxIdle(new String[]{"10"});
		detail.setMaxPoolSize(new String[]{"10"});
		detail.setMinPoolSize(new String[]{"5"});
		detail.setMonNetworkInterface("127.0.0.1");
		detail.setMulticastPort("8080");
		detail.setNtpServer("192.168.0.18");
		detail.setOtherBindAddress("127.0.0.1");
		detail.setPassword(new String[]{"opensource"});
		detail.setPermgenSize("1024M");
		detail.setPgNum("10");
		detail.setPgpNum("10");
		detail.setPort("8080");
		detail.setPortOffset("100");
		detail.setPublicNetwork("0.0.0.0");
		detail.setServerBase("/opt/jboss");
		detail.setServerHome("/opt/servers");
		detail.setServerName("localhost");
		detail.setServerPeerID("01");
		detail.setSoftwareId(new Integer(2));
		detail.setSoftwareName("JBoss EWS(HTTPD)");
		detail.setType(new String[]{"1"});
		detail.setUdpGroupAddr("127.0.0.1");
		detail.setUriworkermap("workers.properties");
		detail.setUrlPrefix("file:///C:/Users/Owner/git/athena-peacock/controller/src/main/webapp/");
		detail.setUser("jboss");
		detail.setUserId(new Integer(1));
		detail.setUserName(new String[]{"jchoi"});
		detail.setVersion("2.2.26");
	}
	
	@Test
    public void testApacheInstall() throws Exception {
		ProvisioningHandler handler = wac.getBean(ProvisioningHandler.class);
		
		detail.setSoftwareId(new Integer(2));
		detail.setSoftwareName("httpd");
		detail.setVersion("2.2.26");
		
		handler.install(detail);
		try {
			handler.remove(detail);
		}catch(Exception e) {
			detail = null;
		}
    }
	
	@Test
    public void testTomcatInstall() throws Exception {
				
		ProvisioningHandler handler = wac.getBean(ProvisioningHandler.class);
		
		detail.setUser("jboss");
		detail.setUserId(new Integer(1));
		detail.setUserName(new String[]{"jchoi"});
		
		detail.setSoftwareId(new Integer(4));
		detail.setSoftwareName("JBoss EWS(Tomcat)");
		detail.setVersion("7.0.54");
		
		handler.install(detail);
		try {
			handler.remove(detail);
		}catch(Exception e) {
			detail = null;
		}
    }
	
	@Test
    public void testMySQLInstall() throws Exception {
				
		ProvisioningHandler handler = wac.getBean(ProvisioningHandler.class);
		
		detail.setUser("mysql");
		detail.setUserId(new Integer(1));
		detail.setUserName(new String[]{"jchoi"});
		
		detail.setSoftwareId(new Integer(4));
		detail.setSoftwareName("mysql");
		detail.setVersion("5.5");
		
		handler.install(detail);
		try {
			handler.remove(detail);
		}catch(Exception e) {
			detail = null;
		}
    }
	
	@Test
    public void testJBossInstall() throws Exception {
				
		ProvisioningHandler handler = wac.getBean(ProvisioningHandler.class);
		
		detail.setUser("jboss");
		detail.setUserId(new Integer(1));
		detail.setUserName(new String[]{"jchoi"});
		detail.setBaseTemplate("Server11");
		
			
		detail.setSoftwareId(new Integer(4));
		detail.setSoftwareName("JBoss");
		detail.setVersion("5.2.0");
		
		handler.install(detail);
		try {
			handler.remove(detail);
		}catch(Exception e) {
			detail = null;
		}
    }
	
	@Test
    public void testJCephInstall() throws Exception {
				
		ProvisioningHandler handler = wac.getBean(ProvisioningHandler.class);
		
		detail.setUser("ceph");
		detail.setUserId(new Integer(1));
		detail.setUserName(new String[]{"jchoi"});
		detail.setBaseTemplate("Server11");
		
		detail.setSoftwareId(new Integer(4));
		detail.setSoftwareName("ceph");
		detail.setVersion("0.94.2");
		detail.setReplicaSize("2");
		
		detail.setType(new String[]{"management"});
		detail.setHostname(new String[]{"ceph-management"});
		
		
		handler.install(detail);
		try {
			handler.remove(detail);
		}catch(Exception e) {
			detail = null;
		}
    }
}
