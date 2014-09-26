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
 * Bong-Jin Kwon	2014. 9. 25.		First Draft.
 */
package com.athena.peacock.controller.web.alm.refactoring;

import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * <pre>
 * 사업부별 패키지 매핑정보
 * 
 * 참조 : hhi wiki [공통]HiWAY3 Home > 02.프로젝트진행 > 00.기본자료 > 07.사업부별 코드 구분
 * </pre>
 * 
 * @author Bong-Jin Kwon
 */
public class DeptPackageMapper {

	private Map<String, String> deptPackageMap;
	
	public DeptPackageMapper() {
		// TODO Auto-generated constructor stub
	}

	public void setDeptPackageMap(Map<String, String> deptPackageMap) {
		this.deptPackageMap = deptPackageMap;
	}
	
	public String getDeptPackage(String projectCode){
		
		String key = projectCode.substring(0, 1).toUpperCase();
		
		String deptPackage = deptPackageMap.get(key);
		
		if(StringUtils.isEmpty(deptPackage)){
			return "";
		}
		return "."+deptPackage;
	}

}
