package com.athena.peacock.controller.web.machine;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class MachineControllerTest extends BaseControllerTest {
	
	@Test
    public void testList() throws Exception {
    	try {
			mockMvc.perform(get("/machine/list")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }

	@Test
    public void testMachine() throws Exception {
    	try {
			mockMvc.perform(get("/machine/getMachine")
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
    public void testAdditionalInfo() throws Exception {
    	try {
			mockMvc.perform(get("/machine/getAdditionalInfo")
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
    public void testUpdateMachine() throws Exception {
    	try {
			mockMvc.perform(get("/machine/updateMachine")
					.param("machineId", "1")
					.param("displayName", "testMachine")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testAgentStart() throws Exception {
    	try {
			mockMvc.perform(get("/machine/agentStart")
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
    public void testAgentStop() throws Exception {
    	try {
			mockMvc.perform(get("/machine/agentStop")
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
    public void testCli() throws Exception {
    	try {
			mockMvc.perform(get("/machine/cli")
					.param("machineId", "a110bb8f-9494-4ed4-949d-9d6a2defb4df")
					.param("command", "ls")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testGroupList() throws Exception {
    	try {
			mockMvc.perform(get("/machine/getGroupList")
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
    public void testAccountList() throws Exception {
    	try {
			mockMvc.perform(get("/machine/getAccountList")
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
    public void testRemoveGroup() throws Exception {
    	try {
			mockMvc.perform(get("/machine/removeGroup")
					.param("machineId", "1")
					.param("group", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testRemoveAccount() throws Exception {
    	try {
			mockMvc.perform(get("/machine/removeAccount")
					.param("machineId", "1")
					.param("account", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testCreateGroup() throws Exception {
    	try {
			mockMvc.perform(get("/machine/createGroup")
					.param("machineId", "1")
					.param("group", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testCreateAccount() throws Exception {
    	try {
			mockMvc.perform(get("/machine/createAccount")
					.param("machineId", "1")
					.param("account", "osc")
					.param("passwd", "osc")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testFstab() throws Exception {
    	try {
			mockMvc.perform(get("/machine/getFstab")
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
    public void testCrontab() throws Exception {
    	try {
			mockMvc.perform(get("/machine/getCrontab")
					.param("machineId", "a110bb8f-9494-4ed4-949d-9d6a2defb4df")
					.param("account", "osc")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testEditCrontab() throws Exception {
    	try {
			mockMvc.perform(get("/machine/editCrontab")
					.param("machineId", "a110bb8f-9494-4ed4-949d-9d6a2defb4df")
					.param("account", "peacock")
					.param("contents", "* * * * *")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testCommand() throws Exception {
    	try {
			mockMvc.perform(get("/machine/command")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
}
