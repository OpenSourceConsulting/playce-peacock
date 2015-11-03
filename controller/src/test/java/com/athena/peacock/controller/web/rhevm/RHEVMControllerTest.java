package com.athena.peacock.controller.web.rhevm;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class RHEVMControllerTest extends BaseControllerTest {
	
	@Test
    public void testList() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm//vms/list")
					.param("hypervisorId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testVMInfo() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/info")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testVMNics() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/nics")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testVMDisks() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/disks")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testTemplateList() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/templates")
					.param("hypervisorId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testTemplateInfo() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/templates/info")
					.param("hypervisorId",  "1")
					.param("templateId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testTemplateNics() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/templates/nics")
					.param("hypervisorId",  "1")
					.param("templateId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testTemplateDisks() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/templates/disks")
					.param("hypervisorId",  "1")
					.param("templateId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testDataCenter() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/datacenter")
					.param("hypervisorId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testHostCluster() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/hostcluster")
					.param("hypervisorId",  "1")
					.param("dataCenterId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testStartVirtualMachine() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/start")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testStopVirtualMachine() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/stop")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testShutdownVirtualMachine() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/shutdown")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testRemoveVirtualMachine() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/remove")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testExportVirtualMachine() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/export")
					.param("hypervisorId",  "1")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testMakeTemplate() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/makeTemplate")
					.param("hypervisorId",  "1")
					.param("name", "test")
					.param("vmId", "001")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testBackup() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/vms/backup")
					.param("host",  "host")
					.param("port", "9000")
					.param("username", "osc")
					.param("password", "osc")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testRemoveTemplate() throws Exception {
    	try {
			mockMvc.perform(get("/rhevm/templates/remove")
					.param("hypervisorId",  "1")
					.param("templateId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
}
