/**
 * <pre>
 * 
 * </pre>
 * @author jin
 * @version 1.0
 */
package com.athena.peacock.controller.web.ceph.object;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;

@Service("objectStorageService")
public class ObjectStorageController {	
	private String accessKey = "YURR234DJCPXHAWA2BKG";
	private String secretKey = "6oyIDbSPAG081wiBFEvo0zXTou01d1gljNi5FdnD";
	
	private AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	private AmazonS3 conn = new AmazonS3Client(credentials);
	
	protected void connectionRadosGW() throws Exception {
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
	
	public static void main(String [] args) throws Exception {
		ObjectStorageController osc = new ObjectStorageController();
		osc.connectionRadosGW();
		System.out.println(osc.listOfBucktes());
	}
}