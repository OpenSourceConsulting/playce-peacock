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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.S3Object;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

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
	
	/**
	 * <pre>
	 * Get bucket list
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/buckets", method={ RequestMethod.GET })
	public @ResponseBody GridJsonResponse listBuckets(GridJsonResponse jsonRes) throws Exception {
		try {
			List<BucketDto> response = objectStorageService.listBucktes();
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
	
	/**
	 * <pre>
	 * Get object list
	 *  - Required parameters : bucketName
	 *  - Optional parameters : parentPath
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/objects", method={ RequestMethod.GET })
	public @ResponseBody GridJsonResponse listObjects(GridJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			List<ObjectDto> response = objectStorageService.listObjects(dto);
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
	
	/**
	 * <pre>
	 * Create a new bucket
	 *  - Required parameters : bucketName
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bucket", method={ RequestMethod.POST })
	public @ResponseBody SimpleJsonResponse createBucket(SimpleJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			objectStorageService.createBucket(dto);
			jsonRes.setSuccess(true);			
			jsonRes.setMsg("Create Bucket 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Create Bucket 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * Delete a bucket
	 *  - Required parameters : bucketName
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bucket", method={ RequestMethod.DELETE })
	public @ResponseBody SimpleJsonResponse deleteBucket(SimpleJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			objectStorageService.deleteBucket(dto);
			jsonRes.setSuccess(true);			
			jsonRes.setMsg("Delete Bucket 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Delete Bucket 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Create a new folder
	 *  - Required parameters : bucketName, objectName
	 *  - Optional parameters : parentPath
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/folder", method={ RequestMethod.POST })
	public @ResponseBody SimpleJsonResponse createFolder(SimpleJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			objectStorageService.createFolder(dto);
			jsonRes.setSuccess(true);			
			jsonRes.setMsg("Create Folder 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Create Folder 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * Get an object's ACL info
	 *  - Required parameters : bucketName, key
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/object", method={ RequestMethod.GET })
	public @ResponseBody DtoJsonResponse getObject(DtoJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			AccessControlList response = objectStorageService.getObjectAcl(dto);
			jsonRes.setSuccess(true);	
			jsonRes.setData(response);
			jsonRes.setMsg("objec 상세 조회 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("objec 상세 조회 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * Delete an object(file & folder)
	 *  - Required parameters : bucketName, key, isFolder
	 *  - Optional parameters : parentPath
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/object", method={ RequestMethod.DELETE })
	public @ResponseBody SimpleJsonResponse deleteObject(SimpleJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			objectStorageService.deleteObject(dto);
			jsonRes.setSuccess(true);
			jsonRes.setMsg("objec 삭제 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("objec 삭제 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * Delete a folder
	 *  - Required parameters : bucketName, objectName
	 *  - Optional parameters : parentPath
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/delete/folder")
//	public @ResponseBody SimpleJsonResponse deleteFolder(SimpleJsonResponse jsonRes, ObjectDto dto) throws Exception {
//		try {
//			objectStorageService.deleteFolder(dto);
//			jsonRes.setSuccess(true);			
//			jsonRes.setMsg("Delete Folder 성공");
//		} catch (Exception e) {
//			jsonRes.setSuccess(false);
//			jsonRes.setMsg("Delete Folder 실패");
//			
//			LOGGER.error("Unhandled Expeption has occurred. ", e);
//		}
//		
//		return jsonRes;
//	}
	
	/**
	 * <pre>
	 * Get Object
	 *  - Required parameters : bucketName, key
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/download", method={ RequestMethod.GET })
	public void download(HttpServletRequest request, HttpServletResponse response, ObjectDto dto) throws Exception {
		S3Object object = objectStorageService.getS3Object(dto);

		//response.reset();

		response.setHeader("Content-Length", Long.toString(object.getObjectMetadata().getContentLength()));
		response.setContentType(object.getObjectMetadata().getContentType());

		String name = object.getKey().substring(object.getKey().lastIndexOf("/") + 1);
		
		System.out.println("============== getObjectByName() ==============");
		System.out.println("key => " + object.getKey());
		System.out.println("name => " + name);
		System.out.println("Content-Type => " + object.getObjectMetadata().getContentType());
		System.out.println("Content-Type => " + object.getObjectMetadata().getContentLength());
		System.out.println("Content-Disposition => " + object.getObjectMetadata().getContentDisposition());
		System.out.println("Content-Encoding => " + object.getObjectMetadata().getContentEncoding());
		
        if(request.getHeader("User-Agent").toLowerCase().contains("firefox") || request.getHeader("User-Agent").toLowerCase().contains("safari")) { 
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(name.getBytes("UTF-8"), "ISO-8859-1") + "\""); 
            response.setHeader("Content-Transfer-Encoding", "binary");
        } else { 
            response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
        }

		try {
			BufferedInputStream fin = new BufferedInputStream(object.getObjectContent());
			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
			
			int read = 0;
			while ((read = fin.read()) != -1) {
				outs.write(read);
			}
			
			IOUtils.closeQuietly(fin);
			IOUtils.closeQuietly(outs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * <pre>
	 * Upload a file
	 *  - Required parameters : bucketName, file
	 *  - Optional parameters : parentPath
	 * </pre>
	 * @param jsonRes
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody SimpleJsonResponse fileUpload(SimpleJsonResponse jsonRes, ObjectDto dto) throws Exception {
		try {
			System.err.println("FileName : " + dto.getFile().getOriginalFilename());
			System.err.println("FileSize : " + dto.getFile().getSize());
			
			objectStorageService.uploadFile(dto);
			jsonRes.setSuccess(true);
			jsonRes.setMsg("Object 업로드 성공");
			
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Object 업로드 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}	
	
}
//end of ObjectStorageTest.java