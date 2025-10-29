package com.bruce.mq.inventory.rocketmq;

import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.shared.notification.MaintenanceNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

/**
 * 库存服务系统维护通知消息监听器
 * 负责接收并处理系统维护通知消息
 *
 * @author BruceXuK
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "notification-topic",
        consumerGroup = "inventory-service-notification-consumer",
        selectorExpression = "SYSTEM_MAINTENANCE"
)
public class MaintenanceNotificationListener implements RocketMQListener<MaintenanceNotification> {

    // 从配置文件或环境变量中获取负责人邮箱地址
    @Value("${app.mail.admin.email-address:your_email_address@qq.com}")
    private String adminEmail;
    
    @Value("${email-service.url:http://localhost:8088}")
    private String emailServiceUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private Environment environment;

    /**
     * 处理系统维护通知消息
     *
     * @param notification 维护通知对象
     */
    @Override
    public void onMessage(MaintenanceNotification notification) {
        try {
            log.info("库存服务收到系统维护通知: {}", notification);

            // 发送邮件通知给库存服务负责人
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

            log.info("库存服务系统维护通知处理完成 - 标题: {}, 开始时间: {}, 结束时间: {}, 紧急: {}",
                    notification.getTitle(), startTimeStr, endTimeStr, notification.isUrgent());

        } catch (Exception e) {
            log.error("库存服务处理系统维护通知失败: " + notification, e);
        }
    }

    /**
     * 通过HTTP调用发送系统维护通知邮件给负责人
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

            // 通过HTTP调用邮件服务发送邮件
            String url = emailServiceUrl + "/email/send-custom";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<EmailRequest> request = new HttpEntity<>(emailRequest, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("已通过HTTP调用发送系统维护通知邮件，收件人: {}", adminEmail);
            } else {
                log.error("通过HTTP调用发送系统维护通知邮件失败，状态码: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("发送系统维护通知邮件失败", e);
        }
    }
}