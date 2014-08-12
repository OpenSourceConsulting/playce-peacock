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

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.software.SoftwareController;

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

    protected final Logger logger = LoggerFactory.getLogger(SoftwareController.class);

	/**
	 * <pre>
	 * ExtJS Grid 목록 조회용 메소드
	 * </pre>
	 * @param jsonRes
	 * @param machine
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listInstance")
	public @ResponseBody GridJsonResponse listInstance(GridJsonResponse jsonRes, String search1, String search2, String search3) throws Exception {
		
		logger.debug(search1 + ":" + search2 + ":" + search3);
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/InstanceGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
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
	@RequestMapping("/getInstanceDescription")
	public @ResponseBody DtoJsonResponse getInstanceDescription(DtoJsonResponse jsonRes, String machineId) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/InstanceGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setData(jsonArray.get(0));
		
		return jsonRes;
	}
	
	@RequestMapping("/listInstanceSoftware")
	public @ResponseBody GridJsonResponse listInstanceSoftware(GridJsonResponse jsonRes, String instanceID) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/InstanceSoftwareGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
		return jsonRes;
	}

	@RequestMapping("/listInstanceOs")
	public @ResponseBody GridJsonResponse listInstanceOs(GridJsonResponse jsonRes, String instanceID) throws Exception {
		
	    JSONParser parser = new JSONParser();  
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/InstanceOSGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size()); 
		jsonRes.setList(jsonArray);
		
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
	@RequestMapping("/getInstanceMonitoring")
	public @ResponseBody Map<String, Object> getInstanceMonitoring(DtoJsonResponse jsonRes, String machineId) throws Exception {
		
		Random random = new Random(); 
		Map<String, Object> chartData = new HashMap<String, Object>();
		
		SimpleDateFormat fm = new SimpleDateFormat("mm:ss");
		
		chartData.put("date", fm.format(new Date()));
		chartData.put("cpu_use", random.nextInt(100));
		chartData.put("cpu_free", 100 - Integer.parseInt(String.valueOf(chartData.get("cpu_use"))));
		chartData.put("memory_use", random.nextInt(100));
		chartData.put("memory_free", 100 - Integer.parseInt(String.valueOf(chartData.get("memory_use"))));
		chartData.put("disk_use", random.nextInt(100));
		chartData.put("disk_free", 100 - Integer.parseInt(String.valueOf(chartData.get("disk_use"))));
		
		//jsonRes.setData(chartData);
		
		return chartData;
	}
	
	@RequestMapping("/listAccount")
	public @ResponseBody GridJsonResponse listAccount(GridJsonResponse jsonRes) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/AccountGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
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