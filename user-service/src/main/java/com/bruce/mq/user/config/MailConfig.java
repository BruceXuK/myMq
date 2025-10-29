package com.bruce.mq.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件配置类
 * 用于配置邮件相关参数
 * 
 * @author BruceXuK
 */
@Configuration
public class MailConfig {
    
    @Value("${email-service.url:http://localhost:8088}")
    private String emailServiceUrl;

    public String getEmailServiceUrl() {
        return emailServiceUrl;
    }
}