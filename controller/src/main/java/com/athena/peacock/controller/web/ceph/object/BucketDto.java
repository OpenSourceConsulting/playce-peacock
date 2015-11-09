package com.athena.peacock.controller.web.ceph.object;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.BucketPolicy;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;

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
    
    private AccessControlList acl;
    private transient BucketCrossOriginConfiguration crossOrigin;
    private String location;
    private transient BucketLoggingConfiguration logging;
    private transient BucketPolicy policy;
    private transient BucketVersioningConfiguration versioning;

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

	public AccessControlList getAcl() {
		return acl;
	}

	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}

	public BucketCrossOriginConfiguration getCrossOrigin() {
		return crossOrigin;
	}

	public void setCrossOrigin(BucketCrossOriginConfiguration crossOrigin) {
		this.crossOrigin = crossOrigin;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BucketLoggingConfiguration getLogging() {
		return logging;
	}

	public void setLogging(BucketLoggingConfiguration logging) {
		this.logging = logging;
	}

	public BucketPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(BucketPolicy policy) {
		this.policy = policy;
	}

	public BucketVersioningConfiguration getVersioning() {
		return versioning;
	}

	public void setVersioning(BucketVersioningConfiguration versioning) {
		this.versioning = versioning;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
