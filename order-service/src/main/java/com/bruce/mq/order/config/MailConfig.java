package com.bruce.mq.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置类
 * 用于读取和管理邮件相关配置
 * 
 * @author BruceXuK
 */
@Component
@ConfigurationProperties(prefix = "mail")
public class MailConfig {
    
    /**
     * 默认收件人邮箱地址
     */
    private String defaultToAddress;

    public String getDefaultToAddress() {
        return defaultToAddress;
    }

    public void setDefaultToAddress(String defaultToAddress) {
        this.defaultToAddress = defaultToAddress;
    }
}