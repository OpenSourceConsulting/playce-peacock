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
 * Sang-cheon Park	2013. 11. 1.		First Draft.
 */
package com.athena.peacock.controller.web.as;

import com.athena.peacock.controller.web.common.dto.BaseDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class AutoScalingDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private Integer hypervisorId;
	
	// Fields for auto_scaling_tbl
	private Integer autoScalingId;
	private String autoScalingName;
	private String vmTemplateId;
	private Integer minMachineSize;
	private Integer maxMachineSize;
	
	// Fileds for as_policy_tbl
	private String policyName;
	private String monFactorId;
	private String displayName;
	private Integer thresholdUpLimit;
	private Integer thresholdDownLimit;
	private Integer thresholdUpPeriod;
	private Integer thresholdDownPeriod;
	private Integer increaseUnit;
	private Integer decreaseUnit;
	
	// Quartz에 의해 주기적인 모니터링 항목 체크 시 사용되는 추가 Fields.
	private String machineId;
	private Long monDataValue;
	private String scaleType;
	private Integer currentMachineSize;
	private Integer loadBalancerId;
	private String lbMachineId;

	/**
	 * @return the hypervisorId
	 */
	public Integer getHypervisorId() {
		return hypervisorId;
	}

	/**
	 * @param hypervisorId the hypervisorId to set
	 */
	public void setHypervisorId(Integer hypervisorId) {
		this.hypervisorId = hypervisorId;
	}

	/**
	 * @return the autoScalingId
	 */
	public Integer getAutoScalingId() {
		return autoScalingId;
	}

	/**
	 * @param autoScalingId the autoScalingId to set
	 */
	public void setAutoScalingId(Integer autoScalingId) {
		this.autoScalingId = autoScalingId;
	}

	/**
	 * @return the autoScalingName
	 */
	public String getAutoScalingName() {
		return autoScalingName;
	}

	/**
	 * @param autoScalingName the autoScalingName to set
	 */
	public void setAutoScalingName(String autoScalingName) {
		this.autoScalingName = autoScalingName;
	}

	/**
	 * @return the vmTemplateId
	 */
	public String getVmTemplateId() {
		return vmTemplateId;
	}

	/**
	 * @param vmTemplateId the vmTemplateId to set
	 */
	public void setVmTemplateId(String vmTemplateId) {
		this.vmTemplateId = vmTemplateId;
	}

	/**
	 * @return the minMachineSize
	 */
	public Integer getMinMachineSize() {
		return minMachineSize;
	}

	/**
	 * @param minMachineSize the minMachineSize to set
	 */
	public void setMinMachineSize(Integer minMachineSize) {
		this.minMachineSize = minMachineSize;
	}

	/**
	 * @return the maxMachineSize
	 */
	public Integer getMaxMachineSize() {
		return maxMachineSize;
	}

	/**
	 * @param maxMachineSize the maxMachineSize to set
	 */
	public void setMaxMachineSize(Integer maxMachineSize) {
		this.maxMachineSize = maxMachineSize;
	}

	/**
	 * @return the policyName
	 */
	public String getPolicyName() {
		return policyName;
	}

	/**
	 * @param policyName the policyName to set
	 */
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	/**
	 * @return the monFactorId
	 */
	public String getMonFactorId() {
		return monFactorId;
	}

	/**
	 * @param monFactorId the monFactorId to set
	 */
	public void setMonFactorId(String monFactorId) {
		this.monFactorId = monFactorId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the thresholdUpLimit
	 */
	public Integer getThresholdUpLimit() {
		return thresholdUpLimit;
	}

	/**
	 * @param thresholdUpLimit the thresholdUpLimit to set
	 */
	public void setThresholdUpLimit(Integer thresholdUpLimit) {
		this.thresholdUpLimit = thresholdUpLimit;
	}

	/**
	 * @return the thresholdDownLimit
	 */
	public Integer getThresholdDownLimit() {
		return thresholdDownLimit;
	}

	/**
	 * @param thresholdDownLimit the thresholdDownLimit to set
	 */
	public void setThresholdDownLimit(Integer thresholdDownLimit) {
		this.thresholdDownLimit = thresholdDownLimit;
	}

	/**
	 * @return the thresholdUpPeriod
	 */
	public Integer getThresholdUpPeriod() {
		return thresholdUpPeriod;
	}

	/**
	 * @param thresholdUpPeriod the thresholdUpPeriod to set
	 */
	public void setThresholdUpPeriod(Integer thresholdUpPeriod) {
		this.thresholdUpPeriod = thresholdUpPeriod;
	}

	/**
	 * @return the thresholdDownPeriod
	 */
	public Integer getThresholdDownPeriod() {
		return thresholdDownPeriod;
	}

	/**
	 * @param thresholdDownPeriod the thresholdDownPeriod to set
	 */
	public void setThresholdDownPeriod(Integer thresholdDownPeriod) {
		this.thresholdDownPeriod = thresholdDownPeriod;
	}

	/**
	 * @return the increaseUnit
	 */
	public Integer getIncreaseUnit() {
		return increaseUnit;
	}

	/**
	 * @param increaseUnit the increaseUnit to set
	 */
	public void setIncreaseUnit(Integer increaseUnit) {
		this.increaseUnit = increaseUnit;
	}

	/**
	 * @return the decreaseUnit
	 */
	public Integer getDecreaseUnit() {
		return decreaseUnit;
	}

	/**
	 * @param decreaseUnit the decreaseUnit to set
	 */
	public void setDecreaseUnit(Integer decreaseUnit) {
		this.decreaseUnit = decreaseUnit;
	}

	/**
	 * @return the machineId
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId the machineId to set
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * @return the monDataValue
	 */
	public Long getMonDataValue() {
		return monDataValue;
	}

	/**
	 * @param monDataValue the monDataValue to set
	 */
	public void setMonDataValue(Long monDataValue) {
		this.monDataValue = monDataValue;
	}

	/**
	 * @return the scaleType
	 */
	public String getScaleType() {
		return scaleType;
	}

	/**
	 * @param scaleType the scaleType to set
	 */
	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
	}

	/**
	 * @return the currentMachineSize
	 */
	public Integer getCurrentMachineSize() {
		return currentMachineSize;
	}

	/**
	 * @param currentMachineSize the currentMachineSize to set
	 */
	public void setCurrentMachineSize(Integer currentMachineSize) {
		this.currentMachineSize = currentMachineSize;
	}

	/**
	 * @return the loadBalancerId
	 */
	public Integer getLoadBalancerId() {
		return loadBalancerId;
	}

	/**
	 * @param loadBalancerId the loadBalancerId to set
	 */
	public void setLoadBalancerId(Integer loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}

	/**
	 * @return the lbMachineId
	 */
	public String getLbMachineId() {
		return lbMachineId;
	}

	/**
	 * @param lbMachineId the lbMachineId to set
	 */
	public void setLbMachineId(String lbMachineId) {
		this.lbMachineId = lbMachineId;
	}
}
//end of AutoScalingDto.java