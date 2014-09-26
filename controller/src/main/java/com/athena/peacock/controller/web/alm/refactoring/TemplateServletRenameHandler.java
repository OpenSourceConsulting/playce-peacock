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
import java.nio.file.Files;

import org.apache.tools.ant.taskdefs.optional.ReplaceRegExp;

/**
 * 
 * src/main/webapp/WEB-INF/config/template-servlet.xml 파일 수정 Handler.
 * 
 * @author Bong-Jin Kwon
 *
 */
public class TemplateServletRenameHandler extends AbstractFileReplaceHandler {

	
	public TemplateServletRenameHandler() {
		setFilePath("src/main/webapp/WEB-INF/config/template-servlet.xml");
	}
	

	/* (non-Javadoc)
	 * @see com.bong.svn.project_refactor.FileReplaceHandler#replaceFile(java.lang.String, java.lang.String)
	 */
	@Override
	public void replaceFile(String projectRootDir, String projectCode) {
		
		File file = new File(projectRootDir + File.separator + getFilePath());
		
		boolean succ = file.renameTo(new File(projectRootDir + File.separator + "src/main/webapp/WEB-INF/config/"+projectCode+"-servlet.xml"));
		
		if(succ){
			logger.debug("Replaced "+getFilePath()+" File!!!");
		}else{
			logger.debug("Replace Fail : "+getFilePath());
		}

	}
	

}
