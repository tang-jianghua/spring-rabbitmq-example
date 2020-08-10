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

public class HeadersListener {

    /**
     * listenerAdapter
     *
     * @param msg 消息内容,当只有一个参数的时候可以不加@Payload注解
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("headQueue-one"),
            exchange = @Exchange(value = "myHeadExchange", type = ExchangeTypes.HEADERS),
            arguments = {
                    @Argument(name = "key-one", value = "1"),
                    @Argument(name = "key-two", value = "2"),
                    @Argument(name = "x-match", value = "any")
            },
            key = "headQueue-one"))
    public void matchAny(@Payload String msg) {
        System.out.println("headQueue-one 收到：" + msg);
    }

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("headQueue-two"),
            exchange = @Exchange(value = "myHeadExchange", type = ExchangeTypes.HEADERS),
            arguments = {
                    @Argument(name = "key-one", value = "1"),
                    @Argument(name = "key-two", value = "2"),
                    @Argument(name = "x-match", value = "all")
            },
            key = "headQueue-two"))
    public void matchAll(@Payload String msg) {
        System.out.println("headQueue-two 收到：" + msg);
    }
}
