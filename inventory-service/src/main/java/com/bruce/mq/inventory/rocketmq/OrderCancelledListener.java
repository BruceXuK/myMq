package com.bruce.mq.inventory.rocketmq;

import com.bruce.mq.inventory.service.InventoryService;
import com.bruce.mq.shared.inventory.model.Inventory;
import com.bruce.mq.shared.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 订单取消消息监听器
 * 负责接收并处理来自订单服务的订单取消消息，回滚库存
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = "inventory-topic", 
    consumerGroup = "inventory-service-cancel-consumer",
    selectorExpression = "ORDER_CANCELLED"
)
public class OrderCancelledListener implements RocketMQListener<Order> {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 处理订单取消消息
     * 当接收到订单取消消息时，根据订单信息回滚相应商品的库存
     *
     * @param order 订单信息
     */
    @Override
    public void onMessage(Order order) {
        try {
            log.info("接收到订单取消消息: {}", order);
            
            // 根据订单信息回滚库存（增加库存）
            Inventory inventory = new Inventory();
            inventory.setProductId(order.getProductId());
            inventory.setQuantity(order.getQuantity());
            
            boolean success = inventoryService.addInventory(inventory);
            if (success) {
                log.info("成功回滚库存: 商品ID={}, 增加数量={}", order.getProductId(), order.getQuantity());
            } else {
                log.error("回滚库存失败: 商品ID={}, 增加数量={}", order.getProductId(), order.getQuantity());
            }
        } catch (Exception e) {
            log.error("处理订单取消消息失败", e);
        }
    }
}