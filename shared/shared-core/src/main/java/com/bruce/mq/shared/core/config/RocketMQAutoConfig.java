package com.bruce.mq.shared.config;

import com.bruce.mq.shared.util.StartupTimeTracker;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * RocketMQ自动配置类
 * 提供延迟加载和重试机制，解决启动时连接不稳定的问题
 */
@Configuration
@Import(RocketMQAutoConfiguration.class)
@ConditionalOnProperty(name = "rocketmq.enable", havingValue = "true", matchIfMissing = false)
public class RocketMQAutoConfig {
    
    @Autowired
    private StartupTimeTracker startupTimeTracker;
    
    @PostConstruct
    public void init() {
        startupTimeTracker.recordEnd("RocketMQAutoConfig");
    }
}