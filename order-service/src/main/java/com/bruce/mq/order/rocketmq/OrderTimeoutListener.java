package com.bruce.mq.order.rocketmq;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.shared.order.enums.OrderStatus;
import com.bruce.mq.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单超时检查消息监听器
 * 负责接收并处理订单超时检查消息，检查订单是否超时未支付
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = "order-timeout-topic", 
    consumerGroup = "order-timeout-check-consumer"
)
public class OrderTimeoutListener implements RocketMQListener<Order> {

    @Autowired
    private OrderService orderService;

    /**
     * 处理订单超时检查消息
     * 当接收到订单超时检查消息时，检查订单状态，如果仍为PENDING则取消订单
     *
     * @param order 订单信息
     */
    @Override
    public void onMessage(Order order) {
        try {
            log.info("接收到订单超时检查消息: {}", order);
            
            // 获取最新的订单状态
            Order currentOrder = orderService.getOrderById(order.getId());
            if (currentOrder == null) {
                log.warn("订单不存在，订单ID: {}", order.getId());
                return;
            }
            
            // 检查订单是否仍为待处理状态
            if (currentOrder.getStatus() == OrderStatus.PENDING) {
                log.info("订单超时未支付，准备取消订单: {}", currentOrder.getOrderNo());
                
                // 取消订单
                boolean success = orderService.cancelOrder(order.getId());
                if (success) {
                    log.info("订单超时取消成功: 订单号={}", currentOrder.getOrderNo());
                    
                    // 发送超时取消通知邮件
                    orderService.sendOrderTimeoutNotificationEmail(currentOrder);
                } else {
                    log.error("订单超时取消失败: 订单号={}", currentOrder.getOrderNo());
                }
            } else {
                log.info("订单状态已变更，无需取消: 订单号={}, 当前状态={}", 
                        currentOrder.getOrderNo(), currentOrder.getStatus());
            }
        } catch (Exception e) {
            log.error("处理订单超时检查消息失败，订单号: " + order.getOrderNo(), e);
        }
    }
}