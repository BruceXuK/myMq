package com.bruce.mq.shared.email.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 邮件发送请求实体类
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    /**
     * 邮箱地址
     */
    private String emailAddress;
    
    /**
     * 邮件主题
     */
    private String subject;
    
    /**
     * 邮件内容
     */
    private String content;
    

    
    @Override
    public String toString() {
        return "EmailRequest{" +
                "emailAddress='" + emailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}