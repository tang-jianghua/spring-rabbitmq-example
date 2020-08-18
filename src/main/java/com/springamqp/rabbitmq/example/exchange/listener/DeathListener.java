package com.springamqp.rabbitmq.example.exchange.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Annotation-driven Listener Endpoints
 *
 * @auth tangjianghua
 * @date 2020/8/10
 */
@Component
public class DeathListener {

    /*@RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "myTopicQueue-delay", durable = "true", autoDelete = "false",
                    arguments = {@Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer"),
                            @Argument(name = "x-dead-letter-exchange", value = "BDC-DEAD-TOPIC-EXCHANEGE"),
                            //@Argument(name = "x-dead-letter-routing-key", value = "dle.app1")
                    }),
            exchange = @Exchange(value = "BDC-TOPIC-EXCHANEGE", type = ExchangeTypes.TOPIC),
            key = "BDC.MERCHANT.001")
    )
    @RabbitHandler
    public void delayHandler(@Payload String payload) {

        System.out.println("myTopicQueue-delay收到："+payload);
    }*/
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "myTopicQueue-delay", durable = "true", autoDelete = "false",
                    arguments = {@Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer"),
                            @Argument(name = "x-dead-letter-exchange", value = "BDC-DEAD-TOPIC-EXCHANEGE"),
                            //@Argument(name = "x-dead-letter-routing-key", value = "dle.app1")
                    }),
            exchange = @Exchange(value = "BDC-TOPIC-EXCHANEGE", type = ExchangeTypes.TOPIC),
            key = "BDC.ORG.001")
    )
    @RabbitHandler
    public void delayHandler(@Payload String payload) {

        System.out.println("myTopicQueue-delay收到："+payload);
    }
  /*  *//**
     * 死信处理
     *//*
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("dle-queue"),
            exchange = @Exchange(value = "myDeadLetterExchange", type = ExchangeTypes.TOPIC),
            key = "dle.#"
    ))
    public void dleHandler(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        String receivedRoutingKey = messageProperties.getReceivedRoutingKey();
        System.out.println("死信前："+receivedRoutingKey);

        System.out.println("消息体："+new String(message.getBody()));

    }*/
  /*  @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "myTopicQueue-delay", durable = "true", autoDelete = "false",
                    arguments = {@Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer"),
                            @Argument(name = "x-dead-letter-exchange", value = "myDlE"),
                            @Argument(name = "x-dead-letter-routing-key", value = "deadQueue")
                    }),
            exchange = @Exchange(value = "myHeadersExchange", type = ExchangeTypes.HEADERS),
            key = "b",
            arguments = {
                    @Argument(name = "x-match", value = "all"),
                    @Argument(name = "thing1", value = "somevalue"),
                    @Argument(name = "thing2")
            })
    )
    @RabbitHandler
    public void dleHandler(@Payload String payload, @) {

    }*/
}
