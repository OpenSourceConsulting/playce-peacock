/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Ji-Woong Choi	2013. 11. 01.		First Draft.
 */
package com.athena.peacock.controller.web.rhevm;


/**
 * <pre>
 * RHEV VM API 목록 정의
 * </pre>
 * @author Ji-Woong Choi
 * @version 1.0
 */
public interface RHEVApi {
	public static final String API = "/api";
	public static final String VMS = "/api/vms";
	public static final String DATA_CENTERS = "/api/datacenters";
	public static final String NETWORKS = "/api/networks";
	public static final String DOMAINS = "/api/domains";
	public static final String ROLES = "/api/roles";
	public static final String TEMPLATES = "/api/templates";
	public static final String USERS = "/api/users";
	public static final String GROUPS = "/api/groups";
	public static final String STORAGE_DOMAINS = "/api/storagedomains";
	public static final String DISKS = "/api/disks";
	public static final String CLUSTERS = "/api/clusters";
	public static final String HOSTS = "/api/hosts";
}
