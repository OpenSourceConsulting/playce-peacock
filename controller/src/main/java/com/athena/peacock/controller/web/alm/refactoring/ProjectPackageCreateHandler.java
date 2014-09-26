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

import java.io.File;

import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;

/**
 * 
 * src/main/webapp/WEB-INF/web.xml 파일 수정 Handler.
 * 
 * @author Bong-Jin Kwon
 *
 */
public class ProjectPackageCreateHandler extends AbstractFileReplaceHandler {

	private DeptPackageMapper deptPackageMapper;
	
	public ProjectPackageCreateHandler() {
		setFilePath("src/main/java/com/hhi/");
	}
	

	public void setDeptPackageMapper(DeptPackageMapper deptPackageMapper) {
		this.deptPackageMapper = deptPackageMapper;
	}



	@Override
	public void replaceFile(String projectRootDir, String projectCode) {
		
		String deptPackage = this.deptPackageMapper.getDeptPackage(projectCode).replace(".", "");
		String fsep = File.separator;
		File file = new File(projectRootDir + fsep + getFilePath() + deptPackage + fsep + projectCode.toLowerCase());
		
		if(file.mkdirs()){
			logger.debug("Created "+file.getAbsolutePath());
		}else{
			logger.debug("Create Fail : "+getFilePath());
		}

	}
	/*
	public static void main(String[] args) {
		
		String EXAMPLE_TEST = "dsss<title>dddddd</title>";
		
		String pattern = "(?i)(<title.*?>)(.+?)(</title>)";
		String updated = EXAMPLE_TEST.replaceAll(pattern, "$2"); 
		System.out.println(updated);
	}
*/
}
