package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.model.Order;

/**
 * 订单服务接口
 * 定义订单相关业务操作的方法
 * 
 * @author BruceXuK
 */
public interface OrderService {
    
    /**
     * 创建订单
     * 
     * @param order 订单信息
     * @return 创建成功的订单
     */
    Order createOrder(Order order);
    
    /**
     * 根据ID获取订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    Order getOrderById(Long id);
}