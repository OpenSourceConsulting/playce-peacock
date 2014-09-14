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
 * Sang-cheon Park	2013. 10. 16.		First Draft.
 */
package com.athena.peacock.controller.web.software;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.common.provisioning.ProvisioningDetail;
import com.athena.peacock.controller.common.provisioning.ProvisioningHandler;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.user.UserController;
import com.athena.peacock.controller.web.user.UserDto;

/**
 * <pre>
 * 소프트웨어 설치 및 제거를 위한 컨트롤러
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Controller
@RequestMapping("/software")
public class SoftwareController {

    protected final Logger logger = LoggerFactory.getLogger(SoftwareController.class);

	@Inject
	@Named("softwareRepoService")
	private SoftwareRepoService softwareRepoService;

	@Inject
	@Named("softwareService")
	private SoftwareService softwareService;
	
	@Inject
	@Named("provisioningHandler")
	private ProvisioningHandler provisioningHandler;
	
	/**
	 * <pre>
	 * 지정된 Agent(machineID)의 소프트웨어 설치 목록 조회
	 * </pre>
	 * @param jsonRes
	 * @param machineId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public @ResponseBody GridJsonResponse list(GridJsonResponse jsonRes, MachineDto machine) throws Exception {
		Assert.isTrue(!StringUtils.isEmpty(machine.getMachineId()), "machineId must not be null.");
		
		jsonRes.setTotal(softwareService.getSoftwareInstallListCnt(machine));
		jsonRes.setList(softwareService.getSoftwareInstallList(machine));
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 소프트웨어 이름 조회
	 * </pre>
	 * @param jsonRes
	 * @param machineId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getNames")
	public @ResponseBody List<String> getNames() throws Exception {
		return softwareRepoService.getSoftwareNames();
	}
	
	/**
	 * <pre>
	 * 소프트웨어 버전 조회
	 * </pre>
	 * @param jsonRes
	 * @param machineId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getVersions")
	public @ResponseBody GridJsonResponse getVersions(GridJsonResponse jsonRes, String softwareName) throws Exception {
		
		List<SoftwareRepoDto> softwareRepoList = softwareRepoService.getSoftwareVersions(softwareName);
		
		jsonRes.setTotal(softwareRepoList.size());
		jsonRes.setList(softwareRepoList);
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * 선택된 Agent(machineID)의 설치 로그 조회
	 * </pre>
	 * @param jsonRes
	 * @param machineId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getInstallLog")
	public @ResponseBody SimpleJsonResponse getInstallLog(SimpleJsonResponse jsonRes,SoftwareDto software) throws Exception {
		Assert.isTrue(software.getSoftwareId() != null, "softwareId must not be null.");
		Assert.isTrue(!StringUtils.isEmpty(software.getMachineId()), "machineId must not be null.");
		
		jsonRes.setData(softwareService.getSoftware(software));
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * Agent에 소프트웨어를 설치
	 * </pre>
	 * @param jsonRes
	 * @param provisioningDetail
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/install")
	public @ResponseBody SimpleJsonResponse install(HttpServletRequest request, SimpleJsonResponse jsonRes, ProvisioningDetail provisioningDetail) {
		Assert.isTrue(provisioningDetail.getSoftwareId() != null, "softwareId must not be null.");
		Assert.isTrue(!StringUtils.isEmpty(provisioningDetail.getMachineId()), "machineId must not be null.");

		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				provisioningDetail.setUserId(userDto.getUserId());
			}
			
			// 기 설치 여부 검사
			boolean installed = false;
			boolean installedDiffVersion = false;
			
			SoftwareRepoDto softwareRepo = softwareRepoService.getSoftwareRepo(provisioningDetail.getSoftwareId());
			provisioningDetail.setSoftwareName(softwareRepo.getSoftwareName());
			provisioningDetail.setVersion(softwareRepo.getSoftwareVersion());
			provisioningDetail.setFileLocation(softwareRepo.getFileLocation());
			provisioningDetail.setFileName(softwareRepo.getFileName());
			
			List<SoftwareDto> softwareList = softwareService.getSoftwareInstallListAll(provisioningDetail.getMachineId());
			
			for (SoftwareDto software : softwareList) {
				if (software.getSoftwareName().equals(provisioningDetail.getSoftwareName()) && software.getInstallYn().equals("Y")) {
					if (software.getSoftwareVersion().equals(provisioningDetail.getVersion())) {
						installed = true;
					} else {
						installedDiffVersion = true;
					}
				}
			}
			
			if (installed) {
				jsonRes.setSuccess(false);
				jsonRes.setMsg("이미 설치된 소프트웨어입니다.");
				return jsonRes;
			} 
			
			if (installedDiffVersion) {
				jsonRes.setSuccess(false);
				jsonRes.setMsg("다른 버전의 소프트웨어가 설치되어 있습니다. 해당 소프트웨어 삭제 후 설치하여 주십시오.");
				return jsonRes;
			}
		
			String urlPrefix = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
			provisioningDetail.setUrlPrefix(urlPrefix);
			
			provisioningHandler.install(provisioningDetail);
			jsonRes.setMsg("소프트웨어 설치 요청이 전달되었습니다. 아래 소프트웨어 탭에서 상태 결과를 조회할 수 있습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("설치 중 예외가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * Agent에 소프트웨어를 삭제
	 * </pre>
	 * @param jsonRes
	 * @param provisioningDetail
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/remove")
	public @ResponseBody SimpleJsonResponse remove(HttpServletRequest request, SimpleJsonResponse jsonRes, ProvisioningDetail provisioningDetail) throws Exception {
		Assert.isTrue(provisioningDetail.getSoftwareId() != null, "softwareId must not be null.");
		Assert.isTrue(!StringUtils.isEmpty(provisioningDetail.getMachineId()), "machineId must not be null.");
		
		try {
			UserDto userDto = (UserDto)request.getSession().getAttribute(UserController.SESSION_USER_KEY);
			if (userDto != null) {
				provisioningDetail.setUserId(userDto.getUserId());
			}
			
			// 기 삭제 여부 검사
			boolean removed = true;
			
			SoftwareRepoDto softwareRepo = softwareRepoService.getSoftwareRepo(provisioningDetail.getSoftwareId());
			provisioningDetail.setSoftwareName(softwareRepo.getSoftwareName());
			provisioningDetail.setVersion(softwareRepo.getSoftwareVersion());
			
			List<SoftwareDto> softwareList = softwareService.getSoftwareInstallListAll(provisioningDetail.getMachineId());
			
			for (SoftwareDto software : softwareList) {
				if (software.getSoftwareName().equals(provisioningDetail.getSoftwareName()) && software.getInstallYn().equals("Y")) {
					if (software.getSoftwareVersion().equals(provisioningDetail.getVersion())) {
						removed = false;
					}
				}
			}
			
			if (removed) {
				jsonRes.setSuccess(false);
				jsonRes.setMsg("이미 삭제되었거나 설치되지 않은 소프트웨어입니다.");
				return jsonRes;
			} 
			
			provisioningHandler.remove(provisioningDetail);
			jsonRes.setMsg("소프트웨어 삭제 요청이 전달되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("삭제 중 예외가 발생하였습니다.");
			
			logger.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * HTTPD 설치를 위해 선택된 사용자 계정이 세팅된 uriworkermap.properties 및 workers.properties 내용을 반환한다.
	 * </pre>
	 * @param jsonRes
	 * @param account
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getConnectorProp")
	public @ResponseBody SimpleJsonResponse getConnectorProp(HttpServletRequest request, SimpleJsonResponse jsonRes, String account) throws Exception {
		Assert.notNull(account, "account must not be null.");
		
		String urlPrefix = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
		
		Map<String, String> properties = new HashMap<String, String>();
				
		String uriworkermap = IOUtils.toString(new URL(urlPrefix + "/httpd/conf/extra/uriworkermap.properties"), "UTF-8").replaceAll("\\$\\{USER\\}", account);
		String workers = IOUtils.toString(new URL(urlPrefix + "/httpd/conf/extra/workers.properties"), "UTF-8").replaceAll("\\$\\{USER\\}", account);
		
		properties.put("uriworkermap", uriworkermap);
		properties.put("workers", workers);
		
		jsonRes.setData(properties);
		
		return jsonRes;
	}
}
//end of SoftwareController.java