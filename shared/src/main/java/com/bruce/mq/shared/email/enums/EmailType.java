package com.bruce.mq.shared.email.enums;

/**
 * 邮件类型枚举
 * 
 * @author BruceXuK
 */
public enum EmailType {
    /**
     * 验证码邮件
     */
    VERIFICATION_CODE("邮件"),
    
    /**
     * 自定义邮件
     */
    CUSTOM("自定义邮件");
    
    private final String description;
    
    EmailType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}