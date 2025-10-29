package com.bruce.mq.email.rocketmq;

import com.bruce.mq.email.service.EmailService;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.shared.notification.MaintenanceNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.time.format.DateTimeFormatter;

/**
 * 邮箱服务系统维护通知消息监听器
 * 负责接收并处理系统维护通知消息，并向邮箱服务负责人发送邮件
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

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private Environment environment;

    // 从配置文件或环境变量中获取负责人邮箱地址
    @Value("${app.mail.admin.email-address:your_email_address@qq.com}")
    private String adminEmail;

    /**
     * 处理系统维护通知消息
     *
     * @param notification 维护通知对象
     */
    @Override
    public void onMessage(MaintenanceNotification notification) {
        try {
            log.info("邮箱服务收到系统维护通知: {}", notification);

            // 发送邮件通知给邮箱服务负责人
            sendEmailNotificationToAdmin(notification);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = notification.getStartTime().format(formatter);
            String endTimeStr = notification.getEndTime().format(formatter);

            // 根据是否紧急维护采取不同措施
            if (notification.isUrgent()) {
                log.warn("紧急系统维护即将开始，请立即处理未完成任务!");
            } else {
                log.info("计划系统维护通知，将影响 {} 至 {} 的服务", startTimeStr, endTimeStr);
            }

            log.info("邮箱服务系统维护通知处理完成 - 标题: {}, 开始时间: {}, 结束时间: {}, 紧急: {}",
                    notification.getTitle(), startTimeStr, endTimeStr, notification.isUrgent());

        } catch (Exception e) {
            log.error("邮箱服务处理系统维护通知失败: " + notification, e);
        }
    }

    /**
     * 发送系统维护通知邮件给邮箱服务负责人
     *
     * @param notification 维护通知对象
     */
    private void sendEmailNotificationToAdmin(MaintenanceNotification notification) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = notification.getStartTime().format(formatter);
            String endTimeStr = notification.getEndTime().format(formatter);
            String urgentStr = notification.isUrgent() ? "紧急" : "计划";
            String serviceName = environment.getProperty("spring.application.name", "未知服务");

            // 构造邮件内容
            String subject = "系统维护通知 - " + notification.getTitle();
            String content = String.format(
                "【%s】收到系统维护通知：\n\n" +
                "标题：%s\n" +
                "内容：%s\n" +
                "开始时间：%s\n" +
                "结束时间：%s\n" +
                "类型：%s维护\n\n" +
                "%s系统请查看更新通知。",
                serviceName,
                notification.getTitle(),
                notification.getContent(),
                startTimeStr,
                endTimeStr,
                urgentStr,
                serviceName
            );

            // 创建邮件请求对象
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setEmailAddress(adminEmail);
            emailRequest.setSubject(subject);
            emailRequest.setContent(content);

            // 发送邮件
            boolean success = emailService.sendCustomEmail(emailRequest);
            
            if (success) {
                log.info("已发送系统维护通知邮件给邮箱服务负责人，收件人: {}", adminEmail);
            } else {
                log.error("发送系统维护通知邮件给邮箱服务负责人失败，收件人: {}", adminEmail);
            }
        } catch (Exception e) {
            log.error("发送系统维护通知邮件给邮箱服务负责人异常", e);
        }
    }
}