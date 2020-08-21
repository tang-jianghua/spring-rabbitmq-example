/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config;

import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * 1.3以后，container 在启动期间会使用RabbitAdmin重新声明auto-startup=true的丢失的队列
 */
@Configuration
public class AmqpMessageListenerContainerConfiguration {

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(@Qualifier("queueAffinityCF") ConnectionFactory queueAffinityCF) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(queueAffinityCF);
        container.setQueueNames("some.queue");
        container.setMessageListener(exampleListener());
        container.setConsumerArguments(Collections.
                <String, Object>singletonMap("x-priority", Integer.valueOf(10)));

        /**
         *consumerBatchEnabled 设为true，需要实现{@link BatchMessageListener} 或者 {@link org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener}
         */
        container.setConsumerBatchEnabled(true);
        container.setMessageListener(new BatchMessageListener() {
            @Override
            public void onMessageBatch(List<Message> messages) {
                //todo
            }
        });
        container.setDeBatchingEnabled(true);
        return container;
    }

    @Bean
    public MessageListener exampleListener() {
        return new MessageListener() {
            public void onMessage(Message message) {
                System.out.println("received: " + message);
            }
        };
    }
}
