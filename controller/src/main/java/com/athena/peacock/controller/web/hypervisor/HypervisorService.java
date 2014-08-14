/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * Sang-cheon Park	2014. 8. 12.		First Draft.
 */
package com.athena.peacock.controller.web.hypervisor;

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
@Service("hypervisorService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class HypervisorService {
    
	@Inject
	@Named("hypervisorDao")
	private HypervisorDao hypervisorDao;
	
	public void insertHypervisor(HypervisorDto hypervisor) throws Exception {	
		hypervisorDao.insertHypervisor(hypervisor);
	}

	public HypervisorDto selectHypervisor(Integer hypervisorId) {	
		return hypervisorDao.selectHypervisor(hypervisorId);
	}
	
	public void updateHypervisor(HypervisorDto hypervisor) throws Exception {
		hypervisorDao.updateHypervisor(hypervisor);
	}
	
	public void deleteHypervisor(Integer hypervisorId) throws Exception {
		hypervisorDao.deleteHypervisor(hypervisorId);
	}
	
	public List<HypervisorDto> getHypervisorList() throws Exception {
		return hypervisorDao.getHypervisorList();
	}
}
//end of HypervisorService.java