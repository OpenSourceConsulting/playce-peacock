package com.athena.peacock.controller.web.ceph.object;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.athena.peacock.controller.web.ceph.CephService;
import com.athena.peacock.controller.web.ceph.base.CephDto;

/**
 * <pre>
 * 
 * </pre>
 * @author jyy
 * @version 1.0
 */
@Service("objectStorageService")
public class ObjectStorageService {	

	private final static String FOLDER_SUFFIX = "/";
	
	@Inject
	@Named("cephService")
	private CephService cephService;
	
	private AmazonS3Client client;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	private AmazonS3Client getClient() throws Exception {
		if (client == null) {
			CephDto cephDto = cephService.selectCeph();
			
			if (cephDto == null) {
				throw new Exception("Ceph cluster does not initiated yet.");
			}

			client = new AmazonS3Client(new BasicAWSCredentials(cephDto.getS3AccessKey(), cephDto.getS3SecretKey()));
			client.setEndpoint(cephDto.getRadosgwApiPrefix());
		}
		
		return client;
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	public List<BucketDto> listBucktes() throws Exception {	
		List<Bucket> buckets = getClient().listBuckets();
		
		List<BucketDto> bucketList = null;
		if (buckets != null) {
			bucketList = new ArrayList<BucketDto>();
			for (Bucket bucket : buckets) {
				BucketDto dto = new BucketDto();
				dto.setOwner(bucket.getOwner().getDisplayName());
				dto.setName(bucket.getName());
				dto.setCreationDate(bucket.getCreationDate());
				bucketList.add(dto);
			}
		}
		
		return bucketList;
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public List<ObjectDto> listObjects(ObjectDto dto) throws Exception {
		List<ObjectDto> objectList = new ArrayList<ObjectDto>();
		List<ObjectDto> fileList = new ArrayList<ObjectDto>();
		List<ObjectDto> folderList = new ArrayList<ObjectDto>();

		ObjectListing objectListing = getClient().listObjects(dto.getBucketName(), dto.getParentPath());
		
		ObjectDto obj = null;
		String objectName = null;
		int cnt = 0;
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			if (objectSummary.getKey().equals(dto.getParentPath())) {
				continue;
			}
			
			obj = new ObjectDto();
			obj.setBucketName(objectSummary.getBucketName());
			obj.setKey(objectSummary.getKey());
			obj.setSize(objectSummary.getSize());
			obj.setOwner(objectSummary.getOwner());
			obj.setStorageClass(objectSummary.getStorageClass());
			obj.setLastModified(objectSummary.getLastModified());
			
			objectName = objectSummary.getKey().substring(dto.getParentPath().length());
			cnt = StringUtils.countMatches(objectName, FOLDER_SUFFIX);
			
			if (cnt == 0) {
				// in case of file
				obj.setObjectName(objectName);
				obj.setFolder(false);
				
				fileList.add(obj);
			} else if (cnt == 1 && objectName.endsWith(FOLDER_SUFFIX)) {
				// in case of folder
				obj.setObjectName(objectName);
				obj.setFolder(true);
				
				folderList.add(obj);
			}
			// cnt > 1 entries will not be included 
        }
	
		objectList.addAll(folderList);
		objectList.addAll(fileList);
		
		/*
		//ObjectListing objects = conn.listObjects(bucket.getName());
		ObjectListing objectListing;
		objectList = new ArrayList<ObjectDto>();
		do {
			    objectListing = getClient().listObjects(request);
		        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
					ObjectDto dto = new ObjectDto();
					dto.setBucketName(objectSummary.getBucketName());
					dto.setKey(objectSummary.getKey());
					dto.setSize(objectSummary.getSize());
					dto.setOwner(objectSummary.getOwner());
					dto.setStorageClass(objectSummary.getStorageClass());
					dto.setLastModified(objectSummary.getLastModified());
					objectList.add(dto);		                
		        }
		        //objects = conn.listNextBatchOfObjects(objects);
		        request.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());
		*/
		
		return objectList;
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @throws Exception
	 */
	public void createBucket(ObjectDto dto) throws Exception {
		getClient().createBucket(dto.getBucketName());
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @throws Exception
	 */
	public void deleteBucket(ObjectDto dto) throws Exception {
		getClient().deleteBucket(dto.getBucketName());
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @throws Exception
	 */
	public void createFolder(ObjectDto dto) throws Exception {
		// Create metadata for your folder & set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// Create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		
		String path = null;
		
		if (dto.getParentPath() != null) {
			path = dto.getParentPath();
		}
		
		if (path.endsWith(FOLDER_SUFFIX)) {
			path = path + dto.getObjectName() + FOLDER_SUFFIX;
		} else {
			path = path + FOLDER_SUFFIX + dto.getObjectName() + FOLDER_SUFFIX;
		}

		PutObjectRequest putObjectRequest =	new PutObjectRequest(dto.getBucketName(), path, emptyContent, metadata);
		getClient().putObject(putObjectRequest);
	}
	
	public void deleteFolder(ObjectDto dto) throws Exception {
		String path = null;
		
		if (dto.getParentPath() != null) {
			path = dto.getParentPath();
		}
		
		if (path.endsWith(FOLDER_SUFFIX)) {
			path = path + dto.getObjectName() + FOLDER_SUFFIX;
		} else {
			path = path + FOLDER_SUFFIX + dto.getObjectName() + FOLDER_SUFFIX;
		}
		
		List<S3ObjectSummary> fileList = getClient().listObjects(dto.getBucketName(), path).getObjectSummaries();

		for (S3ObjectSummary file : fileList) {
			getClient().deleteObject(dto.getBucketName(), file.getKey());
		}
		
		getClient().deleteObject(dto.getBucketName(), path);
	}
	
	public Map<String, Object> getObjectDetail(ObjectDto dto) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		AccessControlList acl = getClient().getObjectAcl(dto.getBucketName(), dto.getKey());
		
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(dto.getBucketName(), dto.getKey());
		URL url = getClient().generatePresignedUrl(request);
		
		ObjectMetadata metadata = getClient().getObjectMetadata(dto.getBucketName(), dto.getKey());
		
		result.put("acl", acl);
		result.put("url", url);
		result.put("metadata", metadata);
		
		// TODO merge object detail info
		return result;
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public AccessControlList getObjectAcl(ObjectDto dto) throws Exception {
		return getClient().getObjectAcl(dto.getBucketName(), dto.getKey());
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public URL getObjectUrl(ObjectDto dto) throws Exception {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(dto.getBucketName(), dto.getKey());
		return getClient().generatePresignedUrl(request);
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public ObjectMetadata getObjectMetadata(ObjectDto dto) throws Exception {
		return getClient().getObjectMetadata(dto.getBucketName(), dto.getKey());
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public void updateObject(ObjectDto dto) throws Exception {
		
		if (dto.getNewName() != null && !dto.getNewName().equals("")) {
			String destinationKeyName = dto.getKey().substring(0, dto.getKey().lastIndexOf(dto.getObjectName())) + dto.getNewName();
			
			CopyObjectRequest copyObjRequest = new CopyObjectRequest(dto.getBucketName(), dto.getKey(), dto.getBucketName(), destinationKeyName);
			getClient().copyObject(copyObjRequest);
			getClient().deleteObject(new DeleteObjectRequest(dto.getBucketName(), dto.getKey()));
		}
		
		System.out.println("DTO : " + dto);
		System.out.println("ACL : " + dto.getAcl());
		
		if (dto.getAcl() != null && !dto.getAcl().equals("")) {
			String acl = dto.getAcl();
			
			CannedAccessControlList newAcl = null;
			if (acl.equals(CannedAccessControlList.Private.toString())) {
				newAcl = CannedAccessControlList.Private;
			} else if (acl.equals(CannedAccessControlList.PublicRead.toString())) {
				newAcl = CannedAccessControlList.PublicRead;
			} else if (acl.equals(CannedAccessControlList.PublicReadWrite.toString())) {
				newAcl = CannedAccessControlList.PublicReadWrite;
			}
			
			if (newAcl != null) {
				System.out.println("Will be changed to : " + newAcl);
				
				getClient().setObjectAcl(dto.getBucketName(), dto.getKey(), newAcl);
			}
		}
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public S3Object getS3Object(ObjectDto dto) throws Exception {
		GetObjectRequest request = new GetObjectRequest(dto.getBucketName(), dto.getKey());
		
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setCacheControl("No-cache");
		responseHeaders.setContentDisposition("attachment; filename=" + dto.getObjectName());
		
		// Add the ResponseHeaderOverides to the request.
		request.setResponseHeaders(responseHeaders);
		
		return getClient().getObject(request);
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @throws Exception
	 */
	public void deleteObject(ObjectDto dto) throws Exception {
		if (dto.isFolder()) {
			deleteFolder(dto);
		} else {
			getClient().deleteObject(dto.getBucketName(), dto.getKey());
		}
	}
	
	public void uploadFile(ObjectDto dto) throws Exception {
		String fileName = dto.getParentPath() + dto.getFile().getOriginalFilename();
		getClient().putObject(new PutObjectRequest(dto.getBucketName(), fileName, saveFile(dto)));
	}
	
	private File saveFile(ObjectDto dto) throws Exception { 
		File file = null;
		
		if (dto.getFile() != null && dto.getFile().getSize() > 0) {
			String defaultPath = System.getProperty("java.io.tmpdir");
			file = new File(defaultPath + dto.getFile().getOriginalFilename());
			
			if (!file.exists()) {
				if (!file.mkdirs()) {
					throw new Exception("Fail to create a directory for attached file [" + file + "]");
				}
			}

			file.deleteOnExit();
			dto.getFile().transferTo(file);
		}
			
		return file;
	}
	
	public static void main(String [] args) throws Exception {
		String accessKey = "YURR234DJCPXHAWA2BKG";
		String secretKey = "6oyIDbSPAG081wiBFEvo0zXTou01d1gljNi5FdnD";
		
		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
		client.setEndpoint("http://192.168.0.219");
		
		/*
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		
		PutObjectRequest putObjectRequest =	new PutObjectRequest("scpark", "folder1/sub-folder1/sub-sub-folder1/", emptyContent, metadata);
		client.putObject(putObjectRequest);
		//*/
		
		//*
		String parentPath = "folder1/";
		ObjectListing objectListing = client.listObjects("scpark", parentPath);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	if (objectSummary.getKey().equals(parentPath)) {
        		continue;
        	}
        	
			ObjectDto dto = new ObjectDto();
			dto.setBucketName(objectSummary.getBucketName());
			dto.setParentPath(parentPath);
			dto.setKey(objectSummary.getKey());
			dto.setSize(objectSummary.getSize());
			dto.setOwner(objectSummary.getOwner());
			dto.setStorageClass(objectSummary.getStorageClass());
			dto.setLastModified(objectSummary.getLastModified());
			
			String key = objectSummary.getKey().substring(parentPath.length());
			
			System.out.println(key + " : " + StringUtils.countMatches(key, "/") + " : " + dto);
        }
		/*/
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName("scpark");
		
		//ObjectListing objects = conn.listObjects(bucket.getName());
		ObjectListing objectListing;
		do {
			    objectListing = client.listObjects(listObjectsRequest);
		        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
					ObjectDto dto = new ObjectDto();
					dto.setBucketName(objectSummary.getBucketName());
					dto.setKey(objectSummary.getKey());
					dto.setSize(objectSummary.getSize());
					dto.setOwner(objectSummary.getOwner());
					dto.setStorageClass(objectSummary.getStorageClass());
					dto.setLastModified(objectSummary.getLastModified());

					System.out.println(dto);
		        }
		        //objects = conn.listNextBatchOfObjects(objects);
		        listObjectsRequest.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());
		//*/

		
	}
	
	/*
	public static void main(String [] args) throws Exception {
		long timeStamp = 1444264448;
		java.util.Date time = new java.util.Date(timeStamp*1000);
		
		System.out.println("Expire time : ");
		System.out.println(time);
		
		ObjectStorageService osc = new ObjectStorageService();
		
		System.out.println("Bucket List : ");
		System.out.println(osc.listOfBucktes());

		System.out.println("Create Folder : ");
		System.out.println(osc.createFolder("my-new-bucket2", null, "testFolder"));
		
		System.out.println("Object List : ");
		System.out.println(osc.listOfObjects("my-new-bucket"));
		System.out.println(osc.listOfObjects("my-new-bucket2"));
		
		System.out.println("Upload File : ");
		System.out.println(osc.uploadFile("my-new-bucket2", "testFolder", "testfile.png"));
		System.out.println(osc.listOfObjects("my-new-bucket2"));
		
		//System.out.println("Delete Folder: ");
		//System.out.println(osc.deleteFolder("my-new-bucket2", "testFolder"));
		
		System.out.println("Object List : ");
		System.out.println(osc.listOfObjects("my-new-bucket2"));
		
		System.out.println("Set Object ACL to Public : ");
		System.out.println(osc.setObjectAclToPublic("my-new-bucket","file.txt"));
		
		System.out.println(osc.getObjectAcl("my-new-bucket","file.txt"));
		System.out.println(osc.getObjectAcl("my-new-bucket2", "testFolder/testfile.png"));
		
		System.out.println("Set Object ACL to Private : ");
		System.out.println(osc.setObjectAclToPrivate("my-new-bucket2","test.txt"));
		
		System.out.println(osc.getObjectAcl("my-new-bucket2","test.txt"));
		
		System.out.println("Object URL : ");
		System.out.println(osc.getUrlOfObject("my-new-bucket","file.txt"));
		System.out.println(osc.getUrlOfObject("my-new-bucket2","test.txt"));
		System.out.println(osc.getUrlOfObject("my-new-bucket2", "testFolder/testfile.png"));
		
		System.out.println(osc.getObjectsMethod("my-new-bucket","file.txt"));
	}
	*/
}