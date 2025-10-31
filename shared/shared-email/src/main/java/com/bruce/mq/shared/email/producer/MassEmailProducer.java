package com.bruce.mq.shared.email.producer;

import com.bruce.mq.shared.email.model.MassEmailRequest;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 群发邮件生产者
 * 提供发送群发邮件请求的方法
 *
 * @author BruceXuK
 */
@Component
public class MassEmailProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送群发邮件请求
     *
     * @param emailAddresses 邮箱地址列表
     * @param subject        邮件主题
     * @param content        邮件内容
     */
    public void sendMassEmail(List<String> emailAddresses, String subject, String content) {
        MassEmailRequest massEmailRequest = new MassEmailRequest(emailAddresses, subject, content);
        rocketMQTemplate.send("mass-email-topic", MessageBuilder.withPayload(massEmailRequest).build());
    }

    /**
     * 发送群发邮件请求
     *
     * @param massEmailRequest 群发邮件请求对象
     */
    public void sendMassEmail(MassEmailRequest massEmailRequest) {
        rocketMQTemplate.send("mass-email-topic", MessageBuilder.withPayload(massEmailRequest).build());
    }
}