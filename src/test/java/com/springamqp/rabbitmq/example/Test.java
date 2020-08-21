/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example;

import org.junit.runner.RunWith;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @auth tangjianghua
 * @date 2020/8/10
 */
@SpringBootTest(classes = ExampleApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class Test {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @org.junit.Test
    public void sendDirect1() {
        rabbitTemplate.convertAndSend("myDirectExchange", "routing.key.direct.one", "来自myDirectExchange的消息");
    }
    @org.junit.Test
    public void sendDirect2() {
        rabbitTemplate.convertAndSend("myDirectExchange", "routing.key.direct.two", "来自myDirectExchange的消息");
    }

    @org.junit.Test
    public void sendfanout1() {
        rabbitTemplate.convertAndSend("myFanoutExchange", "routing.key.fanout.one", "来自myFanoutExchange的消息");
    }
    @org.junit.Test
    public void sendfanout2() {
        rabbitTemplate.convertAndSend("myFanoutExchange", "routing.key.fanout.two", "来自myFanoutExchange的消息");
    }
    @org.junit.Test
    public void sendtopica() {
        rabbitTemplate.convertAndSend("myTopicExchange", "topic.a", "来自myTopicExchange的消息topic.a");
    }
    @org.junit.Test
    public void sendtopicaa() {
        rabbitTemplate.convertAndSend("myTopicExchange", "topic.a.a", "来自myTopicExchange的消息topic.a.a");
    }
    @org.junit.Test
    public void sendtopicaaa() {
        rabbitTemplate.convertAndSend("myTopicExchange", "a.a.a", "来自myTopicExchange的消息a.a.a");
    }
    @org.junit.Test
    public void sendtopicDelay() {
        rabbitTemplate.convertAndSend("myTopicExchange", "dle.app1", "来自myTopicExchange的消息a.a.a",message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("times", "1");
            return message;
        });
    }
    @org.junit.Test
    public void sendheaderMatchAny() {

        rabbitTemplate.convertAndSend("myHeadExchange", "headQueue-one", "来自myHeadExchange的消息", message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("key-one", "1");
            return message;
        });
    }
    @org.junit.Test
    public void sendheaderMatchAll() {

        rabbitTemplate.convertAndSend("myHeadExchange", "headQueue-two", "来自myHeadExchange的消息", message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("key-one", "1");
            properties.setHeader("key-two", "2");
            return message;
        });
    }
}
