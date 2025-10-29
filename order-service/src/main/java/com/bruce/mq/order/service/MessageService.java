package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.shared.email.model.EmailRequest;

/**
 * 消息服务接口
 * 定义消息发送相关操作的方法
 *
 * @author BruceXuK
 */
public interface MessageService {

    /**
     * 扣减库存操作
     * 通过消息队列通知库存服务进行库存扣减
     *
     * @param order 订单信息
     */
    void deductInventory(Order order);

    /**
     * 回滚库存操作
     * 通过消息队列通知库存服务进行库存回滚
     *
     * @param order 订单信息
     */
    void rollbackInventory(Order order);

    /**
     * 发送订单确认邮件
     * 通过消息队列通知邮件服务发送订单确认邮件
     * 注意：此邮件在订单创建后立即发送，无论是否支付
     *
     * @param order 订单信息
     */
    void sendOrderConfirmationEmail(Order order);

    /**
     * 发送订单超时检查延迟消息
     * 发送延迟消息用于检查订单是否超时未支付
     *
     * @param order 订单信息
     * @param delayLevel 延迟级别 (1-18)
     */
    void sendOrderTimeoutCheckMessage(Order order, int delayLevel);

    /**
     * 发送订单超时取消通知邮件
     * 通过消息队列通知邮件服务发送订单超时取消通知邮件
     * 此方法在订单超时未支付被取消后调用，通知用户订单已被取消
     *
     * @param order 订单信息
     */
    void sendOrderTimeoutNotificationEmail(Order order);
    
    /**
     * 发送支付成功通知邮件
     *
     * @param order 订单信息
     */
    void sendPaymentSuccessEmail(Order order);
}