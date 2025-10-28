package com.bruce.mq.shared.order.enums;

/**
 * 订单状态枚举
 * 
 * @author BruceXuK
 */
public enum OrderStatus {
    /**
     * 待处理
     */
    PENDING("PENDING"),
    
    /**
     * 已确认
     */
    CONFIRMED("CONFIRMED"),
    
    /**
     * 已取消
     */
    CANCELLED("CANCELLED");
    
    private final String status;
    
    OrderStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return status;
    }
}