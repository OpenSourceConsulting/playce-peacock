package com.athena.peacock.controller.web.openstack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * <pre>
 * This is a controller for OpenStack REST API
 * OpenStack API를 이용한 작업을 수행하는 컨트롤러
 * </pre>
 * @author Ji-Woong Choi
 * @version 1.0
 */
@Controller
@RequestMapping("/openstack")
public class OpenStackController {
	protected final Logger LOGGER = LoggerFactory.getLogger(OpenStackController.class);

}
