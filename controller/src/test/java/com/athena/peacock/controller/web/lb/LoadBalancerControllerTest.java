package com.athena.peacock.controller.web.lb;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class LoadBalancerControllerTest extends BaseControllerTest {
	
	@Test
    public void testList() throws Exception {
    	try {
			mockMvc.perform(get("/lb/list")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }

	@Test
    public void testListenerList() throws Exception {
    	try {
			mockMvc.perform(get("/lb/listenerlist")
					.param("loadBalancerId", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testAddListener() throws Exception {
    	try {
			mockMvc.perform(get("/lb/addListener")
					.param("loadBalancerId", "1")
					.param("listenPort", "9990")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testUpdateListener() throws Exception {
    	try {
			mockMvc.perform(get("/lb/updateListener")
					.param("loadBalancerId", "1")
					.param("listenPort", "10090")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testDeleteListener() throws Exception {
    	try {
			mockMvc.perform(get("/lb/updateListener")
					.param("loadBalancerId", "1")
					.param("listenPort", "10090")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	

}
