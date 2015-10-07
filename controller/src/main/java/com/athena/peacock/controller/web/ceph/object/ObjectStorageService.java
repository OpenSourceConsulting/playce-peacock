/**
 * <pre>
 * 
 * </pre>
 * @author jin
 * @version 1.0
 */
package com.athena.peacock.controller.web.ceph.object;

import java.io.InputStream;
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
	
	public S3Object getObjectsMethod(String bucketName, String objectName){
		try {
			GetObjectRequest request = new GetObjectRequest(bucketName, objectName);
			//bucket = conn.getObject(objectName);

			ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
			responseHeaders.setCacheControl("No-cache");
			responseHeaders.setContentDisposition("attachment; filename=" + objectName);

			// Add the ResponseHeaderOverides to the request.
			request.setResponseHeaders(responseHeaders);
			
			S3Object objectPortion = conn.getObject(request);
			
			return objectPortion;
			//InputStream objectData = objectPortion.getObjectContent();
			
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean putObjectsMethod(){
		//String fileName = folderName + SUFFIX + "testvideo.mp4";
		//conn.putObject(new PutObjectRequest(bucketName, fileName, 
		//		new File("C:\\Users\\user\\Desktop\\testvideo.mp4")));
		return false;
	}
	
	public static void main(String [] args) throws Exception {
		ObjectStorageService osc = new ObjectStorageService();
		
		System.out.println(osc.listOfBucktes());
		System.out.println(osc.listOfObjects("my-new-bucket"));
	}
}