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
 * Sang-cheon Park	2013. 8. 25.		First Draft.
 */
package com.athena.peacock.controller.web.machine;

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
@Repository("machineDao")
public class MachineDao extends AbstractBaseDao {
	
	public MachineDto getMachine(String machineId) {
		return sqlSession.selectOne("MachineMapper.getMachine", machineId);
	}
	
	public int checkDuplicateDisplayId(String displayId) {
		return sqlSession.selectOne("MachineMapper.checkDuplicateDisplayId", displayId);
	}
	
	public void insertMachine(MachineDto machine) {
		sqlSession.insert("MachineMapper.insertMachine", machine);
	}
	
	public void updateMachine(MachineDto machine) {
		sqlSession.update("MachineMapper.updateMachine", machine);
	}
	
	public void deleteMachine(String machineId) {
		sqlSession.update("MachineMapper.deleteMachine", machineId);
	}
	
	public int getMachineListCnt(MachineDto machine) {
		return sqlSession.selectOne("MachineMapper.getMachineListCnt", machine);
	}

	public List<MachineDto> getMachineList(MachineDto machine) {
		return sqlSession.selectList("MachineMapper.getMachineList", machine);
	}
	
	public MachineDto getAdditionalInfo(String machineId) {
		return sqlSession.selectOne("MachineMapper.getAdditionalInfo", machineId);
	}
	
	public void insertAdditionalInfo(MachineDto machine) {
		sqlSession.insert("MachineMapper.insertAdditionalInfo", machine);
	}
	
	public void updateAdditionalInfo(MachineDto machine) {
		sqlSession.update("MachineMapper.updateAdditionalInfo", machine);
	}
	
	public void applyStaticIp(String machineId) {
		sqlSession.update("MachineMapper.applyStaticIp", machineId);
	}
}
//end of MachineDao.java