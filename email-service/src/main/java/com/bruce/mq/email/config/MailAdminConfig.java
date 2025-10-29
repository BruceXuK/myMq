package com.bruce.mq.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件管理员配置类
 * 用于配置邮件管理员相关信息
 * 
 * @author BruceXuK
 */
@Component
@ConfigurationProperties(prefix = "app.mail.admin")
public class MailAdminConfig {
    
    /**
     * 管理员邮箱地址
     */
    private String emailAddress;
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}