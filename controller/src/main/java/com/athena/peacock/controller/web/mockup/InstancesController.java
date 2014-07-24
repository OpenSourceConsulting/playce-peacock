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
 * Sang-cheon Park	2014. 7. 24.		First Draft.
 */
package com.athena.peacock.controller.web.mockup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.machine.MachineDto;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller("InstancesController")
@RequestMapping("/instance")
public class InstancesController {

	/**
	 * <pre>
	 * ExtJS Grid 목록 조회용 메소드
	 * </pre>
	 * @param jsonRes
	 * @param machine
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, MachineDto machine) throws Exception {
		
		List<MachineDto> machineList = new ArrayList<MachineDto>();
		MachineDto machineDto = new MachineDto();
		machineDto.setMachineId("i-11111111");
		machineDto.setCpuClock("");
		machineDto.setMemSize("4096 MB");
		machineDto.setCpuNum("2");
		machineDto.setHostName("Test");
//		machineDto.set
//		machineDto.set
//		machineDto.set
//		machineDto.set
//		machineDto.set
		machineList.add(machineDto);
		
		jsonRes.setTotal(machineList.size());
		jsonRes.setList(machineList);
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * ExtJS Detail 조회용 메소드
	 * </pre>
	 * @param jsonRes
	 * @param machineId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getInstance")
	public @ResponseBody DtoJsonResponse getInstance(DtoJsonResponse jsonRes, String machineId) throws Exception {
		MachineDto machineDto = new MachineDto();
		machineDto.setMachineId("i-11111111");
		machineDto.setCpuClock("");
		machineDto.setMemSize("4096 MB");
		machineDto.setCpuNum("2");
		machineDto.setHostName("Test");
//		machineDto.set
//		machineDto.set
//		machineDto.set
//		machineDto.set
//		machineDto.set
		
		jsonRes.setData(machineDto);
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * 각종 제어 명령 요청에 따른 처리 메소드
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/check")
	public ModelAndView check() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", "성공");
		
		return mav;
	}
}
//end of InstancesController.java