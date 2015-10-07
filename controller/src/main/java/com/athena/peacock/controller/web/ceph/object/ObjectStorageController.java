/* 
 * Athena Peacock - Auto Provisioning
 * 
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
 * jin			2015. 10. 1.		First Draft.
 */
package com.athena.peacock.controller.web.ceph.object;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.common.model.GridJsonResponse;
/**
 * <pre>
 * 
 * </pre>
 * @author jin
 * @version 1.0
 */
@Controller
@RequestMapping("/ceph/object")
public class ObjectStorageController{
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectStorageController.class);

	@Inject 
	@Named("objectStorageService")
	private ObjectStorageService objectStorageService;
	
	@RequestMapping("/buckets")
	public @ResponseBody GridJsonResponse listBuckets(GridJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			
			System.err.println("\n\n\nObjectDto : " + dto);
			
			List<BucketDto> response = objectStorageService.listOfBucktes();
			jsonRes.setSuccess(true);
			jsonRes.setList(response);
			jsonRes.setMsg("Object Storage - Bucket List 조회 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Object Storage - Bucket List 조회 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/create/bucket")
	public @ResponseBody GridJsonResponse createBucketByName(GridJsonResponse jsonRes, @QueryParam("bucket") String bucket) throws Exception {
		try {
			boolean response = objectStorageService.createBucketMethod(bucket);
			jsonRes.setSuccess(response);			
			jsonRes.setMsg("Create Bucket 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Create Bucket 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	@RequestMapping("/delete/bucket")
	public @ResponseBody GridJsonResponse deleteBucketByName(GridJsonResponse jsonRes, @QueryParam("bucket") String bucket) throws Exception {
		try {
			boolean response = objectStorageService.deleteBucketMethod(bucket);
			jsonRes.setSuccess(response);			
			jsonRes.setMsg("Delete Bucket 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Delete Bucket 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	@RequestMapping("/objects")
	public @ResponseBody GridJsonResponse listObjects(GridJsonResponse jsonRes, @QueryParam("bucket") String bucket) throws Exception {
		try {
			List<ObjectDto> response = objectStorageService.listOfObjects(bucket);
			jsonRes.setSuccess(true);
			jsonRes.setList(response);
			jsonRes.setMsg("Object Storage - Bucket List 조회 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Object Storage - Bucket List 조회 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	@RequestMapping("/get/object")
	public @ResponseBody GridJsonResponse getObjectByName(GridJsonResponse jsonRes, @QueryParam("bucket") String bucket, @QueryParam("object") String object) throws Exception {
		try {
			boolean response = objectStorageService.getObjectsMethod(bucket, object);
			jsonRes.setSuccess(response);			
			jsonRes.setMsg("Object 다운 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Object 다운 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}	
	
}
//end of ObjectStorageTest.java