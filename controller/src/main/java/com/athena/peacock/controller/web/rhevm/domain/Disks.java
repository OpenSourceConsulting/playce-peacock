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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.redhat.rhevm.api.model.BaseDevices;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "disks",
    "clone"
})
@XmlRootElement(name = "disks")
public class Disks extends BaseDevices {

    @XmlElement(name = "disk")
    protected List<Disk> disks;
    protected Boolean clone;

    /**
     * Gets the value of the disks property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disks property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisks().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Disk }
     * 
     * 
     */
    public List<Disk> getDisks() {
        if (disks == null) {
            disks = new ArrayList<Disk>();
        }
        return this.disks;
    }

    public boolean isSetDisks() {
        return ((this.disks!= null)&&(!this.disks.isEmpty()));
    }

    public void unsetDisks() {
        this.disks = null;
    }

    /**
     * Gets the value of the clone property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClone() {
        return clone;
    }

    /**
     * Sets the value of the clone property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClone(Boolean value) {
        this.clone = value;
    }

    public boolean isSetClone() {
        return (this.clone!= null);
    }

}
//end of Disks.java