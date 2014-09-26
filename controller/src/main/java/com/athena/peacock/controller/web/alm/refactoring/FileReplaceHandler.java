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


/**
 * @author Bong-Jin Kwon
 *
 */
public interface FileReplaceHandler {

	/**
	 * 수정할 파일경로 (프로젝트 ROOT PATH 기준 상대경로)
	 * @return
	 */
	String getFilePath();
	
	
	boolean isHandler(String filePath);
	
	/**
	 * 
	 * @param projectRootDir 프로젝트 ROOT PATH
	 * @param projectCode 프로젝트 코드(아마도 대문자??)
	 */
	void replaceFile(String projectRootDir, String projectCode);
	
}
