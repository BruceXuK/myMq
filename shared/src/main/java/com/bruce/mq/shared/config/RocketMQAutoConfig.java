package com.bruce.mq.shared.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * RocketMQ自动配置类
 * 提供延迟加载和重试机制，解决启动时连接不稳定的问题
 */
@Configuration
@Import(RocketMQAutoConfiguration.class)
@ConditionalOnProperty(name = "rocketmq.enable", havingValue = "true", matchIfMissing = true)
public class RocketMQAutoConfig {
    
}