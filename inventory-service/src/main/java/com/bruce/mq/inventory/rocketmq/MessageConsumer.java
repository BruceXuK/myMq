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
 * 消息消费者
 * 负责接收并处理来自订单服务的订单创建消息
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = "order-topic", 
    consumerGroup = "inventory-consumer-group",
    selectorExpression = "ORDER_CREATED"
)
public class MessageConsumer implements RocketMQListener<Order> {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 处理订单创建消息
     * 当接收到订单创建消息时，根据订单信息扣减相应商品的库存
     *
     * @param order 订单信息
     */
    @Override
    public void onMessage(Order order) {
        try {
            log.info("接收到订单创建消息: {}", order);
            
            // 根据订单信息扣减库存
            Inventory inventory = new Inventory();
            inventory.setProductId(order.getProductId());
            inventory.setQuantity(order.getQuantity());
            boolean success = inventoryService.deductInventory(inventory);
            
            if (success) {
                log.info("成功扣减库存: 商品ID={}, 扣减数量={}", order.getProductId(), order.getQuantity());
            } else {
                log.error("扣减库存失败: 商品ID={}, 扣减数量={}", order.getProductId(), order.getQuantity());
            }
        } catch (Exception e) {
            log.error("处理消息失败", e);
        }
    }
}