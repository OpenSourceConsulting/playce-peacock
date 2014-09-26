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

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import com.athena.peacock.common.core.action.FileWriteAction;
import com.athena.peacock.common.core.action.ShellAction;
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

    protected final Logger logger = LoggerFactory.getLogger(MachineService.class);
    
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
			// Edit Instance 메뉴에서 고정 IP를 세팅하면 RHEV Manager가 즉각 반영되지 않아 
			// hypervisorId 및 cluster를 조회하지 못하는 경우가 있다.
			// 따라서 기존 값을 그대로 활용한다.
			if (StringUtils.isEmpty(machine.getCluster())) {
				machine.setCluster(m.getCluster());
				machine.setIsVm("Y");
			}
			if (machine.getHypervisorId() == null) {
				machine.setHypervisorId(m.getHypervisorId());
				machine.setIsVm("Y");
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
		MachineDto m = machineDao.getMachine(machine.getMachineId());
		
		// Instance 이름이 변경되었을 경우 DB, RHEV-M 업데이트
		if (!machine.getDisplayName().equals(m.getDisplayName())) {
			if (m.getHypervisorId() != null && m.getHypervisorId() > 0) {
				int major = RHEVMRestTemplateManager.getRHEVMRestTemplate(m.getHypervisorId()).getMajor();
				int minor = RHEVMRestTemplateManager.getRHEVMRestTemplate(m.getHypervisorId()).getMinor();

				VM vm = null;
				
				double version = Double.parseDouble(major + "." + minor);
				if (version >= 3.2) {
					vm = new VM();
					vm.setName(machine.getDisplayName());
					RHEVMRestTemplateManager.getRHEVMRestTemplate(m.getHypervisorId()).submit(RHEVApi.VMS + "/" + m.getMachineId(), HttpMethod.PUT, vm, "vm", VM.class);
				} else {
					String callUrl = RHEVApi.VMS + "/" + machine.getMachineId();
					vm = RHEVMRestTemplateManager.getRHEVMRestTemplate(m.getHypervisorId()).submit(callUrl, HttpMethod.GET, VM.class);
					
					if (vm.getStatus().getState().toLowerCase().equals("down")) {
						vm = new VM();
						vm.setName(machine.getDisplayName());
						RHEVMRestTemplateManager.getRHEVMRestTemplate(m.getHypervisorId()).submit(RHEVApi.VMS + "/" + m.getMachineId(), HttpMethod.PUT, vm, "vm", VM.class);
					} else {
						throw new Exception("VM_UP_STAT");
					}
				}
			}
			
			if (machine.getDisplayName().toLowerCase().startsWith("hhilws") && !machine.getDisplayName().toLowerCase().startsWith("hhilwsd")) {
				m.setIsPrd("Y");
			} else {
				m.setIsPrd("N");
			}
			
			m.setDisplayName(machine.getDisplayName());
			m.setUpdUserId(machine.getUpdUserId());
			
			machineDao.updateMachine(m);
		}
		
		MachineDto add = getAdditionalInfo(machine.getMachineId());
		if (add == null) {
			insertAdditionalInfo(machine);
		} else {
			machine.setApplyYn(add.getApplyYn());
			
			if (StringUtils.isNotEmpty(machine.getIpAddress()) 
					&& (!machine.getIpAddress().equals(add.getIpAddress())
							|| !machine.getNetmask().equals(add.getNetmask())
							|| !machine.getGateway().equals(add.getGateway()) 
							|| !machine.getNameServer().equals(add.getNameServer()))) {
				machine.setApplyYn("N");
			}
			
			updateAdditionalInfo(machine);
		}
		
		if (StringUtils.isNotEmpty(machine.getHostName()) && !machine.getHostName().equals(m.getHostName())) {

			// Agent가 Running 상태일 경우 ShellAction으로 agent의 chhost.sh 스크립트 실행
			// Agent가 Down 상태일 경우 machine_additional_info_tbl에 업데이트 되어 Running 상태로 변경 시 HostName이 변경된다.
			if (StringUtils.isNotEmpty(machine.getIpAddress()) && peacockTransmitter.isActive(machine.getMachineId())) {
				try {
					// /etc/hosts 파일에 저장될 ipAddress가 현재 Machine의 IP인지 수정 대상 IP인지 확인한다.
					String ipAddress = null;
					
					if (StringUtils.isNotEmpty(machine.getIpAddress())) {
						ipAddress = machine.getIpAddress();
					} else {
						ipAddress = m.getIpAddr();
					}
					
					
					ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
					cmdMsg.setAgentId(machine.getMachineId());
					cmdMsg.setBlocking(true);

					int sequence = 0;
					Command command = new Command("SET_HOSTNAME");
					
					ShellAction action = new ShellAction(sequence++);
					action.setCommand("sh");
					action.addArguments("chhost.sh");
					action.addArguments(ipAddress);
					action.addArguments(machine.getHostName());
					command.addAction(action);
					
					cmdMsg.addCommand(command);
					
					PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
					peacockTransmitter.sendMessage(datagram);
				} catch (Exception e) {
					// HostName 변경이 실패하더라고 고정 IP 세팅을 할 수 있도록 예외를 무시한다.
					logger.error("Unhandled exception has occurred while change hostname.", e);
				}
			}
		}
		
		// 세팅하려는 고정 IP 값이 있는지, Agent가 Running 상태인지, 기존 IP와 다른지 검사하여 고정 IP 변경 작업을 수행한다.
        if (StringUtils.isNotEmpty(machine.getIpAddress()) 
        		&& peacockTransmitter.isActive(machine.getMachineId())
        		&& (!machine.getIpAddress().equals(add.getIpAddress())
							|| !machine.getNetmask().equals(add.getNetmask())
							|| !machine.getGateway().equals(add.getGateway()) 
							|| !machine.getNameServer().equals(add.getNameServer()))) {
        	machine.setIpAddr(m.getIpAddr());
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
		sendCommand(getMachine(machineId), "service peacock-agent start");
	}
	
	public void agentStop(String machineId) throws Exception {
		sendCommand(getMachine(machineId), "service peacock-agent stop");
	}
	
	private void sendCommand(MachineDto machine, String command) throws Exception {
		TargetHost targetHost = new TargetHost();
		targetHost.setHost(machine.getIpAddr());
		targetHost.setPort(Integer.parseInt(machine.getSshPort()));
		targetHost.setUsername(machine.getSshUsername());
		targetHost.setPassword(machine.getSshPassword());
		targetHost.setKeyfile(machine.getSshKeyFile());
		
		String result = SshExecUtil.executeCommand(targetHost, command);
		
		logger.debug("Command : [{}], Result : [{}]", command, result);
	}
	
	public void applyStaticIp(MachineDto machine) throws Exception {
		// Static IP 적용 여부
		boolean result = false;
		
		// 1. /etc/sysconfig/network-scripts/ifcfg-eth0 파일에 저장될 내용을 구성한다.
		StringBuilder ifcfg = new StringBuilder();
		ifcfg.append("DEVICE=eth0").append("\n")
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
		
		logger.debug("ifcfg-etho : [{}], resolv.conf : [{}]", ifcfg.toString(), nameserver.toString());
		
		// 3. ifcnf-eth0, resolv.conf 파일을 저장한다.
		ProvisioningCommandMessage cmdMsg = new ProvisioningCommandMessage();
		cmdMsg.setAgentId(machine.getMachineId());
		//cmdMsg.setBlocking(true);

		int sequence = 0;
		Command command = new Command("SET_STATIC_IP_ifcfg-eth0");
		
		FileWriteAction fwAction = new FileWriteAction(sequence++);
		fwAction.setContents(ifcfg.toString());
		fwAction.setFileName("/etc/sysconfig/network-scripts/ifcfg-eth0");
		command.addAction(fwAction);
		cmdMsg.addCommand(command);
		
		PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
		peacockTransmitter.sendMessage(datagram);

		// nameserver 정보가 존재하지 않을 경우 세팅하지 않는다.
		if (StringUtils.isNotEmpty(nameserver.toString())) {
			cmdMsg = new ProvisioningCommandMessage();
			cmdMsg.setAgentId(machine.getMachineId());
			//cmdMsg.setBlocking(true);
			
			command = new Command("SET_STATIC_IP_resolv.conf");
			
			fwAction = new FileWriteAction(sequence++);
			fwAction.setContents(nameserver.toString());
			fwAction.setFileName("/etc/resolv.conf");
			command.addAction(fwAction);
			cmdMsg.addCommand(command);
			
			datagram = new PeacockDatagram<AbstractMessage>(cmdMsg);
			peacockTransmitter.sendMessage(datagram);
		}
		
		// PeacockServerHandler의 channelRead0()로부터 호출된 경우 FileWriteAction()을 Blocking 형식으로 실행하면 무한 Block에 빠질 수 있다.
		// 따라서 Non-Block 형식으로 호출 하고 해당 파일이 변경되었는지 확인한다.
		TargetHost targetHost = new TargetHost();
		targetHost.setHost(machine.getIpAddr());
		targetHost.setPort(Integer.parseInt(machine.getSshPort()));
		targetHost.setUsername(machine.getSshUsername());
		targetHost.setPassword(machine.getSshPassword());
		targetHost.setKeyfile(machine.getSshKeyFile());
		
		String output1 = null, output2 = null;
		int retryCnt1 = 0, retryCnt2 = 0;
		while (retryCnt1++ < 3) {
			output1 = SshExecUtil.executeCommand(targetHost, "cat /etc/sysconfig/network-scripts/ifcfg-eth0");
			
			if (output1.indexOf("IPADDR=" + machine.getIpAddress()) >= 0) {				
				// 4. network 서비스를 재구동한다.
				TargetHost th1 = new TargetHost();
				th1.setHost(machine.getIpAddr());
				th1.setPort(Integer.parseInt(machine.getSshPort()));
				th1.setUsername(machine.getSshUsername());
				th1.setPassword(machine.getSshPassword());
				th1.setKeyfile(machine.getSshKeyFile());
				
				new NetworkRestarter(th1).start();
				
				targetHost.setHost(machine.getIpAddress());
				while (retryCnt2++ < 30) {
					try {
						output2 = SshExecUtil.executeCommand(targetHost, "ifconfig");

						if (output2.indexOf(machine.getIpAddress()) >= 0) {
							TargetHost th2 = new TargetHost();
							th2.setHost(machine.getIpAddress());
							th2.setPort(Integer.parseInt(machine.getSshPort()));
							th2.setUsername(machine.getSshUsername());
							th2.setPassword(machine.getSshPassword());
							th2.setKeyfile(machine.getSshKeyFile());
							
							// 5. Peacock Agent를 재구동한다.
							machine.setIpAddr(machine.getIpAddress());
							
							new AgentRestarter(th2).start();

							// 6. machine_additional_info_tbl의 apply_yn 값을 업데이트 한다. 
							machineDao.applyStaticIp(machine.getMachineId());
							
							// 7. Agent와 연결된 기존 Channel을 닫는다.
							peacockTransmitter.channelClose(machine.getMachineId());
							
							result = true;
							break;
						}
						
						Thread.sleep(1000);
					} catch (Exception e) {
						// service network restart가 완료되기 전까지 변경된 IP로 ifconfig 명령을 실행할 수 없다.(JSchException)
						// ignore...
					}
				}
				
				break;
			}
			
			Thread.sleep(1000);
		}
		
		if (!result) {
			throw new Exception("static ip set has failed.");
		}
	}
}
//end of MachineService.java

class NetworkRestarter extends Thread {
    protected final Logger logger = LoggerFactory.getLogger(NetworkRestarter.class);
    
	private TargetHost targetHost;
	
	public NetworkRestarter(TargetHost targetHost) {
		this.targetHost = targetHost;
	}
	
	@Override
	public void run() {		
		logger.debug("[targetHost in NetworkRestarter] : [{}]", targetHost.toString());
		
		try {
			String command = "service network restart";
			String result = SshExecUtil.executeCommand(targetHost, command);
			logger.debug("Command : [{}], Result : [{}]", command, result);
		} catch (IOException e) {
			logger.error("Unhandled exception has occurred.", e);
		}
	}
}

class AgentRestarter extends Thread {
    protected final Logger logger = LoggerFactory.getLogger(AgentRestarter.class);
    
	private TargetHost targetHost;
	
	public AgentRestarter(TargetHost targetHost) {
		this.targetHost = targetHost;
	}
	
	@Override
	public void run() {
		logger.debug("[targetHost in AgentRestarter] : [{}]", targetHost.toString());
		
		try {
			String command = "service peacock-agent restart";
			String result = SshExecUtil.executeCommand(targetHost, command);
			logger.debug("Command : [{}], Result : [{}]", command, result);
		} catch (IOException e) {
			logger.error("Unhandled exception has occurred.", e);
		}
	}
}