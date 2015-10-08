/**
 * <pre>
 * 
 * </pre>
 * @author jin
 * @version 1.0
 */
package com.athena.peacock.controller.web.ceph.object;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service("objectStorageService")
public class ObjectStorageService {	
	private final static String accessKey = "YURR234DJCPXHAWA2BKG";
	private final static String secretKey = "6oyIDbSPAG081wiBFEvo0zXTou01d1gljNi5FdnD";
	
	private final static String FOLDER_SUFFIX = "/";
		
	private final static AmazonS3 conn;
	
	static {
		conn = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	public ObjectStorageService() throws Exception {
		conn.setEndpoint("http://192.168.0.219");
	}
	
	public List<BucketDto> listOfBucktes() throws Exception {		
		System.out.println("Connection : " + conn);
		List<BucketDto> bucketList = null;
	
		List<Bucket> buckets = conn.listBuckets();
		
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
	
	public boolean createBucketMethod(String bucketName) throws Exception {
		try {
			Bucket bucket = conn.createBucket(bucketName);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean deleteBucketMethod(String bucketName) throws Exception {
		try {
			conn.deleteBucket(bucketName);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean createFolder(String bucketName, String folderName) throws Exception {
		try {
			// Create metadata for your folder & set content-length to 0
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);

			// Create empty content
			InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

			PutObjectRequest putObjectRequest =
				new PutObjectRequest(bucketName, folderName + FOLDER_SUFFIX, emptyContent, metadata);

			conn.putObject(putObjectRequest);
			
			return true;
			
		} catch (Exception e) {
			
			return false;			
		}		
	}
	
	public boolean deleteFolder(String bucketName, String folderName) throws Exception {
		try {
			List<S3ObjectSummary> fileList = conn.listObjects(bucketName, folderName).getObjectSummaries();

			for (S3ObjectSummary file : fileList) {
				conn.deleteObject(bucketName, file.getKey());
			}
			
			conn.deleteObject(bucketName, folderName);
			
			return true;
			
		} catch (Exception e) {		
			return false;
		}
	}
	
	public List<ObjectDto> listOfObjects(String bucketName) throws Exception {
		List<ObjectDto> objectList = null;

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);
		
		//ObjectListing objects = conn.listObjects(bucket.getName());
		ObjectListing objectListing;
		objectList = new ArrayList<ObjectDto>();
		do {
			    objectListing = conn.listObjects(listObjectsRequest);
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
		        listObjectsRequest.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());
		
		return objectList;
	}
	
	public boolean setObjectAclToPublic(String bucketName, String objectName){
		try {
			conn.setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setObjectAclToPrivate(String bucketName, String objectName){
		try {
			conn.setObjectAcl(bucketName, objectName, CannedAccessControlList.Private);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}

	public String getObjectAcl(String bucketName, String objectName) {
		try {
			AccessControlList objectAcl = conn.getObjectAcl(bucketName, objectName);
						
			return objectAcl.toString();
			
		} catch (Exception e) {
			return "Failed";
		}
		
	}	
	
	public boolean DeleteObject(String bucketName, String objectName){
		try {
			conn.deleteObject(bucketName, objectName);
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getUrlOfObject(String bucketName, String objectName){
		try {
			GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
						
			return conn.generatePresignedUrl(request).toString();
			
		} catch (Exception e) {
			System.err.println("Error");
			return "failed";
		}
	}		
	
	public String getObjectsMethod(String bucketName, String objectName){
		try {
			GetObjectRequest request = new GetObjectRequest(bucketName, objectName);
			
			ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
			responseHeaders.setCacheControl("No-cache");
			responseHeaders.setContentDisposition("attachment; filename=" + objectName);
			
			// Add the ResponseHeaderOverides to the request.
			request.setResponseHeaders(responseHeaders);
			
			S3Object objectPortion = conn.getObject(request);
			System.err.println("\n\n\nerror 2");			
			

			
			System.err.println("\n\n\nerror 3");
			

			
			System.err.println("\n\n\nerror 4");
			
			InputStream objectData = objectPortion.getObjectContent();			
			
			String response = objectData.toString();
			System.err.println(response);
			
			objectPortion.close();
			
			return response;
			
		} catch (Exception e) {
			System.err.println(e);
			return "falied";
		}
	}
	
	public boolean uploadFile(String bucketName, String folderName, String objectName){
		try {
			String filePath = "C:\\Users\\jin\\Downloads\\srv-DC-alger-cpu.png";
			String fileName = folderName + FOLDER_SUFFIX + objectName;

			File uploadFile = new File(filePath);
			conn.putObject(new PutObjectRequest(bucketName, fileName, uploadFile));
		
			return true;
			
		} catch (Exception e) {		
			return false;
		}
	}
	
	public static void main(String [] args) throws Exception {
		long timeStamp = 1444264448;
		java.util.Date time = new java.util.Date(timeStamp*1000);
		
		System.out.println("Expire time : ");
		System.out.println(time);
		
		ObjectStorageService osc = new ObjectStorageService();
		
		System.out.println("Bucket List : ");
		System.out.println(osc.listOfBucktes());

		System.out.println("Create Folder : ");
		System.out.println(osc.createFolder("my-new-bucket2", "testFolder"));
		
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
}