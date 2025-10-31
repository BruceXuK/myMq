package com.bruce.mq.shared.email.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 群发邮件请求实体类
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MassEmailRequest {
    
    /**
     * 邮箱地址列表
     */
    private List<String> emailAddresses;
    
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
        return "MassEmailRequest{" +
                "emailAddresses=" + emailAddresses +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}