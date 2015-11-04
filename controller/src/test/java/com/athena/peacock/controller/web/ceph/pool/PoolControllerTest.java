package com.athena.peacock.controller.web.ceph.pool;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class PoolControllerTest extends BaseControllerTest {
	@Test
    public void testListPools() throws Exception {
    	try {
			mockMvc.perform(get("/ceph/pool/lspools").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testPoolStats() throws Exception {
    	try {
			mockMvc.perform(get("/ceph/pool/stats?name=rbd").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testPoolSize() throws Exception {
    	try {
			mockMvc.perform(get("/ceph/pool/get/size?pool=rbd").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testPoolPgNum() throws Exception {
    	try {
			mockMvc.perform(get("/ceph/pool/set/pg_num").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	// rename, pg_num, setsize, delete can't be tested.
}
