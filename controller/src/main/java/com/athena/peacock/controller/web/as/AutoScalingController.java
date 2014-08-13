/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 11. 1.		First Draft.
 */
package com.athena.peacock.controller.web.as;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.user.UserController;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("/as")
public class AutoScalingController {

    protected final Logger logger = LoggerFactory.getLogger(AutoScalingController.class);
    
	@Inject
	@Named("autoScalingService")
	private AutoScalingService autoScalingService;

	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, AutoScalingDto autoScaling) throws Exception {
		jsonRes.setTotal(autoScalingService.getAutoScalingListCnt(autoScaling));
		jsonRes.setList(autoScalingService.getAutoScalingList(autoScaling));
		
		return jsonRes;
	}

	@RequestMapping("/getAutoScaling")
	public @ResponseBody DtoJsonResponse getMachine(DtoJsonResponse jsonRes, Integer autoScalingId) throws Exception {
		Assert.notNull(autoScalingId, "autoScalingId can not be null.");
		
		jsonRes.setData(autoScalingService.getAutoScaling(autoScalingId));
		
		return jsonRes;
	}

	@RequestMapping("/insertAutoScaling")
	public @ResponseBody SimpleJsonResponse insertAutoScaling(HttpServletRequest request, SimpleJsonResponse jsonRes, AutoScalingDto autoScaling) throws Exception {
		// autoScalingId는 auto increment 됨.
		Assert.notNull(autoScaling.getLoadBalancerId(), "loadBalancerId can not be null.");
		Assert.notNull(autoScaling.getVmTemplateId(), "vmTemplateId can not be null.");
		
		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				autoScaling.setRegUserId(userDto.getUserId());
				autoScaling.setUpdUserId(userDto.getUserId());
			}
			
			autoScalingService.insertAutoScaling(autoScaling);
			jsonRes.setMsg("Auto Scaling 설정이 완료되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Auto Scaling 설정 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	@RequestMapping("/updateAutoScaling")
	public @ResponseBody SimpleJsonResponse updateAutoScaling(HttpServletRequest request, SimpleJsonResponse jsonRes, AutoScalingDto autoScaling) throws Exception {
		Assert.notNull(autoScaling.getAutoScalingId(), "autoScalingId can not be null.");
		Assert.notNull(autoScaling.getLoadBalancerId(), "loadBalancerId can not be null.");
		Assert.notNull(autoScaling.getVmTemplateId(), "vmTemplateId can not be null.");
		
		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				autoScaling.setRegUserId(userDto.getUserId());
				autoScaling.setUpdUserId(userDto.getUserId());
			}
			
			autoScalingService.updateAutoScaling(autoScaling);
			jsonRes.setMsg("Auto Scaling 수정이 완료되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Auto Scaling 수정 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	@RequestMapping("/deleteAutoScaling")
	public @ResponseBody SimpleJsonResponse deleteAutoScaling(HttpServletRequest request, SimpleJsonResponse jsonRes, AutoScalingDto autoScaling) throws Exception {
		Assert.notNull(autoScaling.getAutoScalingId(), "autoScalingId can not be null.");
		
		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				autoScaling.setRegUserId(userDto.getUserId());
				autoScaling.setUpdUserId(userDto.getUserId());
			}
			
			autoScalingService.deleteAutoScaling(autoScaling);
			jsonRes.setMsg("Auto Scaling 삭제가 완료되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Auto Scaling 삭제 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

}
//end of AutoScalingController.java