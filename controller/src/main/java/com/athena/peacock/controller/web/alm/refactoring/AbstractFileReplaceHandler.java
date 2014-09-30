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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Bong-Jin Kwon
 *
 */
public abstract class AbstractFileReplaceHandler implements FileReplaceHandler, InitializingBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String filePath;
	
	public AbstractFileReplaceHandler() {
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/* (non-Javadoc)
	 * @see com.athena.peacock.controller.web.alm.refactoring.FileReplaceHandler#getFilePath()
	 */
	@Override
	public String getFilePath() {

		return this.filePath;
	}

	/* (non-Javadoc)
	 * @see com.athena.peacock.controller.web.alm.refactoring.FileReplaceHandler#isHandler(java.lang.String)
	 */
	@Override
	public boolean isHandler(String filePath) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.filePath);
		
	}


}