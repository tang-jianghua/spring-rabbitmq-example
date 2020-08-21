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
public class TopicListener {

    /**
     * listenerAdapter
     *
     * @param msg 消息内容,当只有一个参数的时候可以不加@Payload注解
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myTopicQueue-topic-#"),
            exchange = @Exchange(value = "myTopicExchange", type = ExchangeTypes.TOPIC),
            key = "topic.#"
    ))
    public void onMessage1(@Payload String msg) {
        System.out.println("myTopicQueue-topic-# 收到：" + msg);
    } /**
     * listenerAdapter
     *
     * @param msg 消息内容,当只有一个参数的时候可以不加@Payload注解
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myTopicQueue-topic-*"),
            exchange = @Exchange(value = "myTopicExchange", type = ExchangeTypes.TOPIC),
            key = "topic.*"
    ))
    public void onMessage4(@Payload String msg) {
        System.out.println("myTopicQueue-topic-* 收到：" + msg);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myTopicQueue-topic.a.#"),
            exchange = @Exchange(value = "myTopicExchange", type = ExchangeTypes.TOPIC),
            key = "topic.a.#"
    ))
    public void onMessage2(@Payload String msg) {
        System.out.println("myTopicQueue-topic.a.# 收到：" + msg);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myTopicQueue-#.a.#"),
            exchange = @Exchange(value = "myTopicExchange", type = ExchangeTypes.TOPIC),
            key = "#.a.#"
    ))
    public void onMessage3(@Payload String msg) {
        System.out.println("myTopicQueue-#.a.# 收到：" + msg);
    }
}
