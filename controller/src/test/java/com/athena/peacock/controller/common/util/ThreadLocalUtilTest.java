package com.athena.peacock.controller.common.util;


import org.junit.Test;

import com.athena.peacock.controller.web.BaseControllerTest;

public class ThreadLocalUtilTest extends BaseControllerTest {
	
	@Test
    public void testThreadLocal() throws Exception {
		try {
			ThreadLocalUtil.add("key",  "value");
			ThreadLocalUtil.get("key");
			ThreadLocalUtil.isExist("key");
			ThreadLocalUtil.clearSharedObject();
			ThreadLocalUtil.getThreadLocalKeys();
			ThreadLocalUtil.getThreadLocalValues();
			ThreadLocalUtil.size();
			ThreadLocalUtil.toPrintString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
