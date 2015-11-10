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
 * Bong-Ji Kwon 			            First Draft.
 */
package com.athena.peacock.controller.web.permission;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.menu.MenuDto;

/**
 * PermissionDao
 *
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class PermissionDao extends AbstractBaseDao {

	/**
	 * PermissionDao
	 *
	 * @param
	 * @exception
	 */
	public PermissionDao() {
	}

	public List<PermissionDto> getPermissionList(ExtjsGridParam gridParam){
		return sqlSession.selectList("Permission.getPermissionList", gridParam);
	}
	
	public int getPermissionListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("Permission.getPermissionListTotalCount", gridParam);
	}
	
	public List<MenuDto> getPermissionMenuList(int permId){
		
		return sqlSession.selectList("Permission.getPermissionMenuList", permId);
	}
	
	public PermissionDto getPermission(PermissionDto param){
		return sqlSession.selectOne("Permission.getPermission", param);
	}
	
	public List<MenuDto> getUserMenuPermissions(int userId){
		return sqlSession.selectList("Permission.getUserMenuPermissions", userId);
	}
	
	public void insertPermission(PermissionDto param){
		sqlSession.insert("Permission.insertPermission", param);
	}
	
	public void updatePermission(PermissionDto param){
		sqlSession.update("Permission.updatePermission", param);
	}
	
	public void deletePermission(PermissionDto param){
		sqlSession.delete("Permission.deletePermission", param);
	}
}