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
@Repository("lbListenerDao")
public class LBListenerDao extends AbstractBaseDao {
	
	public void insertLBListener(LBListenerDto lbListener) {
		sqlSession.insert("LBListenerMapper.insertLBListener", lbListener);
	}
	
	public void updateLBListener(LBListenerDto lbListener) {
		sqlSession.update("LBListenerMapper.updateLBListener", lbListener);
	}
	
	public void deleteLBListener(LBListenerDto lbListener) {
		sqlSession.delete("LBListenerMapper.deleteLBListener", lbListener);
	}
	
	public LBListenerDto getLBListener(LBListenerDto lbListener) {
		return sqlSession.selectOne("LBListenerMapper.getLBListener", lbListener);
	}
	
	public int getLBListenerListCnt(LBListenerDto lbListener) {
		return sqlSession.selectOne("LBListenerMapper.getLBListenerListCnt", lbListener);
	}

	public List<LBListenerDto> getLBListenerList(LBListenerDto lbListener) {
		return sqlSession.selectList("LBListenerMapper.getLBListenerList", lbListener);
	}
}
//end of LBListenerDao.java