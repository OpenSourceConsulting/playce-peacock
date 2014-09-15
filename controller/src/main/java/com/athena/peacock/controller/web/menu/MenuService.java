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
package com.athena.peacock.controller.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.TreeNode;

/**
 * <pre>
 * 
 * </pre>
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class MenuService {

	@Autowired
	private MenuDao dao;
	
	public MenuService() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 2depth tree 기준.
	 * 
	 * @return
	 */
	public List<TreeNode> getAllMenuTree(){
		
		List<MenuDto> dbList = dao.getAllMenuTree();
		
		return convertTreeData(dbList);
	}
	
	public void insertMenu(MenuDto user){
		dao.insertMenu(user);
	}
	
	public List<MenuDto> getMenuList(ExtjsGridParam gridParam){
		return dao.getMenuList(gridParam);
	}
	
	public int getMenuListTotalCount(ExtjsGridParam gridParam){
		
		return dao.getMenuListTotalCount(gridParam);
	}
	
	public MenuDto getMenu(MenuDto param){
		return dao.getMenu(param);
	}
	
	public void updateMenu(MenuDto param){
		dao.updateMenu(param);
	}
	
	public void deleteMenu(MenuDto param){
		dao.deleteMenu(param);
	}
	
	public static List<TreeNode> convertTreeData(List<MenuDto> dbList){
		
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		
		TreeNode preNode = null;
		MenuDto preMenu = null;
		
		for (MenuDto menuDto : dbList) {
			
			TreeNode treeNode = new TreeNode();
			treeNode.put("menuId", menuDto.getMenuId());
			treeNode.put("menuNm", menuDto.getMenuNm());
			treeNode.put("thread", menuDto.getThread());
			treeNode.put("read", menuDto.getReadYn());
			treeNode.put("isRead", menuDto.isRead());
			treeNode.put("write", menuDto.getWriteYn());
			treeNode.put("isWrite", menuDto.isWrite());
			
			if(menuDto.getThread().length() == 2){
				treeList.add(treeNode);
				
				preNode = treeNode;
				preMenu = menuDto;
				
			}else if(preMenu.getThread().length() < menuDto.getThread().length()){
				preNode.addChild(treeNode);
			}
			
		}
		
		return treeList;
	}

}
//end of MenuService.java