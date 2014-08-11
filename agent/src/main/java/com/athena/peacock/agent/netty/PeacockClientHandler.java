/*
 * Copyright 2013 The Athena-Peacock Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2013. 7. 17.		First Draft.
 */
package com.athena.peacock.agent.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.Commandline;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.agent.util.MacAddressUtil;
import com.athena.peacock.agent.util.PropertyUtil;
import com.athena.peacock.agent.util.SigarUtil;
import com.athena.peacock.common.constant.PeacockConstant;
import com.athena.peacock.common.netty.PeacockDatagram;
import com.athena.peacock.common.netty.message.AgentInitialInfoMessage;
import com.athena.peacock.common.netty.message.MessageType;
import com.athena.peacock.common.netty.message.OSPackageInfoMessage;
import com.athena.peacock.common.netty.message.PackageInfo;
import com.athena.peacock.common.netty.message.ProvisioningCommandMessage;
import com.athena.peacock.common.netty.message.ProvisioningResponseMessage;
import com.athena.peacock.common.provider.AppContext;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("peacockClientHandler")
@Sharable
public class PeacockClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(PeacockClientHandler.class);

    private boolean connected = false;
    private Channel channel;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelActive() has invoked.");
		
    	connected = true;
    	channel = ctx.channel();
		ctx.writeAndFlush(getAgentInitialInfo());
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("channelRead0() has invoked.");

		logger.debug("[Client] Object => " + msg.getClass().getName());
		logger.debug("[Client] Contents => " + msg.toString());
		
		if(msg instanceof PeacockDatagram) {
			MessageType messageType = ((PeacockDatagram<?>)msg).getMessageType();
			
			if (messageType.equals(MessageType.COMMAND)) {
				ProvisioningResponseMessage response = new ProvisioningResponseMessage();
				response.setAgentId(((PeacockDatagram<ProvisioningCommandMessage>)msg).getMessage().getAgentId());
				response.setBlocking(((PeacockDatagram<ProvisioningCommandMessage>)msg).getMessage().isBlocking());
				
				((PeacockDatagram<ProvisioningCommandMessage>)msg).getMessage().executeCommands(response);
				
				ctx.writeAndFlush(new PeacockDatagram<ProvisioningResponseMessage>(response));
			} else if (messageType.equals(MessageType.PACKAGE_INFO)) {
				ctx.writeAndFlush("Start OS Package collecting...");

				String packageFile = null;
				
				try {
					packageFile = PropertyUtil.getProperty(PeacockConstant.PACKAGE_FILE_KEY);
				} catch (Exception e) {
					// nothing to do.
				} finally {
					if (StringUtils.isEmpty(packageFile)) {
						packageFile = "/peacock/agent/config/package.log";
					}
				}
				
				new PackageGatherThread(ctx, packageFile).start();
			} else if (messageType.equals(MessageType.INITIAL_INFO)) {
				String machineId = ((PeacockDatagram<AgentInitialInfoMessage>)msg).getMessage().getAgentId();
				
				String agentFile = null;
				String agentId = null;
				
				try {
					agentFile = PropertyUtil.getProperty(PeacockConstant.AGENT_ID_FILE_KEY);
				} catch (Exception e) {
					// nothing to do.
				} finally {
					if (StringUtils.isEmpty(agentFile)) {
						agentFile = "/peacock/agent/.agent";
					}
				}
				
				File file = new File(agentFile);
				boolean isNew = false;
				
				try {
					agentId = IOUtils.toString(file.toURI());
					
					if(!agentId.equals(machineId)) {
						isNew = true;
					}
				} catch (IOException e) {
		            logger.error(agentFile + " file cannot read or saved invalid agent ID.", e);
				}
				
				if(isNew) {
		            logger.info("New Agent-ID({}) will be saved.", machineId);
		            
		            try {
		    			file.setWritable(true);
						OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(file));
						output.write(machineId);
						file.setReadOnly();
						IOUtils.closeQuietly(output);
					} catch (UnsupportedEncodingException e) {
						logger.error("UnsupportedEncodingException has occurred : ", e);
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException has occurred : ", e);
					} catch (IOException e) {
						logger.error("IOException has occurred : ", e);
					}
				}
				
				// 패키지 정보 수집 이력이 없을 경우 수행
				String packageFile = null;
				
				try {
					packageFile = PropertyUtil.getProperty(PeacockConstant.PACKAGE_FILE_KEY);
				} catch (Exception e) {
					// nothing to do.
				} finally {
					if (StringUtils.isEmpty(packageFile)) {
						packageFile = "/peacock/agent/config/package.log";
					}
				}
				
				file = new File(packageFile);
				
				if(!file.exists()) {
					new PackageGatherThread(ctx, packageFile).start();
				}
				
				Scheduler scheduler = (Scheduler)AppContext.getBean("quartzJobScheduler");
				
				if (!scheduler.isStarted()) {
					scheduler.start();
				}
			}
		}
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }
    
    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive() has invoked.");

		// Stop the agent daemon if the connection has lost.
		System.exit(-1);
		
		/*
    	connected = false;
    	channel = null;
    	*/
    }
    
    /**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @return the channel
	 */
	private Channel getChannel() {
		return channel;
	}

	/**
	 * @return the channel
	 */
	public void close() {
		getChannel().close();
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param datagram
	 */
	public void sendMessage(PeacockDatagram<?> datagram) {
		getChannel().writeAndFlush(datagram);
	}//end of sendMessage()

	/**
     * <pre>
     * Agent의 시스템 정보를 조회한다.
     * </pre>
     * @return
	 * @throws Exception 
     */
    private PeacockDatagram<AgentInitialInfoMessage> getAgentInitialInfo() throws Exception {
    	String agentId = IOUtils.toString(new File(PropertyUtil.getProperty(PeacockConstant.AGENT_ID_FILE_KEY)).toURI());
		
		AgentInitialInfoMessage message = new AgentInitialInfoMessage();
		message.setMacAddr(MacAddressUtil.getMacAddressList().get(0));
		message.setAgentId(agentId);
		message.setOsName(System.getProperty("os.name"));
		message.setOsArch(System.getProperty("os.arch"));
		message.setOsVersion(System.getProperty("os.version"));
		message.setCpuClock(SigarUtil.getCpuClock());
		message.setCpuNum(SigarUtil.getCpuNum());
		message.setCpuModel(SigarUtil.getCpuModel());
		message.setCpuVendor(SigarUtil.getCpuVendor());
		message.setMemSize(SigarUtil.getMemSize());
		message.setHostName(SigarUtil.getNetInfo().getHostName());
		//message.setHostName(InetAddress.getLocalHost().getHostName());
		
		try {
			// 
			message.setIpAddr(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			// ignore
			logger.info("[{}] has occurred but ignore this exception.", e.getMessage());
		}
		
		return new PeacockDatagram<AgentInitialInfoMessage>(message);
    }//end of getAgentInitialInfo()
}
//end of PeacockClientHandler.java

/**
 * <pre>
 * Agent의 패키지 정보를 수집하는 스레드
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
class PackageGatherThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PackageGatherThread.class);
	
	private ChannelHandlerContext ctx;
	private String packageFile;
	
	public PackageGatherThread(ChannelHandlerContext ctx, String packageFile) {
		this.ctx = ctx;
		this.packageFile = packageFile;
	}
	
	@Override
	public void run() {
		try {
			// rpm -qa 로 전체 패키지 조회
			Commandline commandLine = new Commandline();
			commandLine.setExecutable("rpm");
			commandLine.createArg().setValue("-qa");

			StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();
			
			logger.debug("Start Package(rpm) info gathering...");
			logger.debug("~]$ {}\n", commandLine.toString());

			int returnCode = CommandLineUtils.executeCommandLine(commandLine, consumer, consumer, Integer.MAX_VALUE);
			
			if (returnCode == 0) {
				String[] rpms = consumer.getOutput().split("\n");

				OSPackageInfoMessage msg = new OSPackageInfoMessage(MessageType.PACKAGE_INFO);
				msg.setAgentId(IOUtils.toString(new File(PropertyUtil.getProperty(PeacockConstant.AGENT_ID_FILE_KEY)).toURI()));
				PackageInfo packageInfo = null;
				for (String rpm : rpms) {
					try {
						// rpm -q --qf "%{NAME}\n%{ARCH}\n%{SIZE}\n%{VERSION}\n%{RELEASE}\n%{INSTALLTIME:date}\n%{SUMMARY}\n%{DESCRIPTION}" ${PACKAGE_NAME} 으로 각 패키지 세부사항 조회
						packageInfo = getPackageInfo(rpm);
						
						if (packageInfo != null) {
							msg.addPackageInfo(packageInfo);
						}
					} catch (Exception e) {
						logger.error("Unhandled Exception has occurred. ", e);
					}
				}
				
				StringBuilder sb = new StringBuilder("CurrenDate : ") .append(new Date()) .append(", RPM Count : ") .append(rpms.length).append("\r\n");
				
				FileWriter fw = new FileWriter(packageFile, true);
				fw.write(sb.toString());
				fw.flush();
				
				IOUtils.closeQuietly(fw);
				
				if (msg.getPackageInfoList().size() > 0) {
					ctx.writeAndFlush(new PeacockDatagram<OSPackageInfoMessage>(msg));
				}
				
				logger.debug("End Package(rpm) info gathering...");
			} else {
				// when command execute failed.
				// especially command not found.
				logger.debug("End Package(rpm) info gathering with error(command execute failed)...");
			}
		} catch (Exception e) {
			logger.error("Unhandled Exception has occurred. ", e);
		}
	}
	
	private PackageInfo getPackageInfo(String rpm) throws CommandLineException {
		Commandline commandLine = new Commandline();
		commandLine.setExecutable("rpm");
		commandLine.createArg().setValue("-q");
		commandLine.createArg().setValue("--qf");
		commandLine.createArg().setValue("%{NAME}\n%{ARCH}\n%{SIZE}\n%{VERSION}\n%{RELEASE}\n%{INSTALLTIME}\n%{SUMMARY}\n%{DESCRIPTION}");
		commandLine.createArg().setValue(rpm);
		
		//logger.debug("~]$ {}\n", commandLine.toString());

		StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();
		int returnCode = CommandLineUtils.executeCommandLine(commandLine, consumer, consumer, Integer.MAX_VALUE);
		
		if (returnCode == 0) {
			PackageInfo packageInfo = new PackageInfo();
			
			String result = consumer.getOutput();
			int start = 0, end = 0;
			
			start = 0;
			end = result.indexOf("\n", start);
			packageInfo.setName(result.substring(start, end));
			
			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setArch(result.substring(start, end));
			
			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setSize(result.substring(start, end));
			
			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setVersion(result.substring(start, end));
			
			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setRelease(result.substring(start, end));
			
			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setInstallDate(new Date(Long.parseLong(result.substring(start, end)) * 1000));
			
			start = end + 1;
			end = result.indexOf("\n", start);
			packageInfo.setSummary(result.substring(start, end));
			
			start = end + 1;
			packageInfo.setDescription(result.substring(start));
			
			return packageInfo;
		} else {
			return null;
		}
	}
}