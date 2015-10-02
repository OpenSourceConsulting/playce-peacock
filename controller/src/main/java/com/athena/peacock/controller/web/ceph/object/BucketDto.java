package com.athena.peacock.controller.web.ceph.object;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.s3.model.Owner;

public class BucketDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The name of this S3 bucket */
    private String name = null;

    /** The details on the owner of this bucket */
    private String owner = null;

    /** The date this bucket was created */
    private Date creationDate = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "BucketDto [name=" + name + ", owner=" + owner
				+ ", creationDate=" + creationDate + "]";
	}
    
}
