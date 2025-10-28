package com.bruce.mq.email.rocketmq;

import com.bruce.mq.email.service.EmailService;
import com.bruce.mq.shared.email.model.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 自定义邮件消息监听器
 * 负责接收并处理来自其他服务的自定义邮件发送请求
 * 包括订单确认邮件、订单取消通知邮件等
 *
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "order-topic",
        consumerGroup = "email-service-request-consumer",
        selectorExpression = "CUSTOM_EMAIL || ORDER_TIMEOUT_CHECK"
)
public class EmailRequestListener implements RocketMQListener<EmailRequest> {

    @Autowired
    private EmailService emailService;

    /**
     * 处理自定义邮件发送请求
     *
     * @param emailRequest 邮件请求对象
     */
    @Override
    public void onMessage(EmailRequest emailRequest) {
        try {
            log.info("邮件服务收到自定义邮件发送请求: {}", emailRequest);

            // 检查邮件请求参数
            if (emailRequest == null) {
                log.error("收到空的邮件请求对象");
                return;
            }

            // 检查必要字段
            if (emailRequest.getEmailAddress() == null || emailRequest.getEmailAddress().isEmpty()) {
                log.error("邮件请求缺少收件人地址: {}", emailRequest);
                return;
            }

            if (emailRequest.getSubject() == null || emailRequest.getSubject().isEmpty()) {
                log.error("邮件请求缺少主题: {}", emailRequest);
                return;
            }

            if (emailRequest.getContent() == null || emailRequest.getContent().isEmpty()) {
                log.error("邮件请求缺少内容: {}", emailRequest);
                return;
            }

            // 发送邮件给用户
            boolean success = emailService.sendCustomEmail(emailRequest);

            if (success) {
                log.info("自定义邮件发送成功: 收件人={}, 主题={}",
                        emailRequest.getEmailAddress(), emailRequest.getSubject());
            } else {
                log.error("自定义邮件发送失败: 收件人={}, 主题={}",
                        emailRequest.getEmailAddress(), emailRequest.getSubject());
            }
        } catch (Exception e) {
            log.error("处理自定义邮件发送请求失败: " + emailRequest, e);
        }
    }

    /**
     * 初始化方法，用于确认监听器已正确加载
     */
    @PostConstruct
    public void init() {
        log.info("邮件服务监听器已初始化，监听主题: order-topic，消费者组: email-service-request-consumer");
    }
}
