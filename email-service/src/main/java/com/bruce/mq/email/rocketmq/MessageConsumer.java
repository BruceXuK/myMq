package com.bruce.mq.email.rocketmq;

import com.bruce.mq.shared.email.model.EmailCode;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息消费者服务
 * 
 * @author BruceXuK
 */
@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private EmailService emailService;

    /**
     * 处理对象消息
     *
     * @param obj 对象消息
     */
    public void receiveObjectMsg(Object obj) {
        if (obj instanceof EmailCode) {
            // 处理邮件验证码消息
            EmailCode emailCode = (EmailCode) obj;
            logger.info("收到邮箱验证码消息: {}", emailCode);
            // 调用邮件服务发送邮件
            boolean success = emailService.sendEmailCode(emailCode);
            if (success) {
                logger.info("邮件验证码发送成功: {}", emailCode.getEmailAddress());
            } else {
                logger.error("邮件验证码发送失败: {}", emailCode.getEmailAddress());
            }
        } else if (obj instanceof EmailRequest) {
            // 处理自定义邮件消息
            EmailRequest emailRequest = (EmailRequest) obj;
            logger.info("收到自定义邮件消息: {}", emailRequest);
            // 调用邮件服务发送自定义邮件
            boolean success = emailService.sendCustomEmail(emailRequest);
            if (success) {
                logger.info("自定义邮件发送成功: {}", emailRequest.getEmailAddress());
            } else {
                logger.error("自定义邮件发送失败: {}", emailRequest.getEmailAddress());
            }
        } else {
            logger.info("收到其他对象消息: {}", obj);
        }
    }
}