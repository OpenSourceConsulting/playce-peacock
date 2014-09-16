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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.athena.peacock.agent.util.AgentConfigUtil;
import com.athena.peacock.common.constant.PeacockConstant;

/**
 * <pre>
 * Netty Client
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
@Component
@Qualifier("peacockClient")
public class PeacockClient {

    private final String[] hosts = AgentConfigUtil.getConfig(PeacockConstant.SERVER_IP).split(",");
    private final int port = Integer.parseInt(AgentConfigUtil.getConfig(PeacockConstant.SERVER_PORT));

    @Inject
    @Named("eventLoopGroup")
    private EventLoopGroup group;

    @Inject
    @Named("peacockClientInitializer")
    private PeacockClientInitializer initializer;
	
	@Inject
	@Named("peacockClientHandler")
	private PeacockClientHandler handler;
	
	/**
	 * <pre>
	 * Netty Server와 연결하기 위한 Bootstrap을 초기화 한다.
	 * </pre>
	 * @param bootstrap
	 * @param group
	 * @return
	 */
	public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup group, String host) {
		if (bootstrap != null) {
			bootstrap.group(group)
					 .channel(NioSocketChannel.class)
					 .option(ChannelOption.SO_KEEPALIVE, true)
					 .handler(new LoggingHandler(LogLevel.WARN))
			         .handler(initializer)
					 .remoteAddress(host, port)
					 .connect().addListener(new PeacockClientListener(this, host));
		}
		
		return bootstrap;
	}//end of createBootstrap()

    /**
     * <pre>
     * Bean 생성 시 수행되는 메소드로 Server와의 연결을 수립한다.
     * </pre>
     * @throws Exception
     */
    @PostConstruct
	public void start() {
    	for (String host : hosts) {
    		createBootstrap(new Bootstrap(), group, host);
    	}
	}//end of start()

	/**
	 * <pre>
	 * Bean 소멸 시 수행되는 메소드로 Server와의 연결을 종료한다.
	 * </pre>
	 */
	@PreDestroy
	public void stop() {
		if (handler.isConnected()) {
			handler.close();
		}
	}//end of stop()
	
}
//end of PeacockClient.java