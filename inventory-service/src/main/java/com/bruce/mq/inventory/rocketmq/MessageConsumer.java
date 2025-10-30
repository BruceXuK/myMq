package com.bruce.mq.inventory.rocketmq;

import com.bruce.mq.shared.order.message.model.PointToPointMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 * 负责接收并处理消息
 *
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "test-topic",
        consumerGroup = "inventory-consumer-group"
)
public class MessageConsumer implements RocketMQListener<PointToPointMessage> {

    /**
     * 处理消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(PointToPointMessage message) {
        try {
            log.info("库存服务收到消息: {}", message);
            log.info("库存服务处理消息完成");
        } catch (Exception e) {
            log.error("库存服务处理消息失败: " + message, e);
        }
    }
}
