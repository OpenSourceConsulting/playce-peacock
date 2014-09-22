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
package com.athena.peacock.controller.web.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.dashboard.AlarmDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Repository("monDataDao")
public class MonDataDao extends AbstractBaseDao {

    @Value("#{contextProperties['cpu.critical.value'] ? : 90}")
    private int cpuCriticalValue;

    @Value("#{contextProperties['memory.critical.value'] ? : 90}")
    private int memoryCriticalValue;

    @Value("#{contextProperties['disk.critical.value'] ? : 90}")
    private int diskCriticalValue;

    @Value("#{contextProperties['cpu.warning.value'] ? : 70}")
    private int cpuWarninglValue;

    @Value("#{contextProperties['memory.warning.value'] ? : 70}")
    private int memoryWarningValue;

    @Value("#{contextProperties['disk.warning.value'] ? : 70}")
    private int diskWarningValue;

	public void insertMonData(MonDataDto monData) {
		sqlSession.insert("MonDataMapper.insertMonData", monData);
	}

	public List<MonDataDto> getMonDataList(MonDataDto monData) {
		return sqlSession.selectList("MonDataMapper.getMonDataList", monData);
	}

	public List<Map<String, String>> getAllMonDataList(MonDataDto monData) {
		return sqlSession.selectList("MonDataMapper.getAllMonDataList", monData);
	}

	public Map<String, AlarmDto> getCriticalList(Integer hypervisorId) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("hypervisorId", hypervisorId);
		param.put("cpuCriticalValue", cpuCriticalValue);
		param.put("memoryCriticalValue", memoryCriticalValue);
		param.put("diskCriticalValue", diskCriticalValue);
		
		// cpu critial alarm
		List<AlarmDto> alarmList = sqlSession.selectList("MonDataMapper.getCpuCritalList", param);
		
		Map<String, AlarmDto> alarmMap = new TreeMap<String, AlarmDto>();
		for (AlarmDto alarm : alarmList) {
			alarmMap.put(alarm.getInstanceName(), alarm);
		}
		
		alarmList = sqlSession.selectList("MonDataMapper.getMemoryCritalList", param);
		for (AlarmDto alarm : alarmList) {
			if (alarmMap.containsKey(alarm.getInstanceName())) {
				alarmMap.get(alarm.getInstanceName()).setMemory(alarm.getMemory());
			} else {
				alarmMap.put(alarm.getInstanceName(), alarm);
			}
		}
		
		MonDataDto monData = new MonDataDto();
		List<MonDataDto> monDataList = sqlSession.selectList("MonDataMapper.getMonDataList", monData);
		//alarmList = sqlSession.selectList("MonDataMapper.getDiskCritalList", param);

		
		
		return null;
	}

	public Map<String, AlarmDto> getWarningList(Integer hypervisorId) {
		// TODO Auto-generated method stub
		return null;
	}
}
//end of MonDataDao.java