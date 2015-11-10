package com.athena.peacock.controller.common.core.action.support;


import org.junit.Test;

import com.athena.peacock.common.core.action.support.PropertyUtil;
import com.athena.peacock.controller.web.BaseControllerTest;

public class PropertyUtilTest extends BaseControllerTest {
	
	@Test
    public void testThreadLocal() throws Exception {
		try {
			PropertyUtil util = new PropertyUtil();
			PropertyUtil.getProperty("key");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
