package com.bruce.mq.email.rocketmq;

import com.bruce.mq.shared.notification.MaintenanceNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * 系统维护通知消息监听器
 * 负责接收并处理系统维护通知消息
 * 
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "notification-topic",
        consumerGroup = "email-service-notification-consumer",
        selectorExpression = "SYSTEM_MAINTENANCE"
)
public class MaintenanceNotificationListener implements RocketMQListener<MaintenanceNotification> {

    /**
     * 处理系统维护通知消息
     * 
     * @param notification 维护通知对象
     */
    @Override
    public void onMessage(MaintenanceNotification notification) {
        try {
            log.info("邮件服务收到系统维护通知: {}", notification);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = notification.getStartTime().format(formatter);
            String endTimeStr = notification.getEndTime().format(formatter);
            
            // 根据是否紧急维护采取不同措施
            if (notification.isUrgent()) {
                log.warn("紧急系统维护即将开始，请立即处理!");
            } else {
                log.info("计划系统维护通知，将影响 {} 至 {} 的服务", startTimeStr, endTimeStr);
            }
            
            log.info("邮件服务系统维护通知处理完成 - 标题: {}, 开始时间: {}, 结束时间: {}, 紧急: {}", 
                    notification.getTitle(), startTimeStr, endTimeStr, notification.isUrgent());
                    
        } catch (Exception e) {
            log.error("邮件服务处理系统维护通知失败: " + notification, e);
        }
    }
}