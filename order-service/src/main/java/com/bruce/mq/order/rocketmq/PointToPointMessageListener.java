package com.bruce.mq.order.rocketmq;

import com.bruce.mq.shared.order.message.model.PointToPointMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 点对点消息监听器
 * 负责接收并处理点对点消息
 *
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "p2p-topic",
        consumerGroup = "order-p2p-consumer"
)
public class PointToPointMessageListener implements RocketMQListener<PointToPointMessage> {

    /**
     * 处理点对点消息
     *
     * @param message 点对点消息
     */
    @Override
    public void onMessage(PointToPointMessage message) {
        try {
            log.info("订单服务收到点对点消息: {}", message);
            log.info("订单服务处理点对点消息完成");
        } catch (Exception e) {
            log.error("订单服务处理点对点消息失败: " + message, e);
        }
    }
}
