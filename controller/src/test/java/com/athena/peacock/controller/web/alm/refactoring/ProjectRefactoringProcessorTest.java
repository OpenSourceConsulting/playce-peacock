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

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.athena.peacock.controller.web.alm.svn.SvnExport;
import com.athena.peacock.controller.web.alm.svn.SvnImport;

/**
 * @author Bong-Jin Kwon
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/athena/peacock/controller/web/alm/refactoring/testContext-refactoring.xml" })
public class ProjectRefactoringProcessorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Autowired
	private ProjectRefactoringProcessor processor;

	@Test
	public void testProcess() {
		
		String SVN_EX_URL = "http://localhost/svn/idkbj/sample";
		String SVN_IM_URL = "http://localhost/svn/idkbj/XABCD";// aleady exists
		String SVN_ID = "idkbj";
		String SVN_PW = "bong0524";
		String projectCode = "XABCD";
		String projectRootDir = "D:\\project\\2014_hhi\\eclipse-jee-kepler-SR2-win32-x86_64\\workspace\\"+projectCode;
		
		//--------- svn init
		SvnExport.setupLibrary();
		
		try {
			
			//--------- svn export
			SvnExport.export(SVN_EX_URL, SVN_ID, SVN_PW, projectRootDir);
			
			
			
			//--------- project refactoring
			processor.process(projectRootDir, projectCode);
			
			
			
			//--------- svn import
			SvnImport.importCommit(SVN_IM_URL, SVN_ID, SVN_PW, projectRootDir);
			
        } catch (Exception e) {
        	e.printStackTrace();
            fail(e.toString());
        }
		
		
	}

}
