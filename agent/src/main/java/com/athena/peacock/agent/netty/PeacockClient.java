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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PeacockClient.class);

    private final String host = AgentConfigUtil.getConfig(PeacockConstant.SERVER_IP);
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
    
    private final int frequency = 5000;
    private final int maxretries = 5;
    private final AtomicInteger count = new AtomicInteger();
    
    /**
     * <pre>
     * Bean 생성 시 수행되는 메소드로 Server와의 연결을 수립한다.
     * </pre>
     * @throws Exception
     */
    @PostConstruct
	public void start() throws Exception {
    	final Bootstrap b = new Bootstrap()
    							.group(group)
						        .channel(NioSocketChannel.class)
						        .handler(new LoggingHandler(LogLevel.WARN))
						        .handler(initializer);
        
        // Start the connection attempt.
        final ChannelFuture connectFuture = b.connect(host, port);
        
        connectFuture.addListener(new ChannelFutureListener() {
        	
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
                    future.sync();
				} else {
					if (count.incrementAndGet() <= maxretries) {
                        logger.debug("Attempt to reconnect within {} seconds.", frequency / 1000);
                        
						try {
                            Thread.sleep(frequency);
                        } catch (InterruptedException e) {
                        	// nothing to do.
                            logger.error(e.getMessage());
                        }   
                        
                        b.connect(host, port).addListener(this);       
					} else {
						// Stop the agent daemon if the connection attempt has failed.
						System.exit(-1);
					}
				}
			}
        });
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