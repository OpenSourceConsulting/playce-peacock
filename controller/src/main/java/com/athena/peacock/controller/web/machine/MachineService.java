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
package com.athena.peacock.controller.web.machine;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.common.core.action.FileWriteAction;
import com.athena.peacock.common.core.action.support.TargetHost;
import com.athena.peacock.common.core.command.Command;
import com.athena.peacock.common.core.util.SshExecUtil;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.controller.common.component.RHEVMRestTemplateManager;
import com.athena.peacock.controller.netty.PeacockTransmitter;
import com.athena.peacock.controller.web.rhevm.RHEVApi;
import com.redhat.rhevm.api.model.VM;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
@Service("machineService")
@Transactional(rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
public class MachineService {
    
	@Inject
	@Named("machineDao")
	private MachineDao machineDao;

    @Inject
    @Named("peacockTransmitter")
	private PeacockTransmitter peacockTransmitter;

	public void insertMachine(MachineDto machine) throws Exception {	
		MachineDto m = machineDao.getMachine(machine.getMachineId());
		
		if (m != null) {
			if (StringUtils.isEmpty(machine.getDisplayId())) {
				machine.setDisplayId(m.getDisplayId());
			}
			if (StringUtils.isEmpty(machine.getDisplayName())) {
				machine.setDisplayName(m.getDisplayName());
			}
			machineDao.updateMachine(machine);
		} else {
			String displayId = "i-" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
			
			while (true) {
				if (machineDao.checkDuplicateDisplayId(displayId) == 0) {
					machine.setDisplayId(displayId);
					break;
				} else {
					displayId = "i-" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
				}
			}

			machineDao.insertMachine(machine);
		}
	}

	public int getMachineListCnt(MachineDto machine) throws Exception {
		return machineDao.getMachineListCnt(machine);
	}

	public List<MachineDto> getMachineList(MachineDto machine) throws Exception {
		return machineDao.getMachineList(machine);
	}

	public MachineDto getMachine(String machineId) throws Exception {
		return machineDao.getMachine(machineId);
	}
	
	public void deleteMachine(String machineId) throws Exception {
		machineDao.deleteMachine(machineId);
	}

	public void updateMachine(MachineDto machine) throws RestClientException, Exception {
		if (machine.getDisplayName().toLowerCase().startsWith("hhilws") && !machine.getDisplayName().toLowerCase().startsWith("hhilwsd")) {
			machine.setIsPrd("Y");
		} else {
			machine.setIsPrd("N");
		}

		MachineDto m = machineDao.getMachine(machine.getMachineId());
		m.setDisplayName(machine.getDisplayName());
		m.setIsPrd(machine.getIsPrd());
		
		machineDao.updateMachine(m);
		
		if (m.getHypervisorId() != null && m.getHypervisorId() > 0) {
			VM vm = new VM();
			vm.setName(m.getDisplayName());
			RHEVMRestTemplateManager.getRHEVMRestTemplate(m.getHypervisorId()).submit(RHEVApi.VMS + "/" + m.getMachineId(), HttpMethod.PUT, vm, "vm", VM.class);
		}
		
		// 세팅하려는 고정 IP 값이 있는지.. 기존 IP와 다른지 검사하여 고정 IP 변경 작업을 수행한다.
		// TODO IP Address 변경없이 NETMASK, GATEWAY, NAMESERVER가 변경된 경우의 처리는??
        if (StringUtils.isNotEmpty(machine.getIpAddress()) && !machine.getIpAddress().equals(machine.getIpAddr())) {
        	applyStaticIp(machine);
        }
	}
	
	public MachineDto getAdditionalInfo(String machineId) {
		return machineDao.getAdditionalInfo(machineId);
	}
	
	public void insertAdditionalInfo(MachineDto machine) {
		machineDao.insertAdditionalInfo(machine);
	}
	
	public void updateAdditionalInfo(MachineDto machine) {
		machineDao.updateAdditionalInfo(machine);
	}
	
	public void agentStart(String machineId) throws Exception {
		List<String> commandList = new ArrayList<String>();
		commandList.add("service peacock-agent start");
		
		sendCommand(machineId, commandList);
	}
	
	public void agentStop(String machineId) throws Exception {
		List<String> commandList = new ArrayList<String>();
		commandList.add("service peacock-agent stop");
		
		sendCommand(machineId, commandList);
	}
	
	public void agentRestart(String machineId) throws Exception {
		List<String> commandList = new ArrayList<String>();
		commandList.add("service peacock-agent restart");
		
		sendCommand(machineId, commandList);
	}
	
	public void networkRestart(String machineId) throws Exception {
		List<String> commandList = new ArrayList<String>();
		commandList.add("service network restart");
		
		sendCommand(machineId, commandList);
	}
	
	private void sendCommand(String machineId, List<String> commandList) throws Exception {
		MachineDto machine = getAdditionalInfo(machineId);
		
		TargetHost targetHost = new TargetHost();
		targetHost.setHost(machine.getIpAddr());
		targetHost.setPort(Integer.parseInt(machine.getSshPort()));
		targetHost.setUsername(machine.getSshUsername());
		targetHost.setPassword(machine.getSshPassword());
		targetHost.setKeyfile(machine.getSshKeyFile());

		SshExecUtil.executeCommand(targetHost, commandList);
	}
	
	public void applyStaticIp(MachineDto machine) throws Exception {
		// 1. /etc/sysconfig/network-scripts/ifcfg-eth0 파일에 저장될 내용을 구성한다.
		StringBuilder ifcfg = new StringBuilder();
		ifcfg.append("DEVICE=etho0").append("\n")
			 .append("BOOTPROTO=static").append("\n")
			 .append("ONBOOT=yes").append("\n")
			 .append("IPADDR=").append(machine.getIpAddress()).append("\n")
			 .append("NETMASK=").append(machine.getNetmask()).append("\n")
			 .append("GATEWAY=").append(machine.getGateway()).append("\n");
		
		// 2. /etc/resolv.conf 파일에 저장될 내용을 구성한다.
		StringBuilder nameserver = new StringBuilder();
		String[] servers = machine.getNameServer().split(",");
		for (String server : servers) {
			nameserver.append("nameserver ").append(server).append("\n");
		}
		
		// 3. ifcnf-eth0, resolv.conf 파일을 저장한다.
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(machine.getMachineId());
		cmdMsg.setBlocking(true);

		int sequence = 0;
		Command command = new Command("SET_STATIC_IP");
		
		FileWriteAction fwAction = new FileWriteAction(sequence++);
		fwAction.setContents(ifcfg.toString());
		fwAction.setFileName("/etc/sysconfig/network-scripts/ifcfg-eth0");
		command.addAction(fwAction);
		
		// nameserver 정보가 존재하지 않을 경우 세팅하지 않는다.
		if (StringUtils.isNotEmpty(nameserver.toString())) {
			fwAction = new FileWriteAction(sequence++);
			fwAction.setContents(nameserver.toString());
			fwAction.setFileName("/etc/resolv.conf");
			command.addAction(fwAction);
		}

		cmdMsg.addCommand(command);

		PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
		peacockTransmitter.sendMessage(datagram);
		
		// 4. network 서비스를 재구동한다.
		networkRestart(machine.getMachineId());
		
		// 5. machine_additional_info_tbl의 apply_yn 값을 업데이트 한다. 
		machineDao.applyStaticIp(machine.getMachineId());
		
		// 6. Peacock Agent를 재구동한다.
		agentRestart(machine.getMachineId());
	}
}
//end of MachineService.java