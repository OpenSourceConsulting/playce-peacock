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
 * Sang-cheon Park	2013. 8. 27.		First Draft.
 */
package com.athena.peacock.controller.common.core.handler;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.common.constant.PeacockConstant;
import com.athena.peacock.controller.common.core.InitializingTask;
import com.athena.peacock.controller.common.util.ThreadLocalUtil;
import com.athena.peacock.controller.web.monitor.MonFactorDao;
import com.athena.peacock.controller.web.monitor.MonFactorDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("monFactorHandler")
public class MonFactorHandler implements InitializingTask {

    protected final Logger LOGGER = LoggerFactory.getLogger(MonFactorHandler.class);
    
    private List<MonFactorDto> monFactorList;
    
    @Inject
    @Named("monFactorDao")
    private MonFactorDao monFactorDao;

	/* (non-Javadoc)
	 * @see com.athena.peacock.controller.common.core.InitializingTask#init()
	 */
	@Override
	public void init() {
		try {
			monFactorList = monFactorDao.getMonFactorList();
			ThreadLocalUtil.add(PeacockConstant.MON_FACTOR_LIST, monFactorList);
			
			LOGGER.debug("mon_factor_tbl fetch result : [{}]", monFactorList);
		} catch (Exception e) {
			LOGGER.error("can not initiate mon_factor_tbl info : ", e);
		}
	}

	/**
	 * @return the monFactorList
	 */
	public List<MonFactorDto> getMonFactorList() {
		if (monFactorList == null) {
			init();
		}
		
		return monFactorList;
	}

}
//end of MonFactorHandler.java