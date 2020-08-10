package com.springamqp.rabbitmq.example.exchange.producer;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auth tangjianghua
 * @date 2020/8/10
 */
@Component
public class MyProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private void sendDirect1() {
        rabbitTemplate.convertAndSend("myDirectExchange", "routing.key.direct.one", "来自myDirectExchange的消息");
    }

    private void sendDirect2() {
        rabbitTemplate.convertAndSend("myDirectExchange", "routing.key.direct.two", "来自myDirectExchange的消息");
    }

    private void sendfanout1() {
        rabbitTemplate.convertAndSend("myFanoutExchange", "routing.key.fanout.one", "来自myFanoutExchange的消息");
    }

    private void sendfanout2() {
        rabbitTemplate.convertAndSend("myFanoutExchange", "routing.key.fanout.two", "来自myFanoutExchange的消息");
    }

    private void sendtopica() {
        rabbitTemplate.convertAndSend("myTopicExchange", "topic.a", "来自myTopicExchange的消息");
    }

    private void sendtopicaa() {
        rabbitTemplate.convertAndSend("myTopicExchange", "topic.a.a", "来自myTopicExchange的消息");
    }

    private void sendtopicaaa() {
        rabbitTemplate.convertAndSend("myTopicExchange", "a.a.a", "来自myTopicExchange的消息");
    }

    private void sendheaderMatchAny() {

        rabbitTemplate.convertAndSend("myHeadExchange", "headQueue-one", "来自myHeadExchange的消息", message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("key-one", "1");
            return message;
        });
    }

    private void sendheaderMatchAll() {

        rabbitTemplate.convertAndSend("myHeadExchange", "headQueue-two", "来自myHeadExchange的消息", message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("key-one", "1");
            properties.setHeader("key-one", "2");
            return message;
        });
    }

}
