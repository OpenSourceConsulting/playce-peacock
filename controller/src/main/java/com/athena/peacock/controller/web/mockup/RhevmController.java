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

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller("RhevmController")
@RequestMapping("/rhevm")
public class RhevmController {

	/**
	 * <pre>
	 * ExtJS Grid 목록 조회용 메소드
	 * </pre>
	 * @param jsonRes
	 * @param machine
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listRhevm")
	public @ResponseBody GridJsonResponse listRhevm(GridJsonResponse jsonRes) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
		return jsonRes;
	}
	
	@RequestMapping("/listRhevmVm")
	public @ResponseBody GridJsonResponse listRhevmVm(GridJsonResponse jsonRes) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmVmGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
		return jsonRes;
	}

	@RequestMapping("/getRhevmGeneral")
	public @ResponseBody DtoJsonResponse getRhevmGeneral(DtoJsonResponse jsonRes, String machineId) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmVmGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setData(jsonArray.get(0));
		
		return jsonRes;
	}

	@RequestMapping("/listRhevmNetwork")
	public @ResponseBody GridJsonResponse listRhevmNetwork(GridJsonResponse jsonRes) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmDetailNetworkGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
		return jsonRes;
	}

	@RequestMapping("/listRhevmDisk")
	public @ResponseBody GridJsonResponse listRhevmDisk(GridJsonResponse jsonRes) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmDetailDiskGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
		return jsonRes;
	}

	@RequestMapping("/listRhevmTemplate")
	public @ResponseBody GridJsonResponse listRhevmTemplate(GridJsonResponse jsonRes) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmTemplateGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setTotal(jsonArray.size());
		jsonRes.setList(jsonArray);
		
		return jsonRes;
	}

	@RequestMapping("/getRhevmTemplateGeneral")
	public @ResponseBody DtoJsonResponse getRhevmTemplateGeneral(DtoJsonResponse jsonRes, String machineId) throws Exception {
		
	    JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader(getClass().getResource("/json/rhevmTemplateGridData.json").getPath()));

	    JSONArray jsonArray =  (JSONArray) obj;

		jsonRes.setData(jsonArray.get(0));
		
		return jsonRes;
	}

}
//end of RhevmController.java