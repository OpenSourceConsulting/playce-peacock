package com.athena.peacock.controller.web.rhevm;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

public class RHEVMServiceTest extends BaseControllerTest {
	
	@Test
    public void testInit() throws Exception {
    	try {
    		RHEVMService service = wac.getBean(RHEVMService.class);
    		
    		service.init();
    		service.getAPI(1);
    		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
    }
	
	@Test
    public void testGetTemplate() throws Exception {
    	try {
    		RHEVMService service = wac.getBean(RHEVMService.class);
    		
    		//service.getTemplate(1, "1");
    		service.createVirtualMachine(1, "unitTestVM", "hello");
    		
		} catch (Exception e) {
			e.printStackTrace();
			//fail("Exception has occurred.");
		}
    }
	
	
}
