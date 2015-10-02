/**
 * <pre>
 * 
 * </pre>
 * @author jin
 * @version 1.0
 */
package com.athena.peacock.controller.web.ceph.object;

import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;


public class ObjectStorageController {	
	private String accessKey = "YURR234DJCPXHAWA2BKG";
	private String secretKey = "6oyIDbSPAG081wiBFEvo0zXTou01d1gljNi5FdnD";
	
	private AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	private AmazonS3 conn = new AmazonS3Client(credentials);
	
	protected void connectionRadosGW() throws Exception {
		conn.setEndpoint("192.168.0.219");
	}

	public Object listOfBucktes() throws Exception {
		List<Bucket> buckets = conn.listBuckets();
		String response = "";
		
		connectionRadosGW();
		
		for (Bucket bucket : buckets) {
		        response += (bucket.getName() + "\t" + StringUtils.fromDate(bucket.getCreationDate()));
		}
		
		return response;
	}
	
	public static void main(String [] args) throws Exception {
		ObjectStorageController osc = new ObjectStorageController();
		osc.connectionRadosGW();
		osc.listOfBucktes();
	}
}