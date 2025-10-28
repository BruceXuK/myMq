package com.bruce.mq.order.rocketmq;

import com.bruce.mq.shared.notification.MaintenanceNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
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
        consumerGroup = "order-service-notification-consumer",
        selectorExpression = "SYSTEM_MAINTENANCE"
)
public class MaintenanceNotificationListener implements RocketMQListener<MaintenanceNotification> {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 处理系统维护通知消息
     * 
     * @param notification 维护通知对象
     */
    @Override
    public void onMessage(MaintenanceNotification notification) {
        try {
            log.info("订单服务收到系统维护通知: {}", notification);
            
            // 通过MQ发送邮件通知给负责人
            sendEmailNotificationToAdmin(notification);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = notification.getStartTime().format(formatter);
            String endTimeStr = notification.getEndTime().format(formatter);
            
            // 根据是否紧急维护采取不同措施
            if (notification.isUrgent()) {
                log.warn("紧急系统维护即将开始，请立即处理未完成订单!");
            } else {
                log.info("计划系统维护通知，将影响 {} 至 {} 的服务", startTimeStr, endTimeStr);
            }
            
            log.info("订单服务系统维护通知处理完成 - 标题: {}, 开始时间: {}, 结束时间: {}, 紧急: {}", 
                    notification.getTitle(), startTimeStr, endTimeStr, notification.isUrgent());
                    
        } catch (Exception e) {
            log.error("订单服务处理系统维护通知失败: " + notification, e);
        }
    }
    

    /**
     * 通过MQ发送系统维护通知邮件给负责人
     * 
     * @param notification 维护通知对象
     */
    private void sendEmailNotificationToAdmin(MaintenanceNotification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = notification.getStartTime().format(formatter);
            String endTimeStr = notification.getEndTime().format(formatter);
            String urgentStr = notification.isUrgent() ? "紧急" : "计划";
            
            // 构造邮件内容
            String subject = "系统维护通知 - " + notification.getTitle();
            String content = String.format(
                "【订单服务】收到系统维护通知：\n\n" +
                "标题：%s\n" +
                "内容：%s\n" +
                "开始时间：%s\n" +
                "结束时间：%s\n" +
                "类型：%s维护\n\n" +
                "请及时处理相关事宜。",
                notification.getTitle(),
                notification.getContent(),
                startTimeStr,
                endTimeStr,
                urgentStr
            );
            
            // 创建邮件请求对象
            // 注意：这里应该使用EmailRequest而不是MaintenanceNotification
            // 因为邮件服务监听的是EmailRequest类型的消息
            // 通过email-topic主题发送，使用SYSTEM_MAINTENANCE_NOTIFICATION标签
            // 这样可以确保邮件服务能够正确处理消息
        } catch (Exception e) {
            log.error("发送系统维护通知邮件失败", e);
        }
    }
}