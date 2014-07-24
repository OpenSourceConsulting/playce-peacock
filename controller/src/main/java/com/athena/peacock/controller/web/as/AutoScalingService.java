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
 * Sang-cheon Park	2013. 11. 6.		First Draft.
 */
package com.athena.peacock.controller.web.as;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.athena.peacock.controller.web.lb.LoadBalancerDao;
import com.athena.peacock.controller.web.lb.LoadBalancerDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("autoScalingService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class AutoScalingService {

	@Inject
	@Named("autoScalingDao")
	private AutoScalingDao autoScalingDao;
	
	@Inject
	@Named("loadBalancerDao")
	private LoadBalancerDao loadBalancerDao;

	public List<AutoScalingDto> inspectMachineStatus() throws Exception {
		return autoScalingDao.inspectMachineStatus();
	}

	public void insertAutoScaling(AutoScalingDto autoScaling) throws Exception {
		autoScalingDao.insertAutoScaling(autoScaling);
		autoScalingDao.insertASPolicy(autoScaling);
		
		// load_balancer_tbl에 해당 load balancer의 auto_scaling_id 값을 세팅한다.
		LoadBalancerDto loadBalancer = loadBalancerDao.getLoadBalancer(autoScaling.getLoadBalancerId());

		Assert.isNull(loadBalancer.getAutoScalingId(), "선택된 Load Balancer는 다른 Auto Scaling에서 사용중입니다.");
		
		loadBalancer.setAutoScalingId(autoScaling.getAutoScalingId());
		loadBalancer.setUpdUserId(autoScaling.getRegUserId());
		loadBalancerDao.updateLoadBalancer(loadBalancer);
	}

	public int getAutoScalingListCnt(AutoScalingDto autoScaling) throws Exception {
		return autoScalingDao.getAutoScalingListCnt(autoScaling);
	}

	public List<AutoScalingDto> getAutoScalingList(AutoScalingDto autoScaling) throws Exception {
		return autoScalingDao.getAutoScalingList(autoScaling);
	}

	public AutoScalingDto getAutoScaling(Integer autoScalingId) throws Exception {
		return autoScalingDao.getAutoScaling(autoScalingId);
	}
	
	public void updateAutoScaling(AutoScalingDto autoScaling) throws Exception {
		// Auto Scaling 설정 중 Load Balancer가 기존과 다른지 확인
		LoadBalancerDto loadBalancer = loadBalancerDao.getLoadBalancerByAutoScalingId(autoScaling.getAutoScalingId());
		
		if (loadBalancer.getLoadBalancerId() != autoScaling.getLoadBalancerId()) {
			// Load Balancer가 기존과 다른 경우 기존 Load Balancer에 매핑되어 있는 auto scaling id를 삭제하고
			// 새로운 Load Balancer에 auto_scaling_id 값을 세팅한다.
			loadBalancer.setAutoScalingId(null);
			loadBalancer.setUpdUserId(autoScaling.getRegUserId());
			loadBalancerDao.updateLoadBalancer(loadBalancer);
			
			loadBalancer = loadBalancerDao.getLoadBalancer(autoScaling.getLoadBalancerId());
			
			Assert.isNull(loadBalancer.getAutoScalingId(), "선택된 Load Balancer는 다른 Auto Scaling에서 사용중입니다.");
			
			loadBalancer.setAutoScalingId(autoScaling.getAutoScalingId());
			loadBalancer.setUpdUserId(autoScaling.getRegUserId());
			loadBalancerDao.updateLoadBalancer(loadBalancer);
		}
		
		autoScalingDao.updateAutoScaling(autoScaling);
		autoScalingDao.updateASPolicy(autoScaling);
	}
	
	public void deleteAutoScaling(AutoScalingDto autoScaling) throws Exception {
		AutoScalingDto as = autoScalingDao.getAutoScaling(autoScaling.getAutoScalingId());
		LoadBalancerDto loadBalancer = loadBalancerDao.getLoadBalancer(as.getLoadBalancerId());
		loadBalancer.setAutoScalingId(null);
		loadBalancer.setUpdUserId(autoScaling.getRegUserId());
		loadBalancerDao.updateLoadBalancer(loadBalancer);
		
		autoScalingDao.deleteASPolicy(autoScaling.getAutoScalingId());
		autoScalingDao.deleteAutoScaling(autoScaling.getAutoScalingId());
	}
}
//end of AutoScalingService.java