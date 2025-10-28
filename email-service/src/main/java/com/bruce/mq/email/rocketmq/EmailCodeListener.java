package com.bruce.mq.email.rocketmq;

import com.bruce.mq.email.service.EmailService;
import com.bruce.mq.shared.email.model.EmailCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码消息监听器
 * 负责接收并处理来自其他服务的邮箱验证码邮件发送请求
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "code-topic",
        consumerGroup = "email-service-code-consumer",
        selectorExpression = "EMAIL"
)
public class EmailCodeListener implements RocketMQListener<EmailCode> {

    @Autowired
    private EmailService emailService;

    /**
     * 处理邮箱验证码邮件发送请求
     * 
     * @param emailCode 邮箱验证码对象
     */
    @Override
    public void onMessage(EmailCode emailCode) {
        try {
            log.info("邮件服务收到邮箱验证码邮件发送请求: {}", emailCode);
            
            // 发送邮件给用户
            boolean success = emailService.sendEmailCode(emailCode);
            
            if (success) {
                log.info("邮箱验证码邮件发送成功: 收件人={}, 主题={}", 
                        emailCode.getEmailAddress(), emailCode.getSubject());
            } else {
                log.error("邮箱验证码邮件发送失败: 收件人={}, 主题={}", 
                        emailCode.getEmailAddress(), emailCode.getSubject());
            }
        } catch (Exception e) {
            log.error("处理邮箱验证码邮件发送请求失败: " + emailCode, e);
        }
    }
}