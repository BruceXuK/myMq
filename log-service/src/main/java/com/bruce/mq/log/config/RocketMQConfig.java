package com.bruce.mq.log.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * RocketMQ配置类
 * 提供延迟加载能力，只有当配置启用时才会加载RocketMQ相关组件
 */
@Configuration
@Import(RocketMQAutoConfiguration.class)
@ConditionalOnProperty(name = "rocketmq.enable", havingValue = "true", matchIfMissing = false)
public class RocketMQConfig {
}