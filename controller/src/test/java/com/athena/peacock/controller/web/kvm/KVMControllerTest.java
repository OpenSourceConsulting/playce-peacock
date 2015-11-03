package com.athena.peacock.controller.web.kvm;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class KVMControllerTest extends BaseControllerTest {
	
	@Test
    public void testVMList() throws Exception {
    	try {
			mockMvc.perform(get("/kvm/vms/list")
					.param("hypervisorId", "1")
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
			mockMvc.perform(get("/kvm/vms/list")
					.param("hypervisorId", "1")
					.param("vmId", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testVMNic() throws Exception {
    	try {
			mockMvc.perform(get("/kvm/vms/nics")
					.param("hypervisorId", "1")
					.param("vmId", "1")
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
			mockMvc.perform(get("/kvm/vms/disks")
					.param("hypervisorId", "1")
					.param("vmId", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
}
