/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config;

import com.rabbitmq.client.Channel;
import com.springamqp.rabbitmq.example.config.channellisteners.ChannelListener1;
import com.springamqp.rabbitmq.example.config.connectionlisteners.ConnectionListener1;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * AMQP 连接资源控制层的配置
 * @auth tangjianghua
 * @date 2020/8/21
 */
@Configuration
public class AmqpConnectionAndResourceManagementConfiguration {

    /**
     * ConnectionFactory是控制broker连接的核心组件
     * CachingConnectionFactory是ConnectionFactory唯一的具体实现(SingleConnectionFactory在测试单元使用)，
     * 默认只创建单个连接被应用共享（CHANNEL模式），某种意义上来说Connection和Channel的关系类似于JMS中的Connection与Session的关系
     * 如果想实现自己的ConnectionFactory，看这里：{@link org.springframework.amqp.rabbit.connection.AbstractConnectionFactory}
     * @return
     */
    @Bean("rabbitConnectionFactory1")
    public ConnectionFactory rabbitConnectionFactory1() {
        //设置broker地址和账号
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory();
        connectionFactory.setAddresses("host1:5672,host2:5672");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //ShuffleAddresses为true设置随机访问集群，不设置的话会按照顺序访问
        connectionFactory.setShuffleAddresses(true);

        //设置缓存模式 可以设置CHANNEL（默认） 或者CONNECTION
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        //connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);

        //设置CHANNEL最大缓存数量，在CONNECTION模式下也会生效，1.6以后默认25，
        //过小会导致高频的created and closed，导致性能下降，可以在RabbitMQ Admin UI上进行监控。
        connectionFactory.setChannelCacheSize(25);
        //设置CONNECTION最大缓存数量，默认Runtime.getRuntime().availableProcessors() * 2
        connectionFactory.setConnectionCacheSize(Runtime.getRuntime().availableProcessors() * 2);
        //在CONNECTION模式下，如果需要大量的连接，最好给connectionFactory设置一个线程池，这样同一个executor可以被
        //所有的连接和connectionFactory线程共享。官方推荐线程池要么不要设置size上限（个人不推荐），要么至少一个连接对应一个线程（至少和连接数相等），
        //这样可以达到最大效率。但是实际情况要根据CPU核数来定，不是连接越多越好。
        connectionFactory.setExecutor(connectionFactoryThreadPoolExecutor());

        //1.4.2以后新增属性，当channelCheckoutTimeout大于0时，ChannelCacheSize不再代表CHANNEL最大缓存数量，
        //而代表的是一个连接上能创建的最大CHANNEL数。当申请的CHANNEL超过这个ChannelCacheSize时，会阻塞channelCheckoutTimeout时间，
        //如果在阻塞期间没有新的可用的CHANNEL,就会抛出AmqpTimeoutException
        connectionFactory.setChannelCheckoutTimeout(0);

        //可以通过这种方式创建连接，这里只做演示，并不是connectionFactory的配置
        //Connection connection = connectionFactory.createConnection();
        //可以通过这种方式创建CHANNEL，这里只做演示，并不是connectionFactory的配置
        //Channel channel = connection.createChannel(true);

        //设置AMQPVirtualHost
        connectionFactory.setVirtualHost("/");

        //设置连接名称策略，便于区分连接，在management UI中可以表现出来
        connectionFactory.setConnectionNameStrategy(cf -> "MY_CONNECTION");
        //或者使用SimplePropertyValueConnectionNameStrategy
        //connectionFactory.setConnectionNameStrategy(new SimplePropertyValueConnectionNameStrategy("MY_CONNECTION"));

        /**
         * 当设置位发布确认模式和发布返回模式以后，{@link Channel}会被封装成为{@link PublisherCallbackChannel}，这样可以
         * 有效促进回调。client端可以为PublisherCallbackChannel注册一个{@link PublisherCallbackChannel.Listener}来监听回调。
         */
        //设置为发布确认模式
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        //设置发布返回模式
        connectionFactory.setPublisherReturns(true);

        //添加连接生命周期监听器
        connectionFactory.addConnectionListener(new ConnectionListener1());
        //添加channel生命周期监听器
        connectionFactory.addChannelListener(new ChannelListener1());

        //可以根据异常类型自定义channel关闭日志打印级别
        connectionFactory.setCloseExceptionLogger((logger, message, t) -> {
            logger.error(message,t);
        });

        return connectionFactory;
    }
    @Bean
    public ConnectionFactory rabbitConnectionFactory2() {
        //设置broker地址和账号
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory();
        connectionFactory.setAddresses("host1:5672,host2:5672");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //ShuffleAddresses为true设置随机访问集群，不设置的话会按照顺序访问
        connectionFactory.setShuffleAddresses(true);

        //设置缓存模式 可以设置CHANNEL（默认） 或者CONNECTION
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        //connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);

        //设置CHANNEL最大缓存数量，在CONNECTION模式下也会生效，1.6以后默认25，
        //过小会导致高频的created and closed，导致性能下降，可以在RabbitMQ Admin UI上进行监控。
        connectionFactory.setChannelCacheSize(25);
        //设置CONNECTION最大缓存数量，默认Runtime.getRuntime().availableProcessors() * 2
        connectionFactory.setConnectionCacheSize(Runtime.getRuntime().availableProcessors() * 2);
        //在CONNECTION模式下，如果需要大量的连接，最好给connectionFactory设置一个线程池，这样同一个executor可以被
        //所有的连接和connectionFactory线程共享。官方推荐线程池要么不要设置size上限（个人不推荐），要么至少一个连接对应一个线程（至少和连接数相等），
        //这样可以达到最大效率。但是实际情况要根据CPU核数来定，不是连接越多越好。
        connectionFactory.setExecutor(connectionFactoryThreadPoolExecutor());

        //1.4.2以后新增属性，当channelCheckoutTimeout大于0时，ChannelCacheSize不再代表CHANNEL最大缓存数量，
        //而代表的是一个连接上能创建的最大CHANNEL数。当申请的CHANNEL超过这个ChannelCacheSize时，会阻塞channelCheckoutTimeout时间，
        //如果在阻塞期间没有新的可用的CHANNEL,就会抛出AmqpTimeoutException
        connectionFactory.setChannelCheckoutTimeout(0);

        //可以通过这种方式创建连接，这里只做演示，并不是connectionFactory的配置
        //Connection connection = connectionFactory.createConnection();
        //可以通过这种方式创建CHANNEL，这里只做演示，并不是connectionFactory的配置
        //Channel channel = connection.createChannel(true);

        //设置AMQPVirtualHost
        connectionFactory.setVirtualHost("/");

        //设置连接名称策略，便于区分连接，在management UI中可以表现出来
        connectionFactory.setConnectionNameStrategy(cf -> "MY_CONNECTION");
        //或者使用SimplePropertyValueConnectionNameStrategy
        //connectionFactory.setConnectionNameStrategy(new SimplePropertyValueConnectionNameStrategy("MY_CONNECTION"));


        /**
         * 当设置为发布确认模式和发布返回模式以后，{@link Channel}会被封装成为{@link PublisherCallbackChannel}，这样可以
         * 有效促进回调。client端可以为PublisherCallbackChannel注册一个{@link PublisherCallbackChannel.Listener}来监听回调。
         */
        //设置为发布确认模式
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        //设置发布返回模式
        connectionFactory.setPublisherReturns(true);

        //添加连接生命周期监听器
        connectionFactory.addConnectionListener(new ConnectionListener1());
        //添加channel生命周期监听器
        connectionFactory.addChannelListener(new ChannelListener1());

        //可以根据异常类型自定义channel关闭日志打印级别
        connectionFactory.setCloseExceptionLogger((logger, message, t) -> {
            logger.error(message,t);
        });
        return connectionFactory;
    }

    /**
     * Routing Connection Factory
     * 1.3以后添加了{@link AbstractRoutingConnectionFactory}，路由连接工厂，
     * 他提供了一种将connectionFactory和lookupKey建立映射关系的机制，这种映射关系是基于线程绑定的，
     * Spring AMQP提供了一个简单的实现类{@link SimpleRoutingConnectionFactory}，可以通过在当前线程
     * 中使用SimpleResourceHolder将lookupKey和connectionFactory绑定起来，然后执行业务操作，
     * 业务操作完以后要记得解绑。
     *
     * @return
     */
    @Bean
    public ConnectionFactory routeConnectionFactory(){
        SimpleRoutingConnectionFactory simpleRoutingConnectionFactory = new SimpleRoutingConnectionFactory();

        Map<Object, ConnectionFactory> targetConnectionFactories = new HashMap<>();
        targetConnectionFactories.put("lookupKey1",rabbitConnectionFactory1());
        targetConnectionFactories.put("lookupKey2",rabbitConnectionFactory2());
        simpleRoutingConnectionFactory.setTargetConnectionFactories(targetConnectionFactories);
        return simpleRoutingConnectionFactory;
    }

    /**
     * 本地连接工厂，HA集群下建议使用该配置
     * 注意三个参数
     * rabbitmq集群地址address，集群admin地址,nodes
     * address和nodes必须一一对应，当一个容器尝试连接一个队列时，会先使用admin API来决定哪个node的队列是master，然后连接到和node对应的address。
     * @param defaultCF
     * @return
     */
    @Bean
    public ConnectionFactory queueAffinityCF(
            @Qualifier("rabbitConnectionFactory1") ConnectionFactory defaultCF) {
        return new LocalizedQueueConnectionFactory(
                //备用连接工厂
                defaultCF,
                //rabbitmq集群地址
                StringUtils.commaDelimitedListToStringArray("host1:5672,host2:5672"),
                //集群admin地址
                StringUtils.commaDelimitedListToStringArray("https://host1:15672,host2:15672"),
                //nodes,和rabbitmq集群地址一一对应的节点名称,注意必须一一对应，长度一致
                StringUtils.commaDelimitedListToStringArray("rabbit@server1,rabbit@server2"),
                "/",
                "admin",
                "admin",
                false,
                null);
    }
    /**
     * 自定义连接工厂线程池
     * @return
     */
    public ThreadPoolExecutor connectionFactoryThreadPoolExecutor() {
        return new ThreadPoolExecutor(2,
                Runtime.getRuntime().availableProcessors() * 2,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100),
                new CustomizableThreadFactory("rabbitmq发布消息线程池"),
                new ThreadPoolExecutor.AbortPolicy());
    }


}
