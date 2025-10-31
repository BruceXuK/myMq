package com.bruce.mq.email.controller;

import com.bruce.mq.email.service.EmailService;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.shared.email.model.MassEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 群发邮件控制器
 * 提供群发邮件的REST API接口
 *
 * @author BruceXuK
 */
@RestController
@RequestMapping("/mass-emails")
public class MassEmailController {

    private static final Logger logger = LoggerFactory.getLogger(MassEmailController.class);

    @Autowired
    private EmailService emailService;

    /**
     * 发送群发邮件
     *
     * @param massEmailRequest 群发邮件请求对象
     * @return 发送结果
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendMassEmail(@RequestBody MassEmailRequest massEmailRequest) {
        try {
            logger.info("收到群发邮件请求: {}", massEmailRequest);

            List<String> emailAddresses = massEmailRequest.getEmailAddresses();
            String subject = massEmailRequest.getSubject();
            String content = massEmailRequest.getContent();

            if (emailAddresses == null || emailAddresses.isEmpty()) {
                logger.warn("邮箱地址列表为空，无法发送群发邮件");
                return ResponseEntity.badRequest().body("邮箱地址列表不能为空");
            }

            // 统计发送结果
            int successCount = 0;
            int failCount = 0;

            // 遍历邮箱地址列表，逐个发送邮件
            for (String emailAddress : emailAddresses) {
                try {
                    boolean success = emailService.sendCustomEmail(
                            new EmailRequest(emailAddress, subject, content));

                    if (success) {
                        successCount++;
                        logger.debug("群发邮件发送成功: {}", emailAddress);
                    } else {
                        failCount++;
                        logger.warn("群发邮件发送失败: {}", emailAddress);
                    }
                } catch (Exception e) {
                    failCount++;
                    logger.error("群发邮件发送异常，收件人: {}", emailAddress, e);
                }
            }

            logger.info("群发邮件处理完成，总数: {}，成功: {}，失败: {}",
                    emailAddresses.size(), successCount, failCount);

            return ResponseEntity.ok(String.format("群发邮件处理完成，总数: %d，成功: %d，失败: %d",
                    emailAddresses.size(), successCount, failCount));
        } catch (Exception e) {
            logger.error("处理群发邮件请求失败: " + massEmailRequest, e);
            return ResponseEntity.status(500).body("处理群发邮件请求失败: " + e.getMessage());
        }
    }
}