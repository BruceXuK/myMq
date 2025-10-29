package com.bruce.mq.user.rocketmq;

import com.bruce.mq.shared.message.model.PointToPointMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 点对点消息生产者
 * 负责发送只需要一个消费者处理的消息到MQ
 * 
 * @author BruceXuK
 */
@Service
public class PointToPointMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(PointToPointMessageProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送点对点消息
     * 
     * @param message 点对点消息对象
     */
    public void sendPointToPointMessage(PointToPointMessage message) {
        try {
            // 发送到point-to-point-topic主题，使用POINT_TO_POINT标签
            rocketMQTemplate.send("point-to-point-topic:POINT_TO_POINT",
                MessageBuilder.withPayload(message).build());
            
            logger.info("点对点消息发送成功: 内容={}, 类型={}", 
                       message.getContent(), message.getType());
        } catch (Exception e) {
            logger.error("发送点对点消息失败: 内容=" + message.getContent(), e);
        }
    }
    
    /**
     * 发送点对点消息
     * 
     * @param content 消息内容
     * @param type 消息类型
     */
    public void sendPointToPointMessage(String content, String type) {
        try {
            PointToPointMessage message = new PointToPointMessage(content, type);
            
            // 发送到point-to-point-topic主题，使用POINT_TO_POINT标签
            rocketMQTemplate.send("point-to-point-topic:POINT_TO_POINT",
                MessageBuilder.withPayload(message).build());
            
            logger.info("点对点消息发送成功: 内容={}, 类型={}", content, type);
        } catch (Exception e) {
            logger.error("发送点对点消息失败: 内容=" + content, e);
        }
    }
}