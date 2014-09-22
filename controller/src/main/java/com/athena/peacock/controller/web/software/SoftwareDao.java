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

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.software.SoftwareDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Repository("softwareDao")
public class SoftwareDao extends AbstractBaseDao {
	
	public void insertSoftware(SoftwareDto software) {
		sqlSession.insert("SoftwareMapper.insertSoftware", software);
	}
	
	public void updateSoftware(SoftwareDto software) {
		sqlSession.update("SoftwareMapper.updateSoftware", software);
	}
	
	public void deleteSoftware(SoftwareDto software) {
		sqlSession.delete("SoftwareMapper.deleteSoftware", software);
	}
	
	public SoftwareDto getSoftware(SoftwareDto software) {
		return sqlSession.selectOne("SoftwareMapper.getSoftware", software);
	}

	public int getSoftwareListCnt(SoftwareDto software) {
		return sqlSession.selectOne("SoftwareMapper.getSoftwareListCnt", software);
	}

	public List<SoftwareDto> getSoftwareList(SoftwareDto software) {
		return sqlSession.selectList("SoftwareMapper.getSoftwareList", software);
	}

	public int getSoftwareInstallListCnt(MachineDto machine) {
		return sqlSession.selectOne("SoftwareMapper.getSoftwareInstallListCnt", machine);
	}

	public List<SoftwareDto> getSoftwareInstallList(MachineDto machine) {
		return sqlSession.selectList("SoftwareMapper.getSoftwareInstallList", machine);
	}

	public List<SoftwareDto> getSoftwareInstallListAll(String machineId) {
		return sqlSession.selectList("SoftwareMapper.getSoftwareInstallListAll", machineId);
	}
}
//end of SoftwareDao.java