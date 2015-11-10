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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.athena.peacock.common.core.action.ShellAction;
import com.athena.peacock.common.core.command.Command;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.common.netty.message.ProvisioningResponseMessage;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.machine.MachineDao;
import com.athena.peacock.controller.web.machine.MachineDto;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("loadBalancerService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class LoadBalancerService {
	
    protected final Logger LOGGER = LoggerFactory.getLogger(LoadBalancerService.class);
    
	@Inject
	@Named("loadBalancerDao")
	private LoadBalancerDao loadBalancerDao;
	
	@Inject
	@Named("lbMachineMapDao")
	private LBMachineMapDao lbMachineMapDao;
	
	@Inject
	@Named("machineDao")
	private MachineDao machineDao;
	
    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;
	
	public void insertLoadBalancer(LoadBalancerDto loadBalancer) throws Exception {
		loadBalancerDao.insertLoadBalancer(loadBalancer);
	}
	
	public void updateLoadBalancer(LoadBalancerDto loadBalancer) throws Exception {
		loadBalancerDao.updateLoadBalancer(loadBalancer);
	}
	
	public void deleteLoadBalancer(int loadBalancerId) throws Exception {
		loadBalancerDao.deleteLoadBalancer(loadBalancerId);
	}
	
	public LoadBalancerDto getLoadBalancer(int loadBalancerId) throws Exception {
		return loadBalancerDao.getLoadBalancer(loadBalancerId);
	}

	public int getLoadBalancerListCnt(LoadBalancerDto loadBalancer) throws Exception {
		return loadBalancerDao.getLoadBalancerListCnt(loadBalancer);
	}
	
	public List<LoadBalancerDto> getLoadBalancerList(LoadBalancerDto loadBalancer) throws Exception {
		return loadBalancerDao.getLoadBalancerList(loadBalancer);
	}

	public void insertLBMachineMap(LoadBalancerDto loadBalancer) {
		lbMachineMapDao.insertLBMachineMap(loadBalancer);
	}

	public void deleteLBMachineMap(LoadBalancerDto loadBalancer) {
		lbMachineMapDao.deleteLBMachineMap(loadBalancer);
	}

	public int getLBMachineMapListCnt(LoadBalancerDto loadBalancer) {
		return lbMachineMapDao.getLBMachineMapListCnt(loadBalancer);
	}

	public List<?> getLBMachineMapList(LoadBalancerDto loadBalancer) {
		return lbMachineMapDao.getLBMachineMapList(loadBalancer);
	}

	public boolean createLoadBalancer(LoadBalancerDto loadBalancer) throws Exception {
		MachineDto machine = machineDao.getMachine(loadBalancer.getMachineId());
		
		String fileName = "haproxy-1.4.22-5.el6_4.i686.rpm";
		if (machine != null) {
			if (machine.getOsArch().indexOf("64") > -1) {
				fileName = "haproxy-1.4.22-5.el6_4.x86_64.rpm";
			}
		} else {
			return false;
		}
		
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(loadBalancer.getMachineId());
		cmdMsg.setBlocking(true);
		
		Command command = new Command("HAProxy INSTALL");
		int sequence = 0;
		
		ShellAction sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/usr/local/src");
		sAction.setCommand("wget");
		sAction.addArguments("${RepositoryUrl}/haproxy/" + fileName);
		sAction.addArguments("-O");
		sAction.addArguments(fileName);
		command.addAction(sAction);
		
		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/usr/local/src");
		sAction.setCommand("rpm");
		sAction.addArguments("-Uvh");
		sAction.addArguments(fileName);
		command.addAction(sAction);
		
		sAction = new ShellAction(sequence++);
		sAction.setWorkingDiretory("/etc/haproxy");
		sAction.setCommand("wget");
		sAction.addArguments("${RepositoryUrl}/haproxy/haproxy.cfg");
		sAction.addArguments("-O");
		sAction.addArguments("haproxy.cfg");
		command.addAction(sAction);

		sAction = new ShellAction(sequence++);
		sAction.setCommand("service");
		sAction.addArguments("haproxy");
		sAction.addArguments("start");
		command.addAction(sAction);
		
		// Add HAProxy INSTALL Command
		cmdMsg.addCommand(command);
		

		LOGGER.debug("Send create LB message to agent on client. Machine ID is {}", machine.getMachineId());
		
		PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
		ProvisioningResponseMessage response = peacockTransmitter.sendMessage(datagram);
		
		LOGGER.debug("Created Load Balancer as below.\n{}", response.getResults());
		
		// TODO loadBalancer에 AS_GROUP_ID 관련 내용 확인
		
		loadBalancerDao.insertLoadBalancer(loadBalancer);
		
		return true;
	}
}
//end of LoadBalancerService.java