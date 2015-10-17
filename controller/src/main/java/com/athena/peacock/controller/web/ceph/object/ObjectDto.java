package com.athena.peacock.controller.web.ceph.object;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.amazonaws.services.s3.model.Owner;

public class ObjectDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String bucketName;
	private String parentPath;
	private String objectName;
	private boolean isFolder;
	private String key;
	private long size;
	private Date lastModified;
	private String storageClass;
	private Owner owner;
	
	private CommonsMultipartFile file;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getParentPath() {
		if (parentPath == null) {
			parentPath = "";
		}
		
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
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

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "ObjectDto [bucketName=" + bucketName + ", parentPath=" + parentPath + ", objectName=" + objectName + ", isFolder=" + isFolder
				 + ", key=" + key + ", size=" + size + ", owner=" + owner + ", storageClass=" + storageClass + ", lastModified=" + lastModified + "]";
	}
}
