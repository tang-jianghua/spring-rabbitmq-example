/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @auth tangjianghua
 * @date 2020/8/21
 */
@Configuration
public class AmqpAminConfiguration {


    /**
     * 使用连接工厂构建RabbitAdmin
     * @param cf
     * @return
     */
    @Bean
    public RabbitAdmin admin(@Qualifier("queueAffinityCF") ConnectionFactory cf) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cf);
        /**
         * 默认情况下，RabbitAdmin 会在有异常发生时直接停止所有的组件声明，这会导致一个队列的声明异常影响后续队列的声明。
         * 为了避免这种情况，可以将RabbitAdmin的属性ignore-declaration-exceptions设置为true，
         * 这个设置使得RabbitAdmin可以打印异常日志然后接着声明其他元素
         */
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        return rabbitAdmin;
    }

    /**
     * 注意如果要使用RabbitTemplate来构造RabbitAdmin，RabbitTemplate的UsePublisherConnection不能设置为true
     * @param rabbitTemplate
     * @return
     */
    @Bean("admin2")
    public RabbitAdmin admin2(@Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }
    @Bean
    public TopicExchange topicExchange(RabbitAdmin admin) {
        /**
         * 参数依次为交换机名称，是否持久化，是否自动删除
         */
        TopicExchange topicExchange = new TopicExchange("MY-TOPIC-EXCHANGE",true,false);
        topicExchange.setAdminsThatShouldDeclare(admin);
        //是否延迟
        topicExchange.setDelayed(true);
        //是否忽略属性匹配异常
        topicExchange.setIgnoreDeclarationExceptions(true);
        //是否自动声明，默认true
        topicExchange.setShouldDeclare(true);
        //1.6以后，可以通过rabbitAdmin将交换机设置一个internal属性（默认为false），
        // 当这个属性设置为true时，RabbitMq不会让客户端使用这个交换机，
        // 这在设置死信交换机或者exchange-to-exchange binding的时候非常有用，
        // 这样可以不用担心别人往这个交换机上发布消息。
        topicExchange.setInternal(false);
        return topicExchange;
    }

    @Bean
    public Queue q1(RabbitAdmin admin) {

        /**
         * 参数依次为
         * 队列名称，
         * 是否持久化，
         * 是否被声明的连接独占，
         * 是否自动删除,
         * 声明参数
         */
        Queue q1 = new Queue("q1", true, false, false,null);
        q1.setIgnoreDeclarationExceptions(true);
        q1.setShouldDeclare(true);
        q1.setAdminsThatShouldDeclare(admin);
        return q1;
    }

    @Bean
    public Binding b1(RabbitAdmin admin) {
        return BindingBuilder.bind(q1(admin)).to(topicExchange(admin)).with("routingKey");
    }

    @Bean
    public Declarables es() {
        return new Declarables(
                new DirectExchange("e2", false, true),
                new DirectExchange("e3", false, true));
    }

    @Bean
    public Declarables qs() {
        return new Declarables(
                new Queue("q2", false, false, true),
                new Queue("q3", false, false, true));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Declarables prototypes() {
        return new Declarables(new Queue("this.prototypeQueueName", false, false, true));
    }

    @Bean
    public Declarables bs() {
        return new Declarables(
                new Binding("q2", Binding.DestinationType.QUEUE, "e2", "k2", null),
                new Binding("q3", Binding.DestinationType.QUEUE, "e3", "k3", null));
    }

    @Bean
    public Declarables ds() {
        return new Declarables(
                new DirectExchange("e4", false, true),
                new Queue("q4", false, false, true),
                new Binding("q4", Binding.DestinationType.QUEUE, "e4", "k4", null));
    }

}
