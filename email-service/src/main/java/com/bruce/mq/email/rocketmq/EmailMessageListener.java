package com.bruce.mq.email.rocketmq;

import com.bruce.mq.email.service.EmailService;
import com.bruce.mq.shared.email.model.EmailCode;
import com.bruce.mq.shared.email.model.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 邮件消息监听器
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "email-topic",
        consumerGroup = "email-consumer-group",
        selectorExpression = "EMAIL || CUSTOM_EMAIL"
)
public class EmailMessageListener implements RocketMQListener<Object> {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 处理接收到的消息
     * 
     * @param obj 接收到的消息对象
     */
    @Override
    public void onMessage(Object obj) {
        try {
            Object messageObj = obj;
            
            // 如果是Message类型，尝试提取有效载荷
            if (obj instanceof Message) {
                Message<?> message = (Message<?>) obj;
                Object payload = message.getPayload();
                
                // 如果payload是字节数组，尝试转换为对象
                if (payload instanceof byte[]) {
                    String jsonString = new String((byte[]) payload, StandardCharsets.UTF_8);
                    try {
                        // 尝试解析为EmailCode
                        messageObj = objectMapper.readValue(jsonString, EmailCode.class);
                    } catch (Exception e) {
                        try {
                            // 尝试解析为EmailRequest
                            messageObj = objectMapper.readValue(jsonString, EmailRequest.class);
                        } catch (Exception ex) {
                            log.warn("无法将消息解析为EmailCode或EmailRequest: {}", jsonString, ex);
                            messageObj = jsonString; // 保持原始字符串
                        }
                    }
                } else {
                    messageObj = payload;
                }
            } else if (obj instanceof String) {
                // 如果直接是字符串（如JSON），尝试解析为EmailCode或EmailRequest
                try {
                    messageObj = objectMapper.readValue((String) obj, EmailCode.class);
                } catch (Exception e) {
                    try {
                        messageObj = objectMapper.readValue((String) obj, EmailRequest.class);
                    } catch (Exception ex) {
                        log.warn("无法将字符串消息解析为EmailCode或EmailRequest: {}", obj, ex);
                    }
                }
            }
            
            log.info("收到邮件消息: {}", messageObj);
            
            // 由于使用了selectorExpression过滤，这里只会收到EmailCode或EmailRequest类型的消息
            if (messageObj instanceof EmailCode) {
                // 处理邮件验证码消息
                EmailCode emailCode = (EmailCode) messageObj;
                log.info("处理邮箱验证码消息: {}", emailCode);
                // 调用邮件服务发送邮件
                boolean success = emailService.sendEmailCode(emailCode);
                if (success) {
                    log.info("邮件验证码发送成功: {}", emailCode.getEmailAddress());
                } else {
                    log.error("邮件验证码发送失败: {}", emailCode.getEmailAddress());
                }
            } else if (messageObj instanceof EmailRequest) {
                // 处理自定义邮件消息
                EmailRequest emailRequest = (EmailRequest) messageObj;
                log.info("处理自定义邮件消息: {}", emailRequest);
                // 调用邮件服务发送自定义邮件
                boolean success = emailService.sendCustomEmail(emailRequest);
                if (success) {
                    log.info("自定义邮件发送成功: {}", emailRequest.getEmailAddress());
                } else {
                    log.error("自定义邮件发送失败: {}", emailRequest.getEmailAddress());
                }
            }
        } catch (Exception e) {
            log.error("处理邮件消息失败", e);
        }
    }
}