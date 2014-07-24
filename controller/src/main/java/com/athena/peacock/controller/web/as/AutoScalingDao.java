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
 * Sang-cheon Park	2013. 11. 6.		First Draft.
 */
package com.athena.peacock.controller.web.as;

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
@Repository("autoScalingDao")
public class AutoScalingDao extends AbstractBaseDao {

	public List<AutoScalingDto> inspectMachineStatus() {
		return sqlSession.selectList("AutoScalingMapper.inspectMachineStatus");
	}
	
	public AutoScalingDto getAutoScaling(Integer autoScalingId) {
		return sqlSession.selectOne("AutoScalingMapper.getAutoScaling", autoScalingId);
	}
	
	public void insertAutoScaling(AutoScalingDto autoScaling) {
		sqlSession.insert("AutoScalingMapper.insertAutoScaling", autoScaling);
	}
	
	public void insertASPolicy(AutoScalingDto autoScaling) {
		sqlSession.insert("AutoScalingMapper.insertASPolicy", autoScaling);
	}
	
	public void updateAutoScaling(AutoScalingDto autoScaling) {
		sqlSession.update("AutoScalingMapper.updateAutoScaling", autoScaling);
	}
	
	public void updateASPolicy(AutoScalingDto autoScaling) {
		sqlSession.update("AutoScalingMapper.updateASPolicy", autoScaling);
	}
	
	public void deleteAutoScaling(Integer autoScalingId) {
		sqlSession.update("AutoScalingMapper.deleteAutoScaling", autoScalingId);
	}
	
	public void deleteASPolicy(Integer autoScalingId) {
		sqlSession.update("AutoScalingMapper.deleteASPolicy", autoScalingId);
	}
	
	public int getAutoScalingListCnt(AutoScalingDto autoScaling) {
		return sqlSession.selectOne("AutoScalingMapper.getAutoScalingListCnt", autoScaling);
	}

	public List<AutoScalingDto> getAutoScalingList(AutoScalingDto autoScaling) {
		return sqlSession.selectList("AutoScalingMapper.getAutoScalingList", autoScaling);
	}
}
//end of AutoScalingDao.java