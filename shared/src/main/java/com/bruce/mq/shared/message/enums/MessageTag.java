package com.bruce.mq.shared.message.enums;

/**
 * 消息标签枚举
 * 
 * @author BruceXuK
 */
public enum MessageTag {
    /**
     * 邮件验证码标签
     */
    EMAIL("EMAIL"),
    
    /**
     * 自定义邮件标签
     */
    CUSTOM_EMAIL("CUSTOM_EMAIL"),
    
    /**
     * 订单创建标签
     */
    ORDER_CREATED("ORDER_CREATED"),
    
    /**
     * 订单取消标签
     */
    ORDER_CANCELLED("ORDER_CANCELLED"),
    
    /**
     * 订单取消通知邮件标签
     */
    ORDER_CANCELLED_NOTIFICATION("ORDER_CANCELLED_NOTIFICATION"),
    
    /**
     * 库存扣减标签
     */
    INVENTORY_DEDUCTED("INVENTORY_DEDUCTED"),
    
    /**
     * 库存增加标签
     */
    INVENTORY_ADDED("INVENTORY_ADDED"),
    
    /**
     * 系统维护通知标签
     */
    SYSTEM_MAINTENANCE("SYSTEM_MAINTENANCE"),
    
    /**
     * 系统维护通知邮件标签
     */
    SYSTEM_MAINTENANCE_NOTIFICATION("SYSTEM_MAINTENANCE_NOTIFICATION");
    
    private final String tag;
    
    MessageTag(String tag) {
        this.tag = tag;
    }
    
    public String getTag() {
        return tag;
    }
}