/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.exchange.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Annotation-driven Listener Endpoints
 * @auth tangjianghua
 * @date 2020/8/10
 */
@Component
public class FanoutListener {

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("myFanoutQueue-one"),
                    exchange = @Exchange(value = "myFanoutExchange", type = ExchangeTypes.FANOUT),
            key = "routing.key.fanout.one"))
    public void onMessage1(@Payload String msg, @Headers Map<String, Object> headers) {
        System.out.println("myFanoutQueue-one 收到：" + msg);
    }

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("myFanoutQueue-two"),
                    exchange = @Exchange(value = "myFanoutExchange", type = ExchangeTypes.FANOUT),
                    key = "routing.key.fanout.two"))
    public void onMessage2(@Payload String msg, @Headers Map<String, Object> headers) {
        System.out.println("myFanoutQueue-two 收到：" + msg);
    }
}
