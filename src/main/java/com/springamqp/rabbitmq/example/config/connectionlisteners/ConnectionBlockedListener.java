/*
 * Copyright (c) 2020. tangjianghua All rights reserved..
 */

package com.springamqp.rabbitmq.example.config.connectionlisteners;

import com.rabbitmq.client.BlockedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 当应用程序配置为单个CachingConnectionFactory时（springboot默认自动配置），
 * 一旦连接在broker发生阻塞，应用程序会停止工作。当这种情况发生时，所有client都会停止工作。
 * 如果生产者和消费者在同一个应用中，当一个生产者被broker阻塞，而消费者又无法释放生产者，会导致死锁。
 *
 * 为了缓解这个问题，建议再创建一个具有相同的配置的单独的CachingConnectionFactory实例，
 * 一个用于生产者，一个用于消费者。这样，即使生产者链接发生阻塞，不会影响消费者的链接。
 *
 * 2.0以后可以为Connection提供一个BlockedListener，使用观察者模式监听Connection的阻塞和非阻塞事件
 * @auth tangjianghua
 * @date 2020/8/21
 */
@Component
public class ConnectionBlockedListener implements BlockedListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 阻塞事件处理
     * @param reason
     * @throws IOException
     */
    @Override
    public void handleBlocked(String reason) throws IOException {
        logger.warn("connection is blocked for reason:{}",reason);
        //todo

    }

    /**
     * 如果处理解开阻塞
     * @throws IOException
     */
    @Override
    public void handleUnblocked() throws IOException {
        logger.warn("here is how to handleUnblocked");
        //todo handleUnblocked
    }
}
