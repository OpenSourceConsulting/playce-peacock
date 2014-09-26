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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Bong-Jin Kwon
 *
 */
public class ProjectRefactoringProcessor implements InitializingBean {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<FileReplaceHandler> replaceHandlers;
	
	/**
	 * 
	 */
	public ProjectRefactoringProcessor() {
		// TODO Auto-generated constructor stub
	}
	

	public void setReplaceHandlers(List<FileReplaceHandler> replaceHandlers) {
		this.replaceHandlers = replaceHandlers;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(this.replaceHandlers);
	}
	
	public void process(String projectRootDir, String projectCode){
		
		if(replaceHandlers == null){
			logger.warn("replaceHandlers is empty(null).");
			return;
		}
		
		logger.info(projectCode + " refactoring : " + projectRootDir);
		
		for (FileReplaceHandler fileReplaceHandler : replaceHandlers) {
			
			if(existsFile(projectRootDir, fileReplaceHandler.getFilePath())){
				fileReplaceHandler.replaceFile(projectRootDir, projectCode);
			}
		}
		
		logger.info("all file replaced!!");
	}
	
	private boolean existsFile(String projectRootDir, String filePath){
		File file = new File(projectRootDir + File.separator + filePath);
		
		return file.exists();
	}

}
