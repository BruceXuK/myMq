package com.bruce.mq.inventory.rocketmq;

import com.bruce.mq.shared.order.model.OrderCancelledEvent;
import com.bruce.mq.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单取消消息监听器
 * 负责监听订单取消事件并恢复库存
 *
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "order-topic",
        consumerGroup = "inventory-order-cancelled-consumer",
        selectorExpression = "ORDER_CANCELLED"
)
public class OrderCancelledListener implements RocketMQListener<OrderCancelledEvent> {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 处理订单取消消息
     *
     * @param event 订单取消事件
     */
    @Override
    public void onMessage(OrderCancelledEvent event) {
        try {
            log.info("库存服务收到订单取消消息: {}", event);
            inventoryService.restoreInventory(event.getProductId(), event.getQuantity());
            log.info("库存服务处理订单取消消息完成，已恢复商品 {} 的库存 {} 件", event.getProductId(), event.getQuantity());
        } catch (Exception e) {
            log.error("库存服务处理订单取消消息失败: " + event, e);
        }
    }
}