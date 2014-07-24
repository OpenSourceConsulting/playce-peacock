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

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("machineService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class MachineService {
    
	@Inject
	@Named("machineDao")
	private MachineDao machineDao;

	public void insertMachine(MachineDto machine) throws Exception {		
		if (machineDao.getMachine(machine.getMachineId()) != null) {
			machineDao.updateMachine(machine);
		} else {
			machineDao.insertMachine(machine);
		}
	}

	public int getMachineListCnt(MachineDto machine) throws Exception {
		return machineDao.getMachineListCnt(machine);
	}

	public List<MachineDto> getMachineList(MachineDto machine) throws Exception {
		return machineDao.getMachineList(machine);
	}

	public MachineDto getMachine(String machineId) throws Exception {
		return machineDao.getMachine(machineId);
	}
	
	public void deleteMachine(String machineId) throws Exception {
		machineDao.deleteMachine(machineId);
	}
}
//end of MachineService.java