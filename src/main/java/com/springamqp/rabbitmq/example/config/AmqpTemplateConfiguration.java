/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 *  AmqpTemplate配置
 * @auth tangjianghua
 * @date 2020/8/21
 */
@Configuration
public class AmqpTemplateConfiguration {

    @Bean("publishRabbitTemplate")
    public RabbitTemplate publishRabbitTemplate(@Qualifier("queueAffinityCF") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        //发布时可以使用第二个连接工厂发布，这样即使阻塞也不会影响到其他consumers
        rabbitTemplate.setUsePublisherConnection(true);
        //channel支持事务提交，在txCommit()时会检测异常。但是事务会降低性能，开启前三思。
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //在1.3版本提供了重试策略
        //1.4以后可以通过RetryTemplate.execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback).
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        //发布return模式，需要connectionFactory.setPublisherReturns(true)
        rabbitTemplate.setReturnCallback(new ReturnsCallBack());
        //发布Confirm模式，需要connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED)
        rabbitTemplate.setConfirmCallback(new ConfirmCallBack1());
        rabbitTemplate.setRetryTemplate(retryTemplate);
        return rabbitTemplate;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("queueAffinityCF") ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        return rabbitTemplate;
    }

    class ReturnsCallBack implements RabbitTemplate.ReturnCallback{
        private Logger logger = LoggerFactory.getLogger(this.getClass());
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            //todo
            logger.debug("recive returnedMessage");
        }
    }

    class ConfirmCallBack1 implements RabbitTemplate.ConfirmCallback{
        /**
         *
         * @param correlationData
         * @param ack
         *          --true ack
         *          --false nack
         * @param cause nack的原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            //todo
        }
    }
}
