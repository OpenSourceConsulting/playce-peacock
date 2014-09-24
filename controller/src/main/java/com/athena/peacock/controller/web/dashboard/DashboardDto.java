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
 * Sang-cheon Park	2014. 9. 19.		First Draft.
 */
package com.athena.peacock.controller.web.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class DashboardDto {

	private List<String> rhevmNames = new ArrayList<String>();
	private Map<String, Integer> vmTotalCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> vmUpCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> templateTotalCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> agentTotalCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> agentRunningCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> cpuCriticalCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> cpuWarningCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> memoryCriticalCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> memoryWarningCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> diskCriticalCnt = new TreeMap<String, Integer>();
	private Map<String, Integer> diskWarningCnt = new TreeMap<String, Integer>();
	
	private Map<String, List<InstanceDto>> vmList = new TreeMap<String, List<InstanceDto>>();
	private Map<String, List<TemplateDto>> templateList = new TreeMap<String, List<TemplateDto>>();
	private Map<String, List<InstanceDto>> agentList = new TreeMap<String, List<InstanceDto>>();
	private Map<String, List<AlarmDto>> criticalList = new TreeMap<String, List<AlarmDto>>();
	private Map<String, List<AlarmDto>> warningList = new TreeMap<String, List<AlarmDto>>();
	
	private int projectCnt = 0;
	private int svnCnt = 0;
	private int jenkinsCnt = 0;
	private int httpdCnt = 0;
	private int tomcatCnt = 0;
	private int jbossCnt = 0;
	
	private List<TopMonitorDto> cpuTopList;
	private List<TopMonitorDto> memoryTopList;
	private List<TopMonitorDto> diskTopList;

	/**
	 * @return the rhevmNames
	 */
	public List<String> getRhevmNames() {
		if (this.rhevmNames == null) {
			this.rhevmNames = new ArrayList<String>();
		}
		return rhevmNames;
	}

	/**
	 * @param rhevmNames the rhevmNames to set
	 */
	public void setRhevmNames(List<String> rhevmNames) {
		this.rhevmNames = rhevmNames;
	}

	/**
	 * @return the rhevmNames
	 */
	public void addRhevmNames(String rhevmName) {
		this.rhevmNames.add(rhevmName);
	}

	/**
	 * @return the vmTotalCnt
	 */
	public Map<String, Integer> getVmTotalCnt() {
		return vmTotalCnt;
	}

	/**
	 * @param vmTotalCnt the vmTotalCnt to set
	 */
	public void setVmTotalCnt(Map<String, Integer> vmTotalCnt) {
		this.vmTotalCnt = vmTotalCnt;
	}

	/**
	 * @return the vmUpCnt
	 */
	public Map<String, Integer> getVmUpCnt() {
		return vmUpCnt;
	}

	/**
	 * @param vmUpCnt the vmUpCnt to set
	 */
	public void setVmUpCnt(Map<String, Integer> vmUpCnt) {
		this.vmUpCnt = vmUpCnt;
	}

	/**
	 * @return the templateTotalCnt
	 */
	public Map<String, Integer> getTemplateTotalCnt() {
		return templateTotalCnt;
	}

	/**
	 * @param templateTotalCnt the templateTotalCnt to set
	 */
	public void setTemplateTotalCnt(Map<String, Integer> templateTotalCnt) {
		this.templateTotalCnt = templateTotalCnt;
	}

	/**
	 * @return the agentTotalCnt
	 */
	public Map<String, Integer> getAgentTotalCnt() {
		return agentTotalCnt;
	}

	/**
	 * @param agentTotalCnt the agentTotalCnt to set
	 */
	public void setAgentTotalCnt(Map<String, Integer> agentTotalCnt) {
		this.agentTotalCnt = agentTotalCnt;
	}

	/**
	 * @return the agentRunningCnt
	 */
	public Map<String, Integer> getAgentRunningCnt() {
		return agentRunningCnt;
	}

	/**
	 * @param agentRunningCnt the agentRunningCnt to set
	 */
	public void setAgentRunningCnt(Map<String, Integer> agentRunningCnt) {
		this.agentRunningCnt = agentRunningCnt;
	}

	/**
	 * @return the cpuCriticalCnt
	 */
	public Map<String, Integer> getCpuCriticalCnt() {
		return cpuCriticalCnt;
	}

	/**
	 * @param cpuCriticalCnt the cpuCriticalCnt to set
	 */
	public void setCpuCriticalCnt(Map<String, Integer> cpuCriticalCnt) {
		this.cpuCriticalCnt = cpuCriticalCnt;
	}

	/**
	 * @return the cpuWarningCnt
	 */
	public Map<String, Integer> getCpuWarningCnt() {
		return cpuWarningCnt;
	}

	/**
	 * @param cpuWarningCnt the cpuWarningCnt to set
	 */
	public void setCpuWarningCnt(Map<String, Integer> cpuWarningCnt) {
		this.cpuWarningCnt = cpuWarningCnt;
	}

	/**
	 * @return the memoryCriticalCnt
	 */
	public Map<String, Integer> getMemoryCriticalCnt() {
		return memoryCriticalCnt;
	}

	/**
	 * @param memoryCriticalCnt the memoryCriticalCnt to set
	 */
	public void setMemoryCriticalCnt(Map<String, Integer> memoryCriticalCnt) {
		this.memoryCriticalCnt = memoryCriticalCnt;
	}

	/**
	 * @return the memoryWarningCnt
	 */
	public Map<String, Integer> getMemoryWarningCnt() {
		return memoryWarningCnt;
	}

	/**
	 * @param memoryWarningCnt the memoryWarningCnt to set
	 */
	public void setMemoryWarningCnt(Map<String, Integer> memoryWarningCnt) {
		this.memoryWarningCnt = memoryWarningCnt;
	}

	/**
	 * @return the diskCriticalCnt
	 */
	public Map<String, Integer> getDiskCriticalCnt() {
		return diskCriticalCnt;
	}

	/**
	 * @param diskCriticalCnt the diskCriticalCnt to set
	 */
	public void setDiskCriticalCnt(Map<String, Integer> diskCriticalCnt) {
		this.diskCriticalCnt = diskCriticalCnt;
	}

	/**
	 * @return the diskWarningCnt
	 */
	public Map<String, Integer> getDiskWarningCnt() {
		return diskWarningCnt;
	}

	/**
	 * @param diskWarningCnt the diskWarningCnt to set
	 */
	public void setDiskWarningCnt(Map<String, Integer> diskWarningCnt) {
		this.diskWarningCnt = diskWarningCnt;
	}

	/**
	 * @return the vmList
	 */
	public Map<String, List<InstanceDto>> getVmList() {
		return vmList;
	}

	/**
	 * @param vmList the vmList to set
	 */
	public void setVmList(Map<String, List<InstanceDto>> vmList) {
		this.vmList = vmList;
	}

	/**
	 * @return the templateList
	 */
	public Map<String, List<TemplateDto>> getTemplateList() {
		return templateList;
	}

	/**
	 * @param templateList the templateList to set
	 */
	public void setTemplateList(Map<String, List<TemplateDto>> templateList) {
		this.templateList = templateList;
	}

	/**
	 * @return the agentList
	 */
	public Map<String, List<InstanceDto>> getAgentList() {
		return agentList;
	}

	/**
	 * @param agentList the agentList to set
	 */
	public void setAgentList(Map<String, List<InstanceDto>> agentList) {
		this.agentList = agentList;
	}

	/**
	 * @return the criticalList
	 */
	public Map<String, List<AlarmDto>> getCriticalList() {
		return criticalList;
	}

	/**
	 * @param criticalList the criticalList to set
	 */
	public void setCriticalList(Map<String, List<AlarmDto>> criticalList) {
		this.criticalList = criticalList;
	}

	/**
	 * @return the warningList
	 */
	public Map<String, List<AlarmDto>> getWarningList() {
		return warningList;
	}

	/**
	 * @param warningList the warningList to set
	 */
	public void setWarningList(Map<String, List<AlarmDto>> warningList) {
		this.warningList = warningList;
	}

	/**
	 * @return the projectCnt
	 */
	public int getProjectCnt() {
		return projectCnt;
	}

	/**
	 * @param projectCnt the projectCnt to set
	 */
	public void setProjectCnt(int projectCnt) {
		this.projectCnt = projectCnt;
	}

	/**
	 * @return the svnCnt
	 */
	public int getSvnCnt() {
		return svnCnt;
	}

	/**
	 * @param svnCnt the svnCnt to set
	 */
	public void setSvnCnt(int svnCnt) {
		this.svnCnt = svnCnt;
	}

	/**
	 * @return the jenkinsCnt
	 */
	public int getJenkinsCnt() {
		return jenkinsCnt;
	}

	/**
	 * @param jenkinsCnt the jenkinsCnt to set
	 */
	public void setJenkinsCnt(int jenkinsCnt) {
		this.jenkinsCnt = jenkinsCnt;
	}

	/**
	 * @return the httpdCnt
	 */
	public int getHttpdCnt() {
		return httpdCnt;
	}

	/**
	 * @param httpdCnt the httpdCnt to set
	 */
	public void setHttpdCnt(int httpdCnt) {
		this.httpdCnt = httpdCnt;
	}

	/**
	 * @return the tomcatCnt
	 */
	public int getTomcatCnt() {
		return tomcatCnt;
	}

	/**
	 * @param tomcatCnt the tomcatCnt to set
	 */
	public void setTomcatCnt(int tomcatCnt) {
		this.tomcatCnt = tomcatCnt;
	}

	/**
	 * @return the jbossCnt
	 */
	public int getJbossCnt() {
		return jbossCnt;
	}

	/**
	 * @param jbossCnt the jbossCnt to set
	 */
	public void setJbossCnt(int jbossCnt) {
		this.jbossCnt = jbossCnt;
	}

	/**
	 * @return the cpuTopList
	 */
	public List<TopMonitorDto> getCpuTopList() {
		return cpuTopList;
	}

	/**
	 * @param cpuTopList the cpuTopList to set
	 */
	public void setCpuTopList(List<TopMonitorDto> cpuTopList) {
		this.cpuTopList = cpuTopList;
	}

	/**
	 * @return the memoryTopList
	 */
	public List<TopMonitorDto> getMemoryTopList() {
		return memoryTopList;
	}

	/**
	 * @param memoryTopList the memoryTopList to set
	 */
	public void setMemoryTopList(List<TopMonitorDto> memoryTopList) {
		this.memoryTopList = memoryTopList;
	}

	/**
	 * @return the diskTopList
	 */
	public List<TopMonitorDto> getDiskTopList() {
		return diskTopList;
	}

	/**
	 * @param diskTopList the diskTopList to set
	 */
	public void setDiskTopList(List<TopMonitorDto> diskTopList) {
		this.diskTopList = diskTopList;
	}
}
//end of DashboardDto.java