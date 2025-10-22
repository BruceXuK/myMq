package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.order.repository.OrderRepository;
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
    
    /**
     * 创建订单
     * 1. 保存订单信息
     * 2. 发送消息到库存服务扣减库存
     * 3. 发送邮件通知客户
     * 
     * @param order 订单信息
     * @return 创建成功的订单
     */
    @Override
    public Order createOrder(Order order) {
        // 保存订单
        Order savedOrder = orderRepository.save(order);
        
        // 发送消息到库存服务扣减库存
        orderRepository.deductInventory(savedOrder);
        
        // 发送邮件通知客户
        orderRepository.sendOrderConfirmationEmail(savedOrder);
        
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
}