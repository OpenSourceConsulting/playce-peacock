package com.athena.peacock.controller.web.software;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class SoftwareControllerTest extends BaseControllerTest {
	
	@Test
    public void testList() throws Exception {
    	try {
			mockMvc.perform(get("/software/list")
					.param("machineId",  "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testGetNames() throws Exception {
    	try {
			mockMvc.perform(get("/software/getNames")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testGetVersions() throws Exception {
    	try {
			mockMvc.perform(get("/software/getVersions")
					.param("softwareName", "JBoss EAP")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testGetInstallLog() throws Exception {
    	try {
			mockMvc.perform(get("/software/getInstallLog")
					.param("softwareId", "4")
					.param("machineId", "a110bb8f-9494-4ed4-949d-9d6a2defb4df")
					.param("installSeq", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testInstall() throws Exception {
    	try {
			mockMvc.perform(get("/software/install")
					.param("softwareId", "4")
					.param("machineId", "a110bb8f-9494-4ed4-949d-9d6a2defb4df")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testRemove() throws Exception {
    	try {
			mockMvc.perform(get("/software/remove")
					.param("softwareId", "4")
					.param("machineId", "001")
					.param("installSeq", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testConnectorProp() throws Exception {
    	try {
			mockMvc.perform(get("/software/getConnectorProp")
					.param("account", "osc")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }

}
