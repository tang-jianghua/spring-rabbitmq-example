/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.exchange.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Annotation-driven Listener Endpoints
 * @auth tangjianghua
 * @date 2020/8/10
 */
@Component
public class DirectListener {

    /**
     * listenerAdapter
     *
     * @param msg 消息内容,当只有一个参数的时候可以不加@Payload注解
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myDirectQueue-one"),
            exchange = @Exchange(value = "myDirectExchange", type = ExchangeTypes.DIRECT),
            key = "routing.key.direct.one"
    ))
    public void onMessage(@Payload String msg) {
        System.out.println("myDirectQueue-one收到:"+msg);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myDirectQueue-two"),
            exchange = @Exchange(value = "myDirectExchange", type = ExchangeTypes.DIRECT),
            key = "routing.key.direct.two"
    ))
    public void onMessage2(@Payload String msg) {
        System.out.println("myDirectQueue-two收到:"+msg);
    }
}
