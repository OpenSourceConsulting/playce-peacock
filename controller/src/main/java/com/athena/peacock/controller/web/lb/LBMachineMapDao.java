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
 * Sang-cheon Park	2013. 10. 29.		First Draft.
 */
package com.athena.peacock.controller.web.lb;

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
@Repository("lbMachineMapDao")
public class LBMachineMapDao extends AbstractBaseDao {
	
	public void insertLBMachineMap(LoadBalancerDto loadBalancer) {
		sqlSession.insert("LBMachineMapMapper.insertLBMachineMap", loadBalancer);
	}
	
	public void deleteLBMachineMap(LoadBalancerDto loadBalancer) {
		sqlSession.delete("LBMachineMapMapper.deleteLBMachineMap", loadBalancer);
	}
	
	public LoadBalancerDto getLBMachineMap(LoadBalancerDto loadBalancer) {
		return sqlSession.selectOne("LBMachineMapMapper.getLBMachineMap", loadBalancer);
	}

	public int getLBMachineMapListCnt(LoadBalancerDto loadBalancer) {
		return sqlSession.selectOne("LBMachineMapMapper.getLBMachineMapListCnt", loadBalancer);
	}

	public List<LoadBalancerDto> getLBMachineMapList(LoadBalancerDto loadBalancer) {
		return sqlSession.selectList("LBMachineMapMapper.getLBMachineMapList", loadBalancer);
	}
}
//end of LBMachineMapDao.java