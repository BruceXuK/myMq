package com.bruce.mq.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RocketMQ通用配置类
 * 用于集中管理RocketMQ相关配置
 */
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQConfig {
    
    /**
     * NameServer地址
     */
    private String nameServer = "localhost:9876";

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }
}