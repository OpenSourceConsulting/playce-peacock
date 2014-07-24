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
 * Sang-cheon Park	2013. 10. 24.		First Draft.
 */
package com.athena.peacock.controller.web.ospackage;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.MessageType;
import com.athena.peacock.common.netty.message.OSPackageInfoMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller("packageController")
@RequestMapping("/package")
public class PackageController {
	
	@Inject
	@Named("packageService")
	private PackageService packageService;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, PackageDto ospackage) throws Exception {
		Assert.isTrue(StringUtils.isNotEmpty(ospackage.getMachineId()), "machineId must not be null.");
		
		jsonRes.setTotal(packageService.getPackageListCnt(ospackage));
		jsonRes.setList(packageService.getPackageList(ospackage));
		
		return jsonRes;
	}

	@RequestMapping("/reload")
	public @ResponseBody SimpleJsonResponse reload(SimpleJsonResponse jsonRes, PackageDto ospackage) throws Exception {
		Assert.isTrue(StringUtils.isNotEmpty(ospackage.getMachineId()), "machineId must not be null.");

		OSPackageInfoMessage msg = new OSPackageInfoMessage(MessageType.PACKAGE_INFO) ;
		msg.setAgentId(ospackage.getMachineId());
		msg.setBlocking(false);
		
		PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(msg);
		peacockTransmitter.sendMessage(datagram);
		
		jsonRes.setSuccess(true);
		jsonRes.setMsg("패키지 정보 재 수집 요청이 전달되었습니다.");
		
		return jsonRes;
	}
}
//end of PackageController.java