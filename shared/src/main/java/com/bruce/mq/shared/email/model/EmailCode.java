package com.bruce.mq.shared.email.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 邮箱验证码实体类
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailCode {
    
    /**
     * 邮箱地址
     */
    private String emailAddress;
    
    /**
     * 验证码
     */
    private String code;
    
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
        return "EmailCode{" +
                "emailAddress='" + emailAddress + '\'' +
                ", code='" + code + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}