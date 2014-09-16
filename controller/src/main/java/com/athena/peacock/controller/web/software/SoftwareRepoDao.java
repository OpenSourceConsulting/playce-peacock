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

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Repository("softwareRepoDao")
public class SoftwareRepoDao extends AbstractBaseDao {
	
	public void insertSoftwareRepo(SoftwareRepoDto softwareRepo) {
		sqlSession.insert("SoftwareRepoMapper.insertSoftwareRepo", softwareRepo);
	}
	
	public void updateSoftwareRepo(SoftwareRepoDto softwareRepo) {
		sqlSession.update("SoftwareRepoMapper.updateSoftwareRepo", softwareRepo);
	}
	
	public void deleteSoftwareRepo(int softwareId) {
		sqlSession.delete("SoftwareRepoMapper.deleteSoftwareRepo", softwareId);
	}
	
	public SoftwareRepoDto getSoftwareRepo(int softwareId) {
		return sqlSession.selectOne("SoftwareRepoMapper.getSoftwareRepo", softwareId);
	}

	public List<SoftwareRepoDto> getSoftwareRepoList(SoftwareRepoDto softwareRepo) {
		return sqlSession.selectList("SoftwareRepoMapper.getSoftwareRepoList", softwareRepo);
	}

	public List<SoftwareRepoDto> getSoftwareNames() {
		return sqlSession.selectList("SoftwareRepoMapper.getSoftwareNames");
	}

	public List<SoftwareRepoDto> getSoftwareVersions(String softwareName) {
		return sqlSession.selectList("SoftwareRepoMapper.getSoftwareVersions", softwareName);
	}
}
//end of SoftwareRepoDao.java