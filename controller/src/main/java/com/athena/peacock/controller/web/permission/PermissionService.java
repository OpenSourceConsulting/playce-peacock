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
 * Bong-Jin Kwon			            First Draft.
 */
package com.athena.peacock.controller.web.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class PermissionService {

	@Autowired
	private PermissionDao dao;
	
	public PermissionService() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertPermission(PermissionDto user){
		dao.insertPermission(user);
	}
	
	public List<PermissionDto> getPermissionList(ExtjsGridParam gridParam){
		return dao.getPermissionList(gridParam);
	}
	
	public int getPermissionListTotalCount(ExtjsGridParam gridParam){
		
		return dao.getPermissionListTotalCount(gridParam);
	}
	
	public PermissionDto getPermission(PermissionDto param){
		return dao.getPermission(param);
	}
	
	public void updatePermission(PermissionDto param){
		dao.updatePermission(param);
	}
	
	public void deletePermission(PermissionDto param){
		dao.deletePermission(param);
	}

}
//end of PermissionService.java