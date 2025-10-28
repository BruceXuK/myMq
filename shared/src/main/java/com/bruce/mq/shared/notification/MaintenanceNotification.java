package com.bruce.mq.shared.notification;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统维护通知实体类
 * 用于表示系统维护相关的通知信息
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceNotification {
    /**
     * 通知ID
     */
    private Long id;
    
    /**
     * 维护标题
     */
    private String title;
    
    /**
     * 维护内容
     */
    private String content;
    
    /**
     * 维护开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 维护结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 通知发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 是否紧急维护
     */
    private boolean urgent;
    
    /**
     * 带参构造函数
     * 
     * @param title 维护标题
     * @param content 维护内容
     * @param startTime 维护开始时间
     * @param endTime 维护结束时间
     * @param urgent 是否紧急维护
     */
    public MaintenanceNotification(String title, String content, LocalDateTime startTime, 
                                 LocalDateTime endTime, boolean urgent) {
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.urgent = urgent;
        this.publishTime = LocalDateTime.now();
    }
}