package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.enums.OrderStatus;
import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.order.repository.OrderRepository;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单服务实现类
 * 实现订单创建、查询等业务逻辑
 *
 * @author BruceXuK
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private MessageService messageService;

    /**
     * 创建订单
     * 1. 保存订单信息
     * 2. 发送邮件通知客户
     * 3. 发送延迟消息用于超时检查
     *
     * @param order 订单信息
     * @return 创建成功的订单
     */
    @Override
    public Order createOrder(Order order) {
        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 发送邮件通知客户（即使未支付也会发送确认邮件）
        messageService.sendOrderConfirmationEmail(savedOrder);


        // 发送延迟消息用于超时检查（1分钟后未支付则取消订单）
        messageService.sendOrderTimeoutCheckMessage(savedOrder, 5);
        return savedOrder;
    }

    /**
     * 根据ID获取订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 支付成功处理
     *
     * @param orderId 订单ID
     * @return 处理结果
     */
    @Override
    public boolean payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return false;
        }

        // 更新订单状态为已支付
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        // 发送支付成功邮件
        messageService.sendPaymentSuccessEmail(order);

        // 发送消息到库存服务扣减库存
        messageService.deductInventory(order);

        return true;
    }

    /**
     * 取消订单并回滚库存
     *
     * @param orderId 订单ID
     * @return 处理结果
     */
    @Override
    public boolean cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return false;
        }

        // 更新订单状态为已取消
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // 发送消息到库存服务恢复库存
        messageService.rollbackInventory(order);

        return true;
    }

    /**
     * 发送订单超时取消通知邮件
     *
     * @param order 订单信息
     * @return 消息发送结果
     */
    @Override
    public void sendOrderTimeoutNotificationEmail(Order order) {
        messageService.sendOrderTimeoutNotificationEmail(order);
    }
}
