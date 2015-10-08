/**
 * <pre>
 * 
 * </pre>
 * @author jin
 * @version 1.0
 */
package com.athena.peacock.controller.web.ceph.object;

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
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service("objectStorageService")
public class ObjectStorageService {	
	private String accessKey = "YURR234DJCPXHAWA2BKG";
	private String secretKey = "6oyIDbSPAG081wiBFEvo0zXTou01d1gljNi5FdnD";
	
	private AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	private AmazonS3 conn = new AmazonS3Client(credentials);
	
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
	
	public boolean putObjectsMethod(){
		//String fileName = folderName + SUFFIX + "testvideo.mp4";
		//conn.putObject(new PutObjectRequest(bucketName, fileName, 
		//		new File("C:\\Users\\user\\Desktop\\testvideo.mp4")));
		return false;
	}
	
	public static void main(String [] args) throws Exception {
		long timeStamp = 1444264448;
		java.util.Date time = new java.util.Date(timeStamp*1000);
		
		System.out.println("Expire time : ");
		System.out.println(time);
		
		ObjectStorageService osc = new ObjectStorageService();
		
		System.out.println("Bucket List : ");
		System.out.println(osc.listOfBucktes());
		
		System.out.println("Object List : ");
		System.out.println(osc.listOfObjects("my-new-bucket"));
		System.out.println(osc.listOfObjects("my-new-bucket2"));
		
		System.out.println("Set Object ACL to Public : ");
		System.out.println(osc.setObjectAclToPublic("my-new-bucket","file.txt"));
		
		System.out.println("Set Object ACL to Private : ");
		System.out.println(osc.setObjectAclToPrivate("my-new-bucket2","test.txt"));
		
		System.out.println("Object URL : ");
		System.out.println(osc.getUrlOfObject("my-new-bucket","file.txt"));
		System.out.println(osc.getUrlOfObject("my-new-bucket2","test.txt"));
		
		System.out.println(osc.getObjectsMethod("my-new-bucket","file.txt"));
	}
}