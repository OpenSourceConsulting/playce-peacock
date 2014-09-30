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
 * Bong-Jin Kwon	2013. 9. 24.		First Draft.
 */
package com.athena.peacock.controller.web.alm.confluence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserAddDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Controller
@RequestMapping("/alm/confluence")
public class AlmConfluenceController {

	@Autowired
	private AlmConfluenceService service;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AlmConfluenceController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/space", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse list(ExtjsGridParam gridParam) {
		return service.getList(gridParam);
	}
	
	@RequestMapping(value = "/space/{spacekey}/permission", method = RequestMethod.GET)
	public @ResponseBody Object getPermmision(@PathVariable String spacekey) {
		return service.getPermissions(spacekey);
	}
	
	@RequestMapping(value = "/space/{spacekey}/permission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DtoJsonResponse addPermission(@RequestBody  AlmUserAddDto userData, BindingResult result){
		return null;
	}
	
	/*@RequestMapping(value = "/space/{spaceKey}/{groupname}/test", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DtoJsonResponse addPermission(@PathVariable String spaceKey, @PathVariable String groupname){
		
		service.addPermissions(groupname, spaceKey, "VIEWSPACE");
		return null;
	}*/
}
// end of AlmUserController.java