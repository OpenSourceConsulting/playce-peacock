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
package com.athena.peacock.controller.web.menu;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.menu.MenuDto;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;

/**
 * MenuDao
 *
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Repository
public class MenuDao extends AbstractBaseDao {

	/**
	 * MenuDao
	 *
	 * @param
	 * @exception
	 */
	public MenuDao() {
	}
	
	public List<MenuDto> getAllMenuTree(){
		return sqlSession.selectList("Menu.getAllMenuTree");
	}

	public List<MenuDto> getMenuList(ExtjsGridParam gridParam){
		return sqlSession.selectList("Menu.getMenuList", gridParam);
	}
	
	public int getMenuListTotalCount(ExtjsGridParam gridParam){
		
		return sqlSession.selectOne("Menu.getMenuListTotalCount", gridParam);
	}
	
	public MenuDto getMenu(MenuDto param){
		return sqlSession.selectOne("Menu.getMenu", param);
	}
	
	public void insertMenu(MenuDto param){
		sqlSession.insert("Menu.insertMenu", param);
	}
	
	public void updateMenu(MenuDto param){
		sqlSession.update("Menu.updateMenu", param);
	}
	
	public void deleteMenu(MenuDto param){
		sqlSession.delete("Menu.deleteMenu", param);
	}
}