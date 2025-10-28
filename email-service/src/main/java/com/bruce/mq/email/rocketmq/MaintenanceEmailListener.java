package com.bruce.mq.email.rocketmq;

import com.bruce.mq.email.service.EmailService;
import com.bruce.mq.shared.email.model.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统维护通知邮件消息监听器
 * 负责接收并处理来自其他服务的系统维护通知邮件发送请求
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "email-topic",
        consumerGroup = "email-service-maintenance-consumer",
        selectorExpression = "SYSTEM_MAINTENANCE_NOTIFICATION"
)
public class MaintenanceEmailListener implements RocketMQListener<EmailRequest> {

    @Autowired
    private EmailService emailService;

    /**
     * 处理系统维护通知邮件发送请求
     * 
     * @param emailRequest 邮件请求对象
     */
    @Override
    public void onMessage(EmailRequest emailRequest) {
        try {
            log.info("邮件服务收到系统维护通知邮件发送请求: {}", emailRequest);
            
            // 发送邮件给负责人
            boolean success = emailService.sendCustomEmail(emailRequest);
            
            if (success) {
                log.info("系统维护通知邮件发送成功: 收件人={}, 主题={}", 
                        emailRequest.getEmailAddress(), emailRequest.getSubject());
            } else {
                log.error("系统维护通知邮件发送失败: 收件人={}, 主题={}", 
                        emailRequest.getEmailAddress(), emailRequest.getSubject());
            }
        } catch (Exception e) {
            log.error("处理系统维护通知邮件发送请求失败: " + emailRequest, e);
        }
    }
}