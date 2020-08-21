/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config.connectionlisteners;

import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.stereotype.Component;

/**
 * @auth tangjianghua
 * @date 2020/8/21
 */
@Component
public class ConnectionListener1 implements ConnectionListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void onCreate(Connection connection) {
        //todo 连接创建
        logger.debug("connection onCreate");
    }

    @Override
    public void onClose(Connection connection) {
        //todo 连接关闭
        logger.debug("connection onClose");
    }

    @Override
    public void onShutDown(ShutdownSignalException signal) {
        //todo 连接被强制关闭
        logger.debug("connection onShutDown");
    }
}
