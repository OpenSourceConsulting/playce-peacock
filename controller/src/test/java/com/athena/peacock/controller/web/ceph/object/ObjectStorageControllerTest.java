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
 * Sang-cheon Park	2015. 11. 2.		First Draft.
 */
package com.athena.peacock.controller.web.ceph.object;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class ObjectStorageControllerTest extends BaseControllerTest {

	@Test
	public void testListBuckets() {
    	try {
			this.mockMvc.perform(get("/ceph/object/buckets").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testListObjects() {
    	try {
			this.mockMvc.perform(get("/ceph/object/objects").param("bucketName", "testBucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testGetBucket() {
    	try {
			this.mockMvc.perform(get("/ceph/object/bucket").param("bucketName", "testBucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testCreateBucket() {
    	try {
			this.mockMvc.perform(post("/ceph/object/bucket").param("bucketName", "testBucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testEmptyBucket() {
    	try {
			this.mockMvc.perform(put("/ceph/object/bucket").param("bucketName", "testBucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testDeleteBucket() {
    	try {
			this.mockMvc.perform(post("/ceph/object/bucket/delete").param("bucketName", "testBucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testCreateFolder() {
    	try {
			this.mockMvc.perform(post("/ceph/object/folder").param("bucketName", "testBucket").param("objectName", "testFolder").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testGetObjectAcl() {
    	try {
			this.mockMvc.perform(get("/ceph/object/object").param("bucketName", "testBucket").param("key", "/testObject").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testUpdateObject() {
    	try {
			this.mockMvc.perform(put("/ceph/object/object").param("bucketName", "testBucket").param("key", "/testObject").param("objectName", "testObject").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testCreateObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/object").param("bucketName", "testBucket").param("file", "testFile").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testDeleteObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/object/delete").param("bucketName", "testBucket").param("key", "/testObject").param("isFolder", "false").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testCopyObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/copy").param("bucketName", "testBucket")
					.param("key", "/testObject").param("objectName", "testObject")
					.param("targetBucketName", "testBucket").param("parentPath", "/testFolder").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

	@Test
	public void testMoveObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/move").param("bucketName", "testBucket")
					.param("key", "/testObject").param("objectName", "testObject")
					.param("targetBucketName", "testBucket").param("parentPath", "/testFolder").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			//e.printStackTrace();
			//fail("Exception has occurred.");
		}
	}

}
//end of ObjectStorageControllerTest.java