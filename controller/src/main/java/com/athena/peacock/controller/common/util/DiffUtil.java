/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 9. 24.		First Draft.
 */
package com.athena.peacock.controller.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

/**
 * <pre>
 * 두개의 파일 / 문자열 / 문자열리스트의 차이를 구분한다.
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DiffUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffUtil.class);
	
	public static Patch<String> getDiff(File original, File revised) throws Exception {
		return getDiff(IOUtils.readLines(new FileInputStream(original), "UTF-8"), IOUtils.readLines(new FileInputStream(revised), "UTF-8"));
	}
	
	public static Patch<String> getDiff(String original, String revised) throws Exception {
		return getDiff(Arrays.asList(original.split("\n")), Arrays.asList(revised.split("\n")));
	}
	
	public static Patch<String> getDiff(List<String> original, List<String> revised) throws Exception {
		return DiffUtils.diff(original, revised);
	}

	public static void main(String[] args) {
		List<String> original = null;
		List<String> revised = null;
		
		String a = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
		String b = "Line 1\nLine 3 with changes\nLine 4\nLine 5 with changes and\na new line\nLine 6\nnew line 6.1\nLine 7\nLine 8\nLine 9\nLine 10 with changes";
		
		original = Arrays.asList(a.split("\n"));
		revised = Arrays.asList(b.split("\n"));
		
		Patch<String> patch = DiffUtils.diff(original, revised);
		
		for (Delta<String> delta : patch.getDeltas()) {
			LOGGER.info(delta.toString());
		}
	}
}
//end of DiffUtil.java