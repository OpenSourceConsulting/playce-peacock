/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 8. 12.		First Draft.
 */
package com.athena.peacock.controller.web.hypervisor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.user.UserController;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * 하이퍼바이저 관리를 위한 컨트롤
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("/hypervisor")
public class HypervisorController {

    protected final Logger logger = LoggerFactory.getLogger(HypervisorController.class);

	@Inject
	@Named("hypervisorService")
	private HypervisorService hypervisorService;

	/**
	 * <pre>
	 * 하이퍼바이저 목록 조회
	 * </pre>
	 * @param jsonRes
	 * @param machineId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes) throws Exception {
		List<HypervisorDto> hypervisorList = hypervisorService.getHypervisorList();
		
		jsonRes.setTotal(hypervisorList.size());
		jsonRes.setList(hypervisorList);
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * 하이퍼바이저 생성
	 * </pre>
	 * @param request
	 * @param jsonRes
	 * @param hypervisor
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertHypervisor")
	public @ResponseBody SimpleJsonResponse insertAutoScaling(HttpServletRequest request, SimpleJsonResponse jsonRes, HypervisorDto hypervisor) throws Exception {
		// hypervisorId는 auto increment 됨.
		
		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				hypervisor.setRegUserId(userDto.getUser_id());
				hypervisor.setUpdUserId(userDto.getUser_id());
			}
			
			hypervisorService.insertHypervisor(hypervisor);
			jsonRes.setMsg("Hypervisor 생성이 완료되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Hypervisor 생성 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * 하이퍼바이저 수정
	 * </pre>
	 * @param request
	 * @param jsonRes
	 * @param hypervisor
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateHypervisor")
	public @ResponseBody SimpleJsonResponse updateAutoScaling(HttpServletRequest request, SimpleJsonResponse jsonRes, HypervisorDto hypervisor) throws Exception {
		Assert.notNull(hypervisor.getHypervisorId(), "hypervisorId can not be null.");
		
		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				hypervisor.setRegUserId(userDto.getUser_id());
				hypervisor.setUpdUserId(userDto.getUser_id());
			}
			
			hypervisorService.updateHypervisor(hypervisor);
			jsonRes.setMsg("Hypervisor 수정이 완료되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Hypervisor 수정 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * 하이퍼바이저 삭제
	 * </pre>
	 * @param jsonRes
	 * @param hypervisor
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteHypervisor")
	public @ResponseBody SimpleJsonResponse deleteAutoScaling(SimpleJsonResponse jsonRes, HypervisorDto hypervisor) throws Exception {
		Assert.notNull(hypervisor.getHypervisorId(), "hypervisorId can not be null.");
		
		try {
			hypervisorService.deleteHypervisor(hypervisor.getHypervisorId());
			jsonRes.setMsg("Hypervisor 삭제가 완료되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Hypervisor 삭제 중 에러가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
}
//end of HypervisorController.java