package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.model.Order;
import org.apache.rocketmq.client.producer.SendResult;

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

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @return 支付结果
     */
    boolean payOrder(Long orderId);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @return 取消结果
     */
    boolean cancelOrder(Long orderId);

    /**
     * 发送订单超时取消通知邮件
     * 在订单超时未支付被取消后调用，通知用户订单已被取消
     *
     * @param order 订单信息
     */
    void sendOrderTimeoutNotificationEmail(Order order);
}
