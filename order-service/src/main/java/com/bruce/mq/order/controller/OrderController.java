package com.bruce.mq.order.controller;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 * 提供订单创建和查询的REST API接口
 * 
 * @author BruceXuK
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单接口
     * 
     * @param order 订单信息
     * @return 创建成功的订单
     */
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
    
    /**
     * 根据ID获取订单信息
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}