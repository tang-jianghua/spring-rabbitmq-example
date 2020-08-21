/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example;

import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MyService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void service(String lookupKey, String payload) {
        //设置当前线程lookupKey，内部由ThreadLocal实现
        SimpleResourceHolder.bind(rabbitTemplate.getConnectionFactory(), lookupKey);
        //业务操作会根据线程中的lookupKey从routeConnectionFactory的targetConnectionFactories中选择对应的connectionFactory
        rabbitTemplate.convertAndSend(payload);
        //操作完以后记得解绑。不影响线程的后序其他工厂操作
        SimpleResourceHolder.unbind(rabbitTemplate.getConnectionFactory());
    }

}