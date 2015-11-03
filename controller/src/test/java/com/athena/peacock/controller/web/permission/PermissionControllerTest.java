package com.athena.peacock.controller.web.permission;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class PermissionControllerTest extends BaseControllerTest {
	
	@Test
    public void testList() throws Exception {
    	try {
			mockMvc.perform(get("/permission/list")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testUserList() throws Exception {
    	try {
			mockMvc.perform(get("/permission/userlist")
					.param("permId", "1")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testCreate() throws Exception {
    	try {
			mockMvc.perform(get("/permission/create")
					.param("permId", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testUpdate() throws Exception {
    	try {
			mockMvc.perform(get("/permission/update")
					.param("permId", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testUpdateMenus() throws Exception {
    	try {
			mockMvc.perform(get("/permission/updatemenus")
					.param("permId", "0")
					.param("permMenus", "menu")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testDelete() throws Exception {
    	try {
			mockMvc.perform(get("/permission/delete")
					.param("permId", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testInsertUser() throws Exception {
    	try {
			mockMvc.perform(get("/permission/insertuser")
					.param("permId", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testDeleteUser() throws Exception {
    	try {
			mockMvc.perform(get("/permission/deleteuser")
					.param("permId", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testPermission() throws Exception {
    	try {
			mockMvc.perform(get("/permission/getPermission")
					.param("permId", "0")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
}
