/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config.channellisteners;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ChannelListener;

/**
 * @auth tangjianghua
 * @date 2020/8/21
 */
public class ChannelListener1 implements ChannelListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onCreate(Channel channel, boolean transactional) {
        //todo channel创建
        logger.debug("channel onCreate");
    }

    @Override
    public void onShutDown(ShutdownSignalException signal) {
        //todo channel关闭
        logger.debug("channel onShutDown");
    }
}
