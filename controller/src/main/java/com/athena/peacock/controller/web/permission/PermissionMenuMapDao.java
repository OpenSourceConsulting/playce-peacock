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

/**
 * PermissionMenuMapDao
 *
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class PermissionMenuMapDao extends AbstractBaseDao {

	/**
	 * PermissionMenuMapDao
	 *
	 * @param
	 * @exception
	 */
	public PermissionMenuMapDao() {
	}

	public List<PermissionMenuMapDto> getPermissionMenuMapList(ExtjsGridParam gridParam){
		return sqlSession.selectList("PermissionMenuMap.getPermissionMenuMapList", gridParam);
	}
	
	public int getPermissionMenuMapListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("PermissionMenuMap.getPermissionMenuMapListTotalCount", gridParam);
	}
	
	public PermissionMenuMapDto getPermissionMenuMap(PermissionMenuMapDto param){
		return sqlSession.selectOne("PermissionMenuMap.getPermissionMenuMap", param);
	}
	
	public void insertPermissionMenuMap(PermissionMenuMapDto param){
		sqlSession.insert("PermissionMenuMap.insertPermissionMenuMap", param);
	}
	
	public void updatePermissionMenuMap(PermissionMenuMapDto param){
		sqlSession.update("PermissionMenuMap.updatePermissionMenuMap", param);
	}
	/**
	 * permId 모두 삭제함
	 * @param param
	 */
	public void deletePermissionMenuMap(PermissionMenuMapDto param){
		sqlSession.delete("PermissionMenuMap.deletePermissionMenuMap", param);
	}
}