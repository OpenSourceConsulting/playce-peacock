package com.athena.peacock.controller.web.alm.crowd;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class AlmUserControllerTest extends BaseControllerTest {
	
	@Test
    public void testUserList() throws Exception {
    	try {
			mockMvc.perform(get("/alm/usermanagement")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }

	@Test
    public void testUser() throws Exception {
    	try {
			mockMvc.perform(get("/alm/usermanagement/jchoi")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testUserGroup() throws Exception {
    	try {
			mockMvc.perform(get("/alm/usermanagement/jchoi/groups")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testAddUser() throws Exception {
    	try {
			mockMvc.perform(post("/alm/usermanagement")
					.param("name",  "jchoi")
					.param("firstName", "Ji-Woong")
					.param("lastName",  "Choi")
					.param("email",  "jchoi@osci.kr")
					.param("password",  "opensource")
					.param("confirmPassword",  "opensource")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testModifyUser() throws Exception {
    	try {
    		mockMvc.perform(put("/alm/usermanagement")
					.param("name",  "jchoi")
					.param("firstName", "Ji-Woong")
					.param("lastName",  "Choi")
					.param("email",  "jchoi@osci.kr")
					.param("password",  "opensource")
					.param("confirmPassword",  "opensource")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testAddUserToGroup() throws Exception {
    	try {
			mockMvc.perform(delete("/alm/usermanagement/jchoi")
					.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }

}
