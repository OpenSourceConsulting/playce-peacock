package com.athena.peacock.controller.web.ceph.object;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectStorageService.class);

	private final static String FOLDER_SUFFIX = "/";
	
	@Inject
	@Named("cephService")
	private CephService cephService;
	
	private AmazonS3Client client;
	
	private String accessKey;
	private String secretKey;
	
	private CephDto cephDto;
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	private AmazonS3Client getClient() throws Exception {
		if (client == null) {
			cephDto = cephService.selectCeph();
			
			if (cephDto == null) {
				throw new Exception("Ceph cluster does not initiated yet.");  // NOPMD
			}
			
			accessKey = cephDto.getS3AccessKey();
			secretKey = cephDto.getS3SecretKey();

			client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
			client.setEndpoint(cephDto.getRadosgwApiPrefix());
		} else {
			cephDto = cephService.selectCeph();

			if (!accessKey.equals(cephDto.getS3AccessKey()) || !secretKey.equals(cephDto.getS3SecretKey())) {
				accessKey = cephDto.getS3AccessKey();
				secretKey = cephDto.getS3SecretKey();

				client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
				client.setEndpoint(cephDto.getRadosgwApiPrefix());
			}
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
	 * @return
	 * @throws Exception
	 */
	public BucketDto getBuckteDetail(BucketDto dto) throws Exception {	
		List<Bucket> buckets = getClient().listBuckets();
		
		for (Bucket bucket : buckets) {
			if (dto.getName().equals(bucket.getName())) {
				dto.setOwner(bucket.getOwner().getDisplayName());
				dto.setName(bucket.getName());
				dto.setCreationDate(bucket.getCreationDate());
				
				dto.setAcl(getClient().getBucketAcl(dto.getName()));
				dto.setCrossOrigin(getClient().getBucketCrossOriginConfiguration(dto.getName()));
				dto.setLocation(getClient().getBucketLocation(dto.getName()));
				dto.setLogging(getClient().getBucketLoggingConfiguration(dto.getName()));
				dto.setPolicy(getClient().getBucketPolicy(dto.getName()));
				dto.setVersioning(getClient().getBucketVersioningConfiguration(dto.getName()));
				
				// below operations has error
				//dto.setLifecycle(getClient().getBucketLifecycleConfiguration(dto.getName()));
				//dto.setNotification(getClient().getBucketNotificationConfiguration(dto.getName()));
				//dto.setReplication(getClient().getBucketReplicationConfiguration(dto.getName()));
				//dto.setTagging(getClient().getBucketTaggingConfiguration(dto.getName()));
				//dto.setWebsite(getClient().getBucketWebsiteConfiguration(dto.getName()));
				
				break;
			}
		}
		
		return dto;
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
		emptyBucket(dto);        
		getClient().deleteBucket(dto.getBucketName());
	}
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param dto
	 * @throws Exception
	 */
	public void emptyBucket(ObjectDto dto) throws Exception {
		ObjectListing objectListing = getClient().listObjects(dto.getBucketName());
        
        while (true) {
            for ( Iterator<?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext(); ) {
                S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
                getClient().deleteObject(dto.getBucketName(), objectSummary.getKey());
            }
 
            if (objectListing.isTruncated()) {
                objectListing = getClient().listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
        
        VersionListing list = getClient().listVersions(new ListVersionsRequest().withBucketName(dto.getBucketName()));
        for ( Iterator<?> iterator = list.getVersionSummaries().iterator(); iterator.hasNext(); ) {
            S3VersionSummary s = (S3VersionSummary)iterator.next();
            getClient().deleteVersion(dto.getBucketName(), s.getKey(), s.getVersionId());
        }
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
		
		String path = dto.getParentPath();
		
		if (path.equals("") || path.endsWith(FOLDER_SUFFIX)) {
			path = path + dto.getObjectName();
		} else {
			path = path + FOLDER_SUFFIX + dto.getObjectName();
		}
		
		if (!path.endsWith(FOLDER_SUFFIX)) {
			path += FOLDER_SUFFIX;
		}

		PutObjectRequest putObjectRequest =	new PutObjectRequest(dto.getBucketName(), path, emptyContent, metadata);
		getClient().putObject(putObjectRequest);
	}
	
	public void deleteFolder(ObjectDto dto) throws Exception {
//		String path = null;
//		
//		if (dto.getParentPath() != null) {
//			path = dto.getParentPath();
//		}
//		
//		if (path.endsWith(FOLDER_SUFFIX)) {
//			path = path + dto.getObjectName() + FOLDER_SUFFIX;
//		} else {
//			path = path + FOLDER_SUFFIX + dto.getObjectName() + FOLDER_SUFFIX;
//		}
		
		List<S3ObjectSummary> fileList = getClient().listObjects(dto.getBucketName(), dto.getKey()).getObjectSummaries();

		for (S3ObjectSummary file : fileList) {
			getClient().deleteObject(dto.getBucketName(), file.getKey());
		}
		
		getClient().deleteObject(dto.getBucketName(), dto.getKey());
	}
	
	public ObjectDto getObjectDetail(ObjectDto dto) throws Exception {
		AccessControlList acl = getClient().getObjectAcl(dto.getBucketName(), dto.getKey());
		
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(dto.getBucketName(), dto.getKey());
		URL url = getClient().generatePresignedUrl(request);
		
		ObjectMetadata metadata = getClient().getObjectMetadata(dto.getBucketName(), dto.getKey());
		
		dto.setAcl(acl);
		dto.setUrl(url.toString());
		dto.setMetadata(metadata);
		
		return dto;
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
		
		LOGGER.info("DTO : " + dto);
		LOGGER.info("Permission : " + dto.getPermission());
		
		if (dto.getPermission() != null && !dto.getPermission().equals("")) {
			String acl = dto.getPermission();
			
			CannedAccessControlList newAcl = null;
			if (acl.equals(CannedAccessControlList.Private.toString())) {
				newAcl = CannedAccessControlList.Private;
			} else if (acl.equals(CannedAccessControlList.PublicRead.toString())) {
				newAcl = CannedAccessControlList.PublicRead;
			} else if (acl.equals(CannedAccessControlList.PublicReadWrite.toString())) {
				newAcl = CannedAccessControlList.PublicReadWrite;
			}
			
			if (newAcl != null) {
				LOGGER.info("Will be changed to : " + newAcl);
				
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

	public void copyObject(ObjectDto dto, boolean isMove) throws Exception {
		String targetKey = dto.getParentPath() + dto.getObjectName();
		getClient().copyObject(dto.getBucketName(), dto.getKey(), dto.getTargetBucketName(), targetKey);
		
		if (isMove) {
			getClient().deleteObject(dto.getBucketName(), dto.getKey());
		}
	}
	
	private File saveFile(ObjectDto dto) throws Exception { 
		File file = null;
		
		if (dto.getFile() != null && dto.getFile().getSize() > 0) {
			String defaultPath = System.getProperty("java.io.tmpdir");
			file = new File(defaultPath + dto.getFile().getOriginalFilename());
			
			if (!file.exists()) {
				if (!file.mkdirs()) {
					throw new Exception("Fail to create a directory for attached file [" + file + "]");  // NOPMD
				}
			}

			file.deleteOnExit();
			dto.getFile().transferTo(file);
		}
			
		return file;
	}
	
//	public static void main(String [] args) throws Exception {
//		String accessKey = "YURR234DJCPXHAWA2BKG";
//		String secretKey = "6oyIDbSPAG081wiBFEvo0zXTou01d1gljNi5FdnD";
//		
//		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
//		client.setEndpoint("http://192.168.0.219");
		
		/*
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		
		PutObjectRequest putObjectRequest =	new PutObjectRequest("scpark", "folder1/sub-folder1/sub-sub-folder1/", emptyContent, metadata);
		client.putObject(putObjectRequest);
		//*/
		
		//*
//		String parentPath = "folder1/";
//		ObjectListing objectListing = client.listObjects("scpark", parentPath);
//
//        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//        	if (objectSummary.getKey().equals(parentPath)) {
//        		continue;
//        	}
//        	
//			ObjectDto dto = new ObjectDto();
//			dto.setBucketName(objectSummary.getBucketName());
//			dto.setParentPath(parentPath);
//			dto.setKey(objectSummary.getKey());
//			dto.setSize(objectSummary.getSize());
//			dto.setOwner(objectSummary.getOwner());
//			dto.setStorageClass(objectSummary.getStorageClass());
//			dto.setLastModified(objectSummary.getLastModified());
//			
//			String key = objectSummary.getKey().substring(parentPath.length());
//			
//			LOGGER.info(key + " : " + StringUtils.countMatches(key, "/") + " : " + dto);
//        }
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

					LOGGER.info(dto);
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
		
		LOGGER.info("Expire time : ");
		LOGGER.info(time);
		
		ObjectStorageService osc = new ObjectStorageService();
		
		LOGGER.info("Bucket List : ");
		LOGGER.info(osc.listOfBucktes());

		LOGGER.info("Create Folder : ");
		LOGGER.info(osc.createFolder("my-new-bucket2", null, "testFolder"));
		
		LOGGER.info("Object List : ");
		LOGGER.info(osc.listOfObjects("my-new-bucket"));
		LOGGER.info(osc.listOfObjects("my-new-bucket2"));
		
		LOGGER.info("Upload File : ");
		LOGGER.info(osc.uploadFile("my-new-bucket2", "testFolder", "testfile.png"));
		LOGGER.info(osc.listOfObjects("my-new-bucket2"));
		
		//LOGGER.info("Delete Folder: ");
		//LOGGER.info(osc.deleteFolder("my-new-bucket2", "testFolder"));
		
		LOGGER.info("Object List : ");
		LOGGER.info(osc.listOfObjects("my-new-bucket2"));
		
		LOGGER.info("Set Object ACL to Public : ");
		LOGGER.info(osc.setObjectAclToPublic("my-new-bucket","file.txt"));
		
		LOGGER.info(osc.getObjectAcl("my-new-bucket","file.txt"));
		LOGGER.info(osc.getObjectAcl("my-new-bucket2", "testFolder/testfile.png"));
		
		LOGGER.info("Set Object ACL to Private : ");
		LOGGER.info(osc.setObjectAclToPrivate("my-new-bucket2","test.txt"));
		
		LOGGER.info(osc.getObjectAcl("my-new-bucket2","test.txt"));
		
		LOGGER.info("Object URL : ");
		LOGGER.info(osc.getUrlOfObject("my-new-bucket","file.txt"));
		LOGGER.info(osc.getUrlOfObject("my-new-bucket2","test.txt"));
		LOGGER.info(osc.getUrlOfObject("my-new-bucket2", "testFolder/testfile.png"));
		
		LOGGER.info(osc.getObjectsMethod("my-new-bucket","file.txt"));
	}
	*/
//}