package com.bruce.mq.order.rocketmq;

import com.bruce.mq.order.service.OrderService;
import com.bruce.mq.shared.order.model.OrderTimeoutEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单超时消息监听器
 * 负责监听订单超时事件并取消超时订单
 *
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "order-topic",
        consumerGroup = "order-timeout-consumer",
        selectorExpression = "ORDER_TIMEOUT"
)
public class OrderTimeoutListener implements RocketMQListener<OrderTimeoutEvent> {

    @Autowired
    private OrderService orderService;

    /**
     * 处理订单超时消息
     *
     * @param event 订单超时事件
     */
    @Override
    public void onMessage(OrderTimeoutEvent event) {
        try {
            log.info("订单服务收到订单超时消息: {}", event);
            orderService.cancelOrder(event.getOrderId());
            log.info("订单服务处理订单超时消息完成，已取消订单 {}", event.getOrderId());
        } catch (Exception e) {
            log.error("订单服务处理订单超时消息失败: " + event, e);
        }
    }
}