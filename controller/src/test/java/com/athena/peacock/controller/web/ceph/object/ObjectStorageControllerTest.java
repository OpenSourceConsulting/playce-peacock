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

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;

import com.athena.peacock.controller.web.BaseControllerTest;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ObjectStorageControllerTest extends BaseControllerTest {

	@Test
	public void test_01_CreateBucket() {
    	try {
			this.mockMvc.perform(post("/ceph/object/bucket").param("bucketName", "test-bucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));

			this.mockMvc.perform(post("/ceph/object/bucket").param("bucketName", "test-bucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_02_ListBuckets() {
    	try {
			this.mockMvc.perform(get("/ceph/object/buckets").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_03_ListObjects() {
    	try {
			this.mockMvc.perform(get("/ceph/object/objects").param("bucketName", "test-bucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
			
			this.mockMvc.perform(get("/ceph/object/objects").param("bucketName", "xxxxxxx").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_04_GetBucket() {
    	try {
			this.mockMvc.perform(get("/ceph/object/bucket").param("name", "test-bucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
			
			this.mockMvc.perform(get("/ceph/object/bucket").param("name", "xxxxxx").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_05_CreateFolder() {
    	try {
			this.mockMvc.perform(post("/ceph/object/folder").param("bucketName", "test-bucket").param("objectName", "testFolder").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_06_CreateObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/object").param("bucketName", "test-bucket").param("file", "file:///C:/Temp/app/classic.json").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_07_UpdateObject() {
    	try {
			this.mockMvc.perform(put("/ceph/object/object").param("bucketName", "asd").param("key", "ceph_ref_2.txt")
					.param("objectName", "ceph_ref_2.txt").param("newName", "ceph_ref_3.txt").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());

			this.mockMvc.perform(put("/ceph/object/object").param("bucketName", "asd").param("key", "ceph_ref_3.txt")
					.param("objectName", "ceph_ref_3.txt").param("newName", "ceph_ref_2.txt").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_08_GetObjectAcl() {
    	try {
			this.mockMvc.perform(post("/ceph/object/object/detail").param("bucketName", "asd").param("key", "ceph_ref_2.txt").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_09_CopyObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/copy").param("bucketName", "asd")
					.param("key", "ceph_ref_2.txt").param("objectName", "ceph_ref_2.txt")
					.param("targetBucketName", "asdf").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_10_MoveObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/move").param("bucketName", "asdf")
					.param("key", "ceph_ref_2.txt").param("objectName", "ceph_ref_2.txt")
					.param("targetBucketName", "asdf").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_11_DeleteObject() {
    	try {
			this.mockMvc.perform(post("/ceph/object/object/delete").param("bucketName", "asdf").param("key", "ceph_ref_2.txt").param("isFolder", "false").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_12_EmptyBucket() {
    	try {
			this.mockMvc.perform(put("/ceph/object/bucket").param("bucketName", "asdf").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

	@Test
	public void test_13_DeleteBucket() {
    	try {
			this.mockMvc.perform(post("/ceph/object/bucket/delete").param("bucketName", "test-bucket").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json;charset=UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has occurred.");
		}
	}

}
//end of ObjectStorageControllerTest.java