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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.dashboard.AlarmDto;
import com.athena.peacock.controller.web.dashboard.DashboardDto;
import com.athena.peacock.controller.web.dashboard.TopMonitorDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Repository("monDataDao")
public class MonDataDao extends AbstractBaseDao {

    @Value("#{contextProperties['cpu.critical.value']}")
    private int cpuCriticalValue;

    @Value("#{contextProperties['memory.critical.value']}")
    private int memoryCriticalValue;

    @Value("#{contextProperties['disk.critical.value']}")
    private int diskCriticalValue;

    @Value("#{contextProperties['cpu.warning.value']}")
    private int cpuWarningValue;

    @Value("#{contextProperties['memory.warning.value']}")
    private int memoryWarningValue;

    @Value("#{contextProperties['disk.warning.value']}")
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

	public void getAlarmList(Integer hypervisorId, String rhevmName, DashboardDto dto) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("hypervisorId", hypervisorId);
		param.put("cpuCriticalValue", cpuCriticalValue);
		param.put("memoryCriticalValue", memoryCriticalValue);
		param.put("diskCriticalValue", diskCriticalValue);
		param.put("cpuWarningValue", cpuWarningValue);
		param.put("memoryWarningValue", memoryWarningValue);
		param.put("diskWarningValue", diskWarningValue);
		
		int criticalCpuAlarmCnt = 0;
		int criticalMemoryAlarmCnt = 0;
		int criticalDiskAlarmCnt = 0;
		
		int warningCpuAlarmCnt = 0;
		int warningMemoryAlarmCnt = 0;
		int warningDiskAlarmCnt = 0;
		
		// cpu critial alarm
		List<AlarmDto> alarmList = sqlSession.selectList("MonDataMapper.getCpuCritalList", param);
		
		Map<String, AlarmDto> criticalAlarmMap = new TreeMap<String, AlarmDto>();
		for (AlarmDto alarm : alarmList) {
			criticalAlarmMap.put(alarm.getInstanceName(), alarm);
			criticalCpuAlarmCnt++;
		}
		
		// memory critical alarm
		alarmList = sqlSession.selectList("MonDataMapper.getMemoryCritalList", param);
		for (AlarmDto alarm : alarmList) {
			if (criticalAlarmMap.containsKey(alarm.getInstanceName())) {
				criticalAlarmMap.get(alarm.getInstanceName()).setMemory(alarm.getMemory());
			} else {
				criticalAlarmMap.put(alarm.getInstanceName(), alarm);
			}
			
			criticalMemoryAlarmCnt++;
		}

		// cpu warning alarm
		alarmList = sqlSession.selectList("MonDataMapper.getCpuWarningList", param);

		Map<String, AlarmDto> warningAlarmMap = new TreeMap<String, AlarmDto>();
		for (AlarmDto alarm : alarmList) {
			warningAlarmMap.put(alarm.getInstanceName(), alarm);
			warningCpuAlarmCnt++;
		}
		
		// memory warning alarm
		alarmList = sqlSession.selectList("MonDataMapper.getMemoryWarningList", param);
		for (AlarmDto alarm : alarmList) {
			if (warningAlarmMap.containsKey(alarm.getInstanceName())) {
				warningAlarmMap.get(alarm.getInstanceName()).setMemory(alarm.getMemory());
			} else {
				warningAlarmMap.put(alarm.getInstanceName(), alarm);
			}
			
			warningMemoryAlarmCnt++;
		}
		
		// disk critical / warning alarm
		MonDataDto monData = new MonDataDto();
		monData.setHypervisorId(hypervisorId);
		monData.setMonFactorId("FACTOR_006");
		monData.setTimeRange("1h");
		List<MonDataDto> monDataList = sqlSession.selectList("MonDataMapper.getMonDataList", monData);
		
		AlarmDto alarm = null;
		String value = null;
		String[] partitions = null;
		
		for (MonDataDto md : monDataList) {
			value = md.getMonDataValue();
			
			partitions = value.split(",");
			
			for (String partition : partitions) {
				if (Double.parseDouble(partition.split(":")[1]) >= (double) diskWarningValue &&
						Double.parseDouble(partition.split(":")[1]) < (double) diskCriticalValue) {

					if (warningAlarmMap.containsKey(md.getInstanceName()) && warningAlarmMap.get(md.getInstanceName()).getDisk() != null) {
						continue;
					}
					
					if (warningAlarmMap.containsKey(md.getInstanceName())) {
						warningAlarmMap.get(md.getInstanceName()).setDisk(true);
					} else {
						alarm = new AlarmDto();
						alarm.setInstanceName(md.getInstanceName());
						alarm.setDisk(true);
						warningAlarmMap.put(alarm.getInstanceName(), alarm);
					}

					warningDiskAlarmCnt++;
				} else if (Double.parseDouble(partition.split(":")[1]) >= (double) diskCriticalValue) {

					if (criticalAlarmMap.containsKey(md.getInstanceName()) && criticalAlarmMap.get(md.getInstanceName()).getDisk() != null) {
						continue;
					}
					
					if (criticalAlarmMap.containsKey(md.getInstanceName())) {
						criticalAlarmMap.get(md.getInstanceName()).setDisk(true);
					} else {
						alarm = new AlarmDto();
						alarm.setInstanceName(md.getInstanceName());
						alarm.setDisk(true);
						criticalAlarmMap.put(alarm.getInstanceName(), alarm);
					}

					criticalDiskAlarmCnt++;
				}
			}
		}
		
		// top 5 - cpu
		List<TopMonitorDto> topList = null;
		if (dto.getCpuTopList() == null) {
			topList = sqlSession.selectList("MonDataMapper.getTop5CpuList");
			dto.setCpuTopList(topList);
		}
		
		// top 5 - memory
		if (dto.getMemoryTopList() == null) {
			topList = sqlSession.selectList("MonDataMapper.getTop5MemoryList");
			dto.setMemoryTopList(topList);
		}
		
		// top 5 - disk
		if (dto.getDiskTopList() == null) {
			topList = sqlSession.selectList("MonDataMapper.getTop5DiskList");
			dto.setDiskTopList(topList);
		}
		
		dto.getCriticalList().put(rhevmName, new ArrayList<AlarmDto>(criticalAlarmMap.values()));
		dto.getWarningList().put(rhevmName, new ArrayList<AlarmDto>(warningAlarmMap.values()));
		
		dto.getCpuCriticalCnt().put(rhevmName, criticalCpuAlarmCnt);
		dto.getMemoryCriticalCnt().put(rhevmName, criticalMemoryAlarmCnt);
		dto.getDiskCriticalCnt().put(rhevmName, criticalDiskAlarmCnt);
		
		dto.getCpuWarningCnt().put(rhevmName, warningCpuAlarmCnt);
		dto.getMemoryWarningCnt().put(rhevmName, warningMemoryAlarmCnt);
		dto.getDiskWarningCnt().put(rhevmName, warningDiskAlarmCnt);
	}
}
//end of MonDataDao.java