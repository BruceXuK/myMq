package com.bruce.mq.user.rocketmq;

import com.bruce.mq.shared.notification.MaintenanceNotification;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 系统维护通知消息生产者
 * 负责发送系统维护通知消息到MQ
 * 
 * @author BruceXuK
 */
@Service
public class MaintenanceNotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceNotificationProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送系统维护通知
     * 
     * @param title 维护标题
     * @param content 维护内容
     * @param startTime 维护开始时间
     * @param endTime 维护结束时间
     * @param urgent 是否紧急维护
     */
    public void sendMaintenanceNotification(String title, String content, 
                                          LocalDateTime startTime, LocalDateTime endTime, 
                                          boolean urgent) {
        try {
            // 创建维护通知对象
            MaintenanceNotification notification = new MaintenanceNotification(
                title, content, startTime, endTime, urgent);
            
            // 发送到notification-topic主题，使用SYSTEM_MAINTENANCE标签
            rocketMQTemplate.send("notification-topic:SYSTEM_MAINTENANCE",
                MessageBuilder.withPayload(notification).build());
            
            logger.info("系统维护通知发送成功: 标题={}, 开始时间={}, 结束时间={}, 紧急={}", 
                       title, startTime, endTime, urgent);
        } catch (Exception e) {
            logger.error("发送系统维护通知失败: 标题=" + title, e);
        }
    }
    
    /**
     * 发送系统维护通知
     * 
     * @param notification 维护通知对象
     */
    public void sendMaintenanceNotification(MaintenanceNotification notification) {
        try {
            // 发送到notification-topic主题，使用SYSTEM_MAINTENANCE标签
            rocketMQTemplate.send("notification-topic:SYSTEM_MAINTENANCE",
                MessageBuilder.withPayload(notification).build());
            
            logger.info("系统维护通知发送成功: 标题={}, 开始时间={}, 结束时间={}, 紧急={}", 
                       notification.getTitle(), notification.getStartTime(), 
                       notification.getEndTime(), notification.isUrgent());
        } catch (Exception e) {
            logger.error("发送系统维护通知失败: 标题=" + notification.getTitle(), e);
        }
    }
}