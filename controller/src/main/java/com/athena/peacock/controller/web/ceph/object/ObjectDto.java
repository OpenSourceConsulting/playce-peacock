package com.athena.peacock.controller.web.ceph.object;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.s3.model.Owner;

public class ObjectDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String bucketName;
	protected String key;
	protected long size;
	protected Date lastModified;
	protected String storageClass;
	protected Owner owner;


	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getStorageClass() {
		return storageClass;
	}

	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}

	@Override
	public String toString() {
		return "ObjectDto [bucketName=" + bucketName + ", key=" + key + ", size=" + size + ", owner=" + owner
				+ ", storageClass=" + storageClass + ", lastModified=" + lastModified + "]";
	}
    
}
