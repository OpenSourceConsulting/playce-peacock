/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2014. 8. 13.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.redhat.rhevm.api.model.BaseDevice;
import com.redhat.rhevm.api.model.Statistics;
import com.redhat.rhevm.api.model.Status;
import com.redhat.rhevm.api.model.StorageDomains;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "storageDomains",
    "size",
    "provisionedSize",
    "actualSize",
    "type",
    "status",
    "_interface",
    "format",
    "sparse",
    "bootable",
    "shareable",
    "wipeAfterDelete",
    "propagateErrors",
    "statistics",
    "active"
})
public class Disk extends BaseDevice {

    @XmlElement(name = "storage_domains")
    protected StorageDomains storageDomains;
    protected Long size;
    @XmlElement(name = "provisioned_size")
    protected Long provisionedSize;
    @XmlElement(name = "actual_size")
    protected Long actualSize;
    protected String type;
    protected Status status;
    @XmlElement(name = "interface")
    protected String _interface;
    protected String format;
    protected Boolean sparse;
    protected Boolean bootable;
    protected Boolean shareable;
    @XmlElement(name = "wipe_after_delete")
    protected Boolean wipeAfterDelete;
    @XmlElement(name = "propagate_errors")
    protected Boolean propagateErrors;
    protected Statistics statistics;
    protected String active;

    /**
     * Gets the value of the storageDomains property.
     * 
     * @return
     *     possible object is
     *     {@link StorageDomains }
     *     
     */
    public StorageDomains getStorageDomains() {
        return storageDomains;
    }

    /**
     * Sets the value of the storageDomains property.
     * 
     * @param value
     *     allowed object is
     *     {@link StorageDomains }
     *     
     */
    public void setStorageDomains(StorageDomains value) {
        this.storageDomains = value;
    }

    public boolean isSetStorageDomains() {
        return (this.storageDomains!= null);
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSize(Long value) {
        this.size = value;
    }

    public boolean isSetSize() {
        return (this.size!= null);
    }

    /**
	 * @return the provisionedSize
	 */
	public Long getProvisionedSize() {
		return provisionedSize;
	}

	/**
	 * @param provisionedSize the provisionedSize to set
	 */
	public void setProvisionedSize(Long provisionedSize) {
		this.provisionedSize = provisionedSize;
	}

	/**
	 * @return the actualSize
	 */
	public Long getActualSize() {
		return actualSize;
	}

	/**
	 * @param actualSize the actualSize to set
	 */
	public void setActualSize(Long actualSize) {
		this.actualSize = actualSize;
	}

	/**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterface(String value) {
        this._interface = value;
    }

    public boolean isSetInterface() {
        return (this._interface!= null);
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    public boolean isSetFormat() {
        return (this.format!= null);
    }

    /**
     * Gets the value of the sparse property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSparse() {
        return sparse;
    }

    /**
     * Sets the value of the sparse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSparse(Boolean value) {
        this.sparse = value;
    }

    public boolean isSetSparse() {
        return (this.sparse!= null);
    }

    /**
     * Gets the value of the bootable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBootable() {
        return bootable;
    }

    /**
     * Sets the value of the shareable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShareable(Boolean value) {
        this.bootable = value;
    }

    public boolean isSetShareable() {
        return (this.shareable!= null);
    }

    /**
     * Gets the value of the shareable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShareable() {
        return shareable;
    }

    /**
     * Sets the value of the bootable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBootable(Boolean value) {
        this.bootable = value;
    }

    public boolean isSetBootable() {
        return (this.bootable!= null);
    }

    /**
     * Gets the value of the wipeAfterDelete property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWipeAfterDelete() {
        return wipeAfterDelete;
    }

    /**
     * Sets the value of the wipeAfterDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWipeAfterDelete(Boolean value) {
        this.wipeAfterDelete = value;
    }

    public boolean isSetWipeAfterDelete() {
        return (this.wipeAfterDelete!= null);
    }

    /**
     * Gets the value of the propagateErrors property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPropagateErrors() {
        return propagateErrors;
    }

    /**
     * Sets the value of the propagateErrors property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPropagateErrors(Boolean value) {
        this.propagateErrors = value;
    }

    public boolean isSetPropagateErrors() {
        return (this.propagateErrors!= null);
    }

    /**
     * Gets the value of the statistics property.
     * 
     * @return
     *     possible object is
     *     {@link Statistics }
     *     
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Sets the value of the statistics property.
     * 
     * @param value
     *     allowed object is
     *     {@link Statistics }
     *     
     */
    public void setStatistics(Statistics value) {
        this.statistics = value;
    }

    public boolean isSetStatistics() {
        return (this.statistics!= null);
    }

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
}
//end of Disk.java