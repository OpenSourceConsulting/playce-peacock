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
 * Sang-cheon Park	2013. 8. 1.		First Draft.
 */
package com.athena.peacock.common.constant;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public final class PeacockConstant {

	/** 서버 접속 정보등의 설정 정보가 저장된 파일 경로 */
	public static final String CONFIG_FILE_KEY 		= "peacock.agent.config.file.name";
	/** Agent ID가 저장된 파일 경로 */
	public static final String AGENT_ID_FILE_KEY	= "peacock.agent.agent.file.name";
	/** 패키지 정보 수집 이력이 저장된 파일 경로 */
	public static final String PACKAGE_FILE_KEY		= "peacock.agent.package.file.name";
	/** Agent에서 접속할 서버의 IP */
	public static final String SERVER_IP 			= "ServerIP";
	/** Agent에서 접속할 서버의 포트 */
	public static final String SERVER_PORT 			= "ServerPort";
	/** wget을 이용한 software 다운로드 url */
	public static final String REPOSITORY_URL		= "RepositoryUrl";
	/** 모니터링 항목 리스트 */
	public static final String MON_FACTOR_LIST 		= "MON_FACTOR_LIST";
}
//end of PeacockConstant.java