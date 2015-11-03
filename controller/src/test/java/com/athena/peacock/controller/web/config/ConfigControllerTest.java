package com.athena.peacock.controller.web.config;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class ConfigControllerTest extends BaseControllerTest {
	
	@Test
    public void testConfigFileNames() throws Exception {
    	try {
			mockMvc.perform(get("/config/getConfigFileNames")
					.param("machineId", "1")
					.param("softwareId",  "1")
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
    public void testConfigFileVersions() throws Exception {
    	try {
			mockMvc.perform(get("/config/getConfigFileVersions")
					.param("machineId", "1")
					.param("softwareId",  "1")
					.param("installSeq", "1")
					.param("configFileName", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testConfig() throws Exception {
    	try {
			mockMvc.perform(get("/config/getConfigDetail")
					.param("machineId", "1")
					.param("softwareId",  "1")
					.param("installSeq", "1")
					.param("configFileName", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testSystemConfig() throws Exception {
    	try {
			mockMvc.perform(get("/config/getSystemConfig")
					.param("machineId", "1")
					.param("configFileLocation",  "1")
					.param("configFileName", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testDiff() throws Exception {
    	try {
			mockMvc.perform(get("/config/diff")
					.param("softwareId",  "1")					
					.param("machineId", "1")
					.param("installSeq",  "1")
					.param("configFileId", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
}
