package com.bruce.mq.shared.order.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 订单超时事件实体类
 * 用于表示订单超时的事件消息
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTimeoutEvent {
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;
    
    /**
     * 带参构造函数
     * 
     * @param orderId 订单ID
     */
    public OrderTimeoutEvent(Long orderId) {
        this.orderId = orderId;
        this.eventTime = LocalDateTime.now();
    }
}