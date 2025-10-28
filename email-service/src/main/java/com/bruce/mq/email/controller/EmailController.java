package com.bruce.mq.email.controller;

import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件服务控制器
 * 提供邮件发送的REST API接口
 * 
 * @author BruceXuK
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    /**
     * 发送自定义邮件
     *
     * @param emailRequest 邮件请求对象
     * @return 发送结果
     */
    @PostMapping("/send-custom")
    public ResponseEntity<Map<String, Object>> sendCustomEmail(@RequestBody EmailRequest emailRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean success = emailService.sendCustomEmail(emailRequest);

            if (success) {
                response.put("success", true);
                response.put("message", "邮件发送成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "邮件发送失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            logger.error("发送自定义邮件失败", e);
            response.put("success", false);
            response.put("message", "邮件发送异常: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}