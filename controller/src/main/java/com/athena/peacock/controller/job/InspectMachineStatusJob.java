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
package com.athena.peacock.controller.job;

import java.util.List;

import com.athena.peacock.common.provider.AppContext;
import com.athena.peacock.common.scheduler.InternalJobExecutionException;
import com.athena.peacock.common.scheduler.quartz.BaseJob;
import com.athena.peacock.common.scheduler.quartz.JobExecution;
import com.athena.peacock.controller.web.as.AutoScalingDto;
import com.athena.peacock.controller.web.as.AutoScalingService;
import com.athena.peacock.controller.web.lb.LBListenerService;
import com.athena.peacock.controller.web.lb.LoadBalancerDto;
import com.athena.peacock.controller.web.lb.LoadBalancerService;
import com.athena.peacock.controller.web.machine.MachineService;
import com.athena.peacock.controller.web.rhevm.RHEVMService;
import com.athena.peacock.controller.web.rhevm.dto.VMDto;
import com.redhat.rhevm.api.model.VM;

/**
 * <pre>
 * Auto Scaling을 위해 Auto Scaling 대상 인스턴스의 모니터링 상태를 조회하기 위한 Quartz Job
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class InspectMachineStatusJob extends BaseJob {
	
	private AutoScalingService autoScalingService;
	
	public void setAutoScalingService(AutoScalingService autoScalingService) {
		this.autoScalingService = autoScalingService;
	}

	/* (non-Javadoc)
	 * @see com.athena.peacock.common.scheduler.quartz.BaseJob#executeInternal(com.athena.peacock.common.scheduler.quartz.JobExecution)
	 */
	@Override
	protected void executeInternal(JobExecution context) throws InternalJobExecutionException {
		
		if (autoScalingService == null) {
			autoScalingService = AppContext.getBean(AutoScalingService.class);
		}
		
		try {
			List<AutoScalingDto> autoScalingList = autoScalingService.inspectMachineStatus();
			
			LOGGER.debug("Inspection Result => {}", autoScalingList);
			
			Long value = 0L;
			int currentMachineSize = 0;
			int minMachineSize = 0;
			int maxMachineSize = 0;
			for (AutoScalingDto autoScaling : autoScalingList) {
				value = autoScaling.getMonDataValue();
				currentMachineSize = autoScaling.getCurrentMachineSize();
				minMachineSize = autoScaling.getMinMachineSize();
				maxMachineSize = autoScaling.getMaxMachineSize();
				
				if (AutoScalingStatus.getProcessCount(autoScaling.getAutoScalingId()) != null 
						&& AutoScalingStatus.getProcessCount(autoScaling.getAutoScalingId()) > 0) {
					long diff = System.currentTimeMillis() - AutoScalingStatus.getStartTime(autoScaling.getAutoScalingId());
					
					// 10분 이상 지난 Auto Scaling 작업 요청은 삭제한다.
					if (diff >= (10 * 60 * 1000)) {
						AutoScalingStatus.removeStatus(autoScaling.getAutoScalingId());
					} else {
						continue;
					}
				}
				
				// currentMachineSize가 minMachineSize보다 적을 경우 minMachineSize만큼 인스턴스를 늘린다.
				if (currentMachineSize < minMachineSize) {
					int increaseSize = minMachineSize - currentMachineSize;

					AutoScalingStatus.addStatus(autoScaling.getAutoScalingId(), increaseSize);
					
					for (int i = 0; i < increaseSize; i++) {
						new ScaleOutThread(autoScaling).start();
					}
					
					continue;
				}
				
				// currentMachineSize가 maxMachineSize보다 클 경우 maxMachineSize만큼 인스턴스를 줄인다.
				if (currentMachineSize > maxMachineSize) {
					int decreaseSize = currentMachineSize - maxMachineSize;
					
					AutoScalingStatus.addStatus(autoScaling.getAutoScalingId(), decreaseSize);
					
					for (AutoScalingDto as : autoScalingList) {
						if (as.getAutoScalingId().equals(autoScaling.getAutoScalingId())) {
							if (decreaseSize-- > 0) {
								new ScaleInThread(as).start();
							} else {
								break;
							}
						}
					}
					
					continue;
				}
				
				if (autoScaling.getScaleType().toLowerCase().indexOf("out") > -1) {
					if (value > autoScaling.getThresholdUpLimit()) {
						if (currentMachineSize < maxMachineSize) {
							// Scale Out 실행
							// increaseUnit, currentMachineSize, maxMachineSize를 이용하여 생성해야 할 머신 수를 계산한다.
							int increaseSize = maxMachineSize - currentMachineSize;
							
							if (increaseSize > autoScaling.getIncreaseUnit()) {
								increaseSize = autoScaling.getIncreaseUnit();
							}
							
							AutoScalingStatus.addStatus(autoScaling.getAutoScalingId(), increaseSize);
							
							for (int i = 0; i < increaseSize; i++) {
								new ScaleOutThread(autoScaling).start();
							}
						} else {
							LOGGER.debug("[Auto Scaling ID : {}] 현재 동작 중인 머신 수가 설정된 최대 머신 수 보다 크거나 같기 때문에 Scale Out을 수행할 수 없습니다.", autoScaling.getAutoScalingId());
						}
					}
				} else {
					if (value < autoScaling.getThresholdDownLimit()) {
						if (currentMachineSize > minMachineSize) {
							// Scale In 실행
							// decreaseUnit, currentMachineSize, minMachineSize를 이용하여 중지해야 할 머신 수를 계산한다.
							int decreaseSize = currentMachineSize - minMachineSize;
							
							if (decreaseSize > autoScaling.getDecreaseUnit()) {
								decreaseSize = autoScaling.getDecreaseUnit();
							}
							
							AutoScalingStatus.addStatus(autoScaling.getAutoScalingId(), decreaseSize);
							
							for (AutoScalingDto as : autoScalingList) {
								//if (as.getAutoScalingId() == autoScaling.getAutoScalingId()) {
								if (as.getAutoScalingId().equals(autoScaling.getAutoScalingId())) {
									if (decreaseSize-- > 0) {
										new ScaleInThread(as).start();
									} else {
										break;
									}
								}
							}
						} else {
							LOGGER.debug("[Auto Scaling ID : {}] 현재 동작 중인 머신 수가 설정된 최소 머신 수 보다 작거나 같기 때문에 Scale In을 수행할 수 없습니다.", autoScaling.getAutoScalingId());
						}
					}
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("Unhandled exception has occurred.", e);
			throw new InternalJobExecutionException(e);
		}
	}
}
//end of InspectMachineStatusJob.java

class ScaleOutThread extends Thread {

	private final AutoScalingDto autoScaling;
	private final RHEVMService rhevmService;
	private final MachineService machineService;
	private final LoadBalancerService loadBalancerService;
	private final LBListenerService lbListenerService;
	
	public ScaleOutThread(AutoScalingDto autoScaling) {
		this.autoScaling = autoScaling;

		rhevmService = AppContext.getBean(RHEVMService.class);
		machineService = AppContext.getBean(MachineService.class);
		loadBalancerService = AppContext.getBean(LoadBalancerService.class);
		lbListenerService = AppContext.getBean(LBListenerService.class);
	}

	@Override
	public void run() {
		try {
			/**
			 * 1. RHEV Manager에게 vm template, vm name을 이용하여 새로운 인스턴스를 생성하도록 요청한다.
			 */
			String vmName = autoScaling.getAutoScalingName() + "-" + System.currentTimeMillis();
			vmName = vmName.replaceAll(" ", "_");
			VM vm = rhevmService.createVirtualMachine(autoScaling.getHypervisorId(), vmName, autoScaling.getVmTemplateId());

			/**
			 * 2. Thread.sleep(5000)을 이용하여 인스턴스 생성이 완료되었는지 확인한다.
			 */
			VMDto vmDto = null;
			while (true) {
				vmDto = rhevmService.getVirtualMachine(autoScaling.getHypervisorId(), vm.getId());
				
				if (vmDto.getStatus().toLowerCase().equals("down")) {
					break;
				}
				
				Thread.sleep(5000);
			}

			/**
			 * 3. 인스턴스 생성이 완료되면 인스턴스를 시작한다.
			 */
			rhevmService.startVirtualMachine(autoScaling.getHypervisorId(), vm.getId());

			/**
			 * 4. Thread.sleep(5000)을 이용하여 인스턴스가 시작되었는지 확인한다.
			 */
			vmDto = null;
			while (true) {
				vmDto = rhevmService.getVirtualMachine(autoScaling.getHypervisorId(), vm.getId());
				
				if (vmDto.getStatus().toLowerCase().equals("up")) {
					break;
				}
				
				Thread.sleep(5000);
			}

			/**
			 * 5. machine_tbl에 해당 인스턴스가 등록되어 있는지 확인한다.(Agent가 자동 실행될 경우)
			 */
			int retry = 6;
			while (retry-- > 0) {
				if (machineService.getMachine(vm.getId()) != null) {
					/**
					 * 6. lb_machine_map_tbl에 해당 인스턴스를 추가하고 Load Balancer를 reload 시킨다.
					 */
					LoadBalancerDto loadBalancer = new LoadBalancerDto();
					loadBalancer.setLoadBalancerId(autoScaling.getLoadBalancerId());
					loadBalancer.setMachineId(vm.getId());
					loadBalancerService.insertLBMachineMap(loadBalancer);
					
					loadBalancer.setMachineId(autoScaling.getLbMachineId());
					lbListenerService.applyListener(loadBalancer);
					
					break;
				}
				
				Thread.sleep(10000);
			}
			
			AutoScalingStatus.complete(autoScaling.getAutoScalingId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//end of ScaleOutThread.java

class ScaleInThread extends Thread {

	private final AutoScalingDto autoScaling;
	
	private final RHEVMService rhevmService;
	private final MachineService machineService;
	private final LoadBalancerService loadBalancerService;
	private final LBListenerService lbListenerService;
	
	public ScaleInThread(AutoScalingDto autoScaling) {
		this.autoScaling = autoScaling;
		
		rhevmService = AppContext.getBean(RHEVMService.class);
		machineService = AppContext.getBean(MachineService.class);
		loadBalancerService = AppContext.getBean(LoadBalancerService.class);
		lbListenerService = AppContext.getBean(LBListenerService.class);
	}

	@Override
	public void run() {
		try {
			/**
			 * 1. lb_machine_map_tbl에 해당 인스턴스를 제거하고 Load Balancer를 reload 시킨다.
			 */
			LoadBalancerDto loadBalancer = new LoadBalancerDto();
			loadBalancer.setLoadBalancerId(autoScaling.getLoadBalancerId());
			loadBalancer.setMachineId(autoScaling.getMachineId());
			loadBalancerService.deleteLBMachineMap(loadBalancer);
			
			loadBalancer.setMachineId(autoScaling.getLbMachineId());
			lbListenerService.applyListener(loadBalancer);
			
			/**
			 * 2. RHEV Manager에게 해당 인스턴스를 중지(Shutdown)하도록 요청한다.
			 */
			rhevmService.shutdownVirtualMachine(autoScaling.getHypervisorId(), autoScaling.getMachineId());

			/**
			 * 3. Thread.sleep(5000)을 이용하여 인스턴스가 중지되었는지 확인한다.
			 */
			VMDto vmDto = null;
			while (true) {
				vmDto = rhevmService.getVirtualMachine(autoScaling.getHypervisorId(), autoScaling.getMachineId());
				
				if (vmDto.getStatus().toLowerCase().equals("down")) {
					break;
				}
				
				Thread.sleep(5000);
			}

			/**
			 * 4. RHEV Manager에세 해당 인스턴스를 제거(Remove) 하도록 요청한다.
			 */
			rhevmService.removeVirtualMachine(autoScaling.getHypervisorId(), autoScaling.getMachineId());

			/**
			 * 5. machine_tbl에서 해당 인스턴스를 삭제 처리한다.
			 */
			machineService.deleteMachine(autoScaling.getMachineId());

			AutoScalingStatus.complete(autoScaling.getAutoScalingId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//end of ScaleInThread.java