package com.bruce.mq.shared.email.client;

import com.bruce.mq.shared.email.model.MassEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 群发邮件客户端
 * 通过API网关调用邮箱服务的群发邮件API
 *
 * @author BruceXuK
 */
@Component
public class MassEmailClient {

    private static final Logger logger = LoggerFactory.getLogger(MassEmailClient.class);

    @Value("${gateway.url:http://localhost:8092}")
    private String gatewayUrl;

    private final RestTemplate restTemplate;

    public MassEmailClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 发送群发邮件
     *
     * @param emailAddresses 邮箱地址列表
     * @param subject        邮件主题
     * @param content        邮件内容
     * @return 发送结果
     */
    public String sendMassEmail(List<String> emailAddresses, String subject, String content) {
        try {
            String url = gatewayUrl + "/api/email-service/mass-emails/send";
            
            MassEmailRequest massEmailRequest = new MassEmailRequest(emailAddresses, subject, content);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<MassEmailRequest> request = new HttpEntity<>(massEmailRequest, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            logger.info("群发邮件发送完成，响应: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            logger.error("发送群发邮件失败", e);
            return "发送群发邮件失败: " + e.getMessage();
        }
    }

    /**
     * 发送群发邮件
     *
     * @param massEmailRequest 群发邮件请求对象
     * @return 发送结果
     */
    public String sendMassEmail(MassEmailRequest massEmailRequest) {
        return sendMassEmail(massEmailRequest.getEmailAddresses(), 
                           massEmailRequest.getSubject(), 
                           massEmailRequest.getContent());
    }
}