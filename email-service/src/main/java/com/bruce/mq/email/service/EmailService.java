package com.bruce.mq.email.service;

import com.bruce.mq.shared.email.model.EmailCode;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.shared.email.enums.EmailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务类
 * 
 * @author BruceXuK
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送邮件验证码
     *
     * @param emailCode 邮件验证码对象
     * @return 是否发送成功
     */
    public boolean sendEmailCode(EmailCode emailCode) {
        return sendEmail(emailCode.getEmailAddress(), emailCode.getSubject(), emailCode.getContent(), EmailType.VERIFICATION_CODE);
    }
    
    /**
     * 发送自定义邮件
     *
     * @param emailRequest 邮件请求对象
     * @return 是否发送成功
     */
    public boolean sendCustomEmail(EmailRequest emailRequest) {
        return sendEmail(emailRequest.getEmailAddress(), emailRequest.getSubject(), emailRequest.getContent(), EmailType.CUSTOM);
    }
    
    /**
     * 发送邮件的通用方法
     *
     * @param to 收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param type 邮件类型
     * @return 是否发送成功
     */
    private boolean sendEmail(String to, String subject, String content, EmailType type) {
        Exception lastException = null;
        
        // 记录邮件发送尝试
        logger.info("开始发送{}到: {}", type.getDescription(), to);
        
        // 尝试发送邮件，最多重试3次
        for (int i = 0; i < 3; i++) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content);

                javaMailSender.send(message);
                logger.info("{}发送成功: {} (尝试次数: {})", type.getDescription(), to, i + 1);
                return true;
            } catch (Exception e) {
                lastException = e;
                logger.warn("{}发送失败: {} (尝试次数: {})", type.getDescription(), to, i + 1, e);
                try {
                    // 等待一段时间再重试
                    Thread.sleep(1000 * (i + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.warn("{}发送重试被中断", type, ie);
                    break;
                }
            }
        }
        
        logger.error("{}发送最终失败: {}", type.getDescription(), to, lastException);
        return false;
    }
}