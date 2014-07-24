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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.athena.peacock.controller.web.config.ConfigDao;
import com.athena.peacock.controller.web.config.ConfigDto;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.software.SoftwareDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("softwareService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class SoftwareService {
    
	@Inject
	@Named("softwareDao")
	private SoftwareDao softwareDao;
	
	@Inject
	@Named("configDao")
	private ConfigDao configDao;
	
	public void insertSoftware(SoftwareDto software) throws Exception {		
		if (softwareDao.getSoftware(software) != null) {
			softwareDao.updateSoftware(software);
		} else {
			softwareDao.insertSoftware(software);
		}
	}
	
	public void insertSoftware(SoftwareDto software, List<ConfigDto> configList) throws Exception {		
		if (softwareDao.getSoftware(software) != null) {
			softwareDao.updateSoftware(software);
		} else {
			softwareDao.insertSoftware(software);
		}
		
		// 기존에 존재하는 config 파일 정보를 삭제 처리한다.
		if (configList.size() > 0) {
			configDao.deleteConfig(configList.get(0));
		}
		
		for (ConfigDto config : configList) {
			configDao.insertConfig(config);
		}
	}
	
	public void updateSoftware(SoftwareDto software) throws Exception {
		softwareDao.updateSoftware(software);
	}
	
	public void deleteSoftware(SoftwareDto software) throws Exception {
		softwareDao.deleteSoftware(software);
	}
	
	public SoftwareDto getSoftware(SoftwareDto software) throws Exception {
		return softwareDao.getSoftware(software);
	}
	
	public List<SoftwareDto> getSoftwareList(SoftwareDto software) throws Exception {
		return softwareDao.getSoftwareList(software);
	}
	
	public int getSoftwareInstallListCnt(MachineDto machine) throws Exception {
		return softwareDao.getSoftwareInstallListCnt(machine);
	}
	
	public List<SoftwareDto> getSoftwareInstallList(MachineDto machine) throws Exception {
		return softwareDao.getSoftwareInstallList(machine);
	}
	
	public List<SoftwareDto> getSoftwareInstallListAll(String machineId) throws Exception {
		return softwareDao.getSoftwareInstallListAll(machineId);
	}

}
//end of SoftwareService.java