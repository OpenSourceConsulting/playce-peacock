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
 * Sang-cheon Park	2013. 7. 17.		First Draft.
 */
package com.athena.peacock.controller.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AbstractMessage;
import com.athena.peacock.common.netty.message.AgentInitialInfoMessage;
import com.athena.peacock.common.netty.message.AgentSystemStatusMessage;
import com.athena.peacock.common.netty.message.MessageType;
import com.athena.peacock.common.netty.message.OSPackageInfoMessage;
import com.athena.peacock.common.netty.message.PackageInfo;
import com.athena.peacock.common.netty.message.ProvisioningResponseMessage;
import com.athena.peacock.common.provider.AppContext;
import com.athena.peacock.controller.common.component.RHEVMRestTemplate;
import com.athena.peacock.controller.common.component.RHEVMRestTemplateManager;
import com.athena.peacock.controller.common.core.handler.MonFactorHandler;
import com.athena.peacock.controller.web.machine.MachineDto;
import com.athena.peacock.controller.web.machine.MachineService;
import com.athena.peacock.controller.web.monitor.MonDataDto;
import com.athena.peacock.controller.web.monitor.MonFactorDto;
import com.athena.peacock.controller.web.monitor.MonitorService;
import com.athena.peacock.controller.web.ospackage.PackageDto;
import com.athena.peacock.controller.web.ospackage.PackageService;
import com.redhat.rhevm.api.model.Cluster;
import com.redhat.rhevm.api.model.IP;
import com.redhat.rhevm.api.model.VM;
import com.redhat.rhevm.api.model.VMs;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("peacockServerHandler")
@Sharable
public class PeacockServerHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger logger = LoggerFactory.getLogger(PeacockServerHandler.class);
	
	@Inject
	@Named("machineService")
	private MachineService machineService;
	
	@Inject
	@Named("monitorService")
	private MonitorService monitorService;
	
	@Inject
	@Named("packageService")
	private PackageService packageService;

	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("channelRead0() has invoked.");
		logger.debug("[Server] IP Address => " + ctx.channel().remoteAddress().toString());
		logger.debug("[Server] Object => " + msg.getClass().getName());
		//logger.debug("[Server] Contents => " + msg.toString());
		
		if("bye".equals(msg.toString())) {
			// Response and exit.
			ChannelFuture future = ctx.write("This channel will be closed.");
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			if(msg instanceof PeacockDatagram) {
				MessageType messageType = ((PeacockDatagram<?>)msg).getMessageType();

				switch (messageType) {
					case COMMAND : 
						break;
					case RESPONSE : 
						ProvisioningResponseMessage responseMsg = ((PeacockDatagram<ProvisioningResponseMessage>)msg).getMessage();
						
						if (responseMsg.isBlocking()) {
							CallbackManagement.poll().handle(responseMsg);
						}
						break;
					case SYSTEM_STATUS : 
						AgentSystemStatusMessage statusMsg = ((PeacockDatagram<AgentSystemStatusMessage>)msg).getMessage();

						//ThreadLocal cannot use.
						//List<MonFactorDto> monFactorList = (List<MonFactorDto>) ThreadLocalUtil.get(PeacockConstant.MON_FACTOR_LIST);
						List<MonFactorDto> monFactorList = AppContext.getBean(MonFactorHandler.class).getMonFactorList();
						
						List<MonDataDto> monDataList = new ArrayList<MonDataDto>();
						MonDataDto monData = null;
						
						for (MonFactorDto monFactor : monFactorList) {
							monData = new MonDataDto();
							
							monData.setMachineId(statusMsg.getAgentId());
							monData.setMonFactorId(monFactor.getMonFactorId());
							monData.setMonDataValue(getMonDataValue(monFactor, statusMsg));
							monData.setRegUserId(1);
							monData.setUpdUserId(1);
							
							monDataList.add(monData);
						}
						
						if (this.monitorService == null) {
							monitorService = AppContext.getBean(MonitorService.class);
						}
						
						monitorService.insertMonDataList(monDataList);
						
						break;
					case INITIAL_INFO : 
						AgentInitialInfoMessage infoMsg = ((PeacockDatagram<AgentInitialInfoMessage>)msg).getMessage();

						String ipAddr = ctx.channel().remoteAddress().toString();
						ipAddr = ipAddr.substring(1, ipAddr.indexOf(":"));
						
						// ipAddr에 해당하는 rhev_id를 RHEV Manager로부터 조회한다.
						String machineId = infoMsg.getAgentId();
						Integer hypervisorId = null;
						String displayName = null;
						String clusterName = null;
						String isPrd = "N";
						String isVm = "N";
						boolean isMatch = false;
						try {
							List<RHEVMRestTemplate> templates = RHEVMRestTemplateManager.getAllTemplates();
							
							for (RHEVMRestTemplate restTemplate : templates) {
								VMs vms = restTemplate.submit("/api/vms?search=" + ipAddr, HttpMethod.GET, VMs.class);
								
								if (vms.getVMs().size() > 0) {
									isVm = "Y";
									
									List<VM> vmList = vms.getVMs();
									List<IP> ipList = null;
									for (VM vm : vmList) {
										// ip를 이용한 조회로써 getIps()가 null이 아님.
										ipList = vm.getGuestInfo().getIps().getIPs();
										
										for (IP ip : ipList) {
											if (ip.getAddress().equals(ipAddr)) {
												isMatch = true;
												machineId = vm.getId();
												hypervisorId = restTemplate.getHypervisorId();
												displayName = vm.getName();
												
												Cluster cluster = restTemplate.submit(vm.getCluster().getHref(), HttpMethod.GET, Cluster.class);
												clusterName = cluster.getName();
												break;
											}
										}

										if (isMatch) {
											break;
										}
									}
								}

								if (isMatch) {
									break;
								}
							}
						} catch (Exception e) {
							// ignore
							logger.error("Unhandled Exception has occurred.", e);
						}
						
						if (this.machineService == null) {
							machineService = AppContext.getBean(MachineService.class);
						}
						
						// machine_additional_info_tbl에 고정 IP로 변경해야 할 내용이 있는지 조회한다.(현재 연결된 IP와 applyYn 정보를 이용)
						MachineDto machine = machineService.getAdditionalInfo(machineId);
						
						boolean resetIp = false;
						if (machine != null && StringUtils.isNotEmpty(machine.getIpAddress())) {
							if (machine.getApplyYn().equals("N") && !machine.getIpAddress().equals(ipAddr)) {
								// 고정 IP 세팅이 필요할 경우 Agent가 재구동 되기 때문에 더 이상의 처리를 하지 않는다.
								machineService.applyStaticIp(machine);
								resetIp = true;
							}
						}
						
						if (!resetIp) {
							// register a new channel
							ChannelManagement.registerChannel(machineId, ctx.channel());
							
							machine = new MachineDto();
							machine.setMachineId(machineId);
							machine.setHypervisorId(hypervisorId);
							machine.setDisplayName(displayName);
							
							if (StringUtils.isNotEmpty(displayName)) {
								if (displayName.toLowerCase().startsWith("hhilws") && !displayName.toLowerCase().startsWith("hhilwsd")) {
									isPrd = "Y";
								}
							}
							machine.setIsPrd(isPrd);
							
							machine.setMachineMacAddr(infoMsg.getMacAddrMap().get(ipAddr));
							machine.setIsVm(isVm);
							machine.setCluster(clusterName);
							machine.setOsName(infoMsg.getOsName());
							machine.setOsVer(infoMsg.getOsVersion());
							machine.setOsArch(infoMsg.getOsArch());
							machine.setCpuClock(Integer.toString(infoMsg.getCpuClock()));
							machine.setCpuNum(Integer.toString(infoMsg.getCpuNum()));
							machine.setMemSize(Long.toString(infoMsg.getMemSize()));
							machine.setIpAddr(ipAddr);
							machine.setHostName(infoMsg.getHostName());
							machine.setRegUserId(1);
							machine.setUpdUserId(1);
							
							if (this.machineService == null) {
								machineService = AppContext.getBean(MachineService.class);
							}
							
							machineService.insertMachine(machine);
							
							infoMsg = new AgentInitialInfoMessage();
							infoMsg.setAgentId(machineId);
							PeacockDatagram<AbstractMessage> datagram = new PeacockDatagram<AbstractMessage>(infoMsg);
							ctx.channel().writeAndFlush(datagram);
						}
						
						break;
					case PACKAGE_INFO :
						OSPackageInfoMessage packageMsg = ((PeacockDatagram<OSPackageInfoMessage>)msg).getMessage();
						List<PackageInfo> packageInfoList = packageMsg.getPackageInfoList();
						PackageInfo packageInfo = null;
						List<PackageDto> packageList = new ArrayList<PackageDto>();
						PackageDto ospackage = null;
						for (int i = 0; i < packageInfoList.size(); i++) {
							packageInfo = packageInfoList.get(i);
							
							ospackage = new PackageDto();
							ospackage.setPkgId(i + 1);
							ospackage.setMachineId(packageMsg.getAgentId());
							ospackage.setName(packageInfo.getName());
							ospackage.setArch(packageInfo.getArch());
							ospackage.setSize(packageInfo.getSize());
							ospackage.setVersion(packageInfo.getVersion());
							ospackage.setReleaseInfo(packageInfo.getRelease());
							ospackage.setInstallDate(packageInfo.getInstallDate());
							ospackage.setSummary(packageInfo.getSummary());
							ospackage.setDescription(packageInfo.getDescription());
							
							packageList.add(ospackage);
						}
						
						if (packageList.size() > 0) {
							if (packageService == null) {
								packageService = AppContext.getBean(PackageService.class);
							}
							
							packageService.insertPackageList(packageList);
						}
						
						break;
				}
			}
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete() has invoked.");
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {	
		logger.debug("channelActive() has invoked.");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive() has invoked.");

		// deregister a closed channel
		ChannelManagement.deregisterChannel(ctx.channel());
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		logger.debug("handlerAdded() has invoked.");
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Unexpected exception from downstream.", cause);
        
        // ctx will not be closed.
        //if (!(cause instanceof NestedRuntimeException)) {
        //	ctx.close();
        //}
    }
    
    private String getMonDataValue(MonFactorDto monFactor, AgentSystemStatusMessage statusMsg) {
    	String value = null;
    			
    	if (monFactor.getMonFactorName().toLowerCase().indexOf("cpu") > -1) {
			if (monFactor.getMonFactorName().toLowerCase().indexOf("idle") > -1) {
				value = statusMsg.getIdleCpu();
			} else if (monFactor.getMonFactorName().toLowerCase().indexOf("combined") > -1) {
				value = statusMsg.getCombinedCpu();
			}    		
		} else if (monFactor.getMonFactorName().toLowerCase().indexOf("memory") > -1) {
			if (monFactor.getMonFactorName().toLowerCase().indexOf("total") > -1) {
				value = statusMsg.getTotalMem();
			} else if (monFactor.getMonFactorName().toLowerCase().indexOf("free") > -1) {
				value = statusMsg.getFreeMem();
			} else if (monFactor.getMonFactorName().toLowerCase().indexOf("used") > -1) {
				value = statusMsg.getUsedMem();
			}
		} 
    	
    	return value;
    }
    
    /**
     * <pre>
     * 해당 Agent로 Provisioning 관련 명령을 전달하고 필요 시 응답을 반환한다.
     * </pre>
     * @param datagram
     * @return
     * @throws Exception 
     */
    public ProvisioningResponseMessage sendMessage(PeacockDatagram<AbstractMessage> datagram) throws Exception {
    	Channel channel = ChannelManagement.getChannel(datagram.getMessage().getAgentId());
    	boolean isBlocking = datagram.getMessage().isBlocking();

    	if (isBlocking) {
			Callback callback = new Callback(); 
			CallbackManagement.lock(); 
			
			try { 
				CallbackManagement.add(callback); 
				
				if (channel != null) {
					channel.writeAndFlush(datagram);
				} else {
					throw new Exception("Channel is null.");
				}
			} finally { 
				CallbackManagement.unlock(); 
			} 
			
			return callback.get(); 
    	} else {
			if (channel != null) {
				channel.writeAndFlush(datagram);
			} else {
				throw new Exception("Channel is null.");
			}
    		
    		return null;
    	}
    }//end of sendMessage()
    
    /**
     * <pre>
     * channelMap 내에 agentId에 해당하는 Channel이 등록되어 있으면 true, 아니면 false
     * </pre>
     * @param agentId
     * @return
     */
    public boolean isActive(String agentId) {
    	return ChannelManagement.getChannel(agentId) != null ? true : false;
    }
    
    /**
     * <pre>
     * Channel 관리를 위한 클래스
     * </pre>
     * @author Sang-cheon Park
     * @version 1.0
     */
    static class ChannelManagement {
    	
    	static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
        
        /**
         * <pre>
         * 신규 채널을 등록한다.
         * </pre>
         * @param agentId
         * @param channel
         */
        static void registerChannel(String agentId, Channel channel) {
        	logger.debug("agentId({}) and channel({}) will be added to channelMap.", agentId, channel);
        	channelMap.put(agentId, channel);
        }//end of registerChannel()
        
        /**
         * <pre>
         * agentId에 해당하는 채널을 map에서 제거한다.
         * </pre>
         * @param agentId
         */
        static void deregisterChannel(String agentId) {
        	logger.debug("agentId({}) will be removed from channelMap.", agentId);
        	channelMap.remove(agentId);
        }//end of deregisterChannel()
        
        /**
         * <pre>
         * 연결 종료된 채널을 map에서 제거한다.
         * </pre>
         * @param channel
         */
        static void deregisterChannel(Channel channel) {
        	Iterator<Entry<String, Channel>> iter = channelMap.entrySet().iterator();
        	
        	Entry<String, Channel> entry = null;
        	while (iter.hasNext()) {
        		entry = iter.next();
        		
        		if (entry.getValue() != null && entry.getValue() == channel) {
        			deregisterChannel(entry.getKey());
        			break;
        		}
        	}
        }//end of deregisterChannel()
        
        /**
         * <pre>
         * agentId에 해당하는 채널 정보를 가져온다.
         * </pre>
         * @param agentId
         * @return
         */
        static Channel getChannel(String agentId) {
        	return channelMap.get(agentId);
        }//end of getChannel()
    }
    //end of ChannelManagement.java
    
	/**
     * <pre>
     * 서버의 처리 순서대로 받기 위한 콜백 클래스
     * </pre>
     * @author Sang-cheon Park
     * @version 1.0
	 */
	static class Callback {

		private final CountDownLatch latch = new CountDownLatch(1);

		private ProvisioningResponseMessage response;

		ProvisioningResponseMessage get() {
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return response;
		}

		void handle(ProvisioningResponseMessage response) {
			this.response = response;
			latch.countDown();
		}
	}
	//end of Callback.java
	
	/**
	 * <pre>
	 * Multi-thread 환경에서 Callback 객체를 관리하기 위한 클래스 
	 * </pre>
	 * @author Sang-cheon Park
	 * @version 1.0
	 */
	static class CallbackManagement {
		private static final Lock lock = new ReentrantLock();
		private static final Queue<Callback> callbacks = new ConcurrentLinkedQueue<Callback>();
		
		static void lock() {
			lock.lock();
		}
		
		static void unlock() {
			lock.unlock();
		}
		
		static void add(Callback callback) {
			callbacks.add(callback);
		}
		
		static Callback poll() {
			return callbacks.poll();
		}
		
		static int getSize() {
			return callbacks.size();
		}
	}
	//end of CallbackManagement.java
	
}
//end of PeacockServerHandler.java