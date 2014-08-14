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
 * Sang-cheon Park	2014. 8. 14.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.redhat.rhevm.api.model.BaseDevices;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "nics"
})
public class Nics extends BaseDevices {

    @XmlElement(name = "nic")
    protected List<NIC> nics;

    /**
     * Gets the value of the nics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NIC }
     * 
     * 
     */
    public List<NIC> getNics() {
        if (nics == null) {
            nics = new ArrayList<NIC>();
        }
        return this.nics;
    }

    public boolean isSetNics() {
        return ((this.nics!= null)&&(!this.nics.isEmpty()));
    }

    public void unsetNics() {
        this.nics = null;
    }

}
//end of Nics.java