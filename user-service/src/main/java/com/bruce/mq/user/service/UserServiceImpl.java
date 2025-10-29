package com.bruce.mq.user.service;

import com.bruce.mq.user.config.MailConfig;
import com.bruce.mq.user.repository.UserRepository;
import com.bruce.mq.shared.user.model.User;
import com.bruce.mq.shared.user.dto.UserRegisterRequest;
import com.bruce.mq.shared.user.service.UserService;
import com.bruce.mq.shared.email.model.EmailCode;
import com.bruce.mq.shared.email.model.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 用户服务实现类
 * 
 * @author BruceXuK
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private MailConfig mailConfig;

    /**
     * 用户注册
     * 
     * @param registerRequest 用户注册请求
     * @return 注册成功的用户
     */
    @Override
    public User register(UserRegisterRequest registerRequest) {
        // 检查两次输入的密码是否一致
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }

        // 检查邮箱是否已被注册
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("该邮箱已被注册");
        }

        // 验证验证码
        if (!verifyCode(registerRequest.getEmail(), registerRequest.getVerificationCode())) {
            throw new IllegalArgumentException("验证码错误或已过期");
        }

        // 创建用户
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        // 保存用户
        User savedUser = userRepository.save(user);

        // 验证成功后删除验证码
        userRepository.removeVerificationCode(registerRequest.getEmail());

        return savedUser;
    }

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱地址
     * @return 用户信息
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 检查邮箱是否已被注册
     * 
     * @param email 邮箱地址
     * @return 如果已被注册返回true，否则返回false
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 发送验证码到指定邮箱
     * 
     * @param email 邮箱地址
     * @return 发送的验证码
     */
    @Override
    public String sendVerificationCode(String email) {
        // 生成6位随机验证码
        String code = String.format("%06d", (int) (Math.random() * 1000000));

        // 保存验证码
        userRepository.saveVerificationCode(email, code);

        // 发送验证码邮件
        EmailCode emailCode = new EmailCode(email, code, "用户注册验证码",
                "您的注册验证码是：" + code + "，请在10分钟内使用。");
        
        // 通过HTTP调用邮件服务发送邮件
        sendEmailViaHttp(emailCode);
        log.info("已发送验证码邮件，邮箱: " + email + "，验证码: " + code);

        return code;
    }

    /**
     * 验证验证码是否正确
     * 
     * @param email 邮箱地址
     * @param code 验证码
     * @return 验证成功返回true，否则返回false
     */
    @Override
    public boolean verifyCode(String email, String code) {
        String storedCode = userRepository.getVerificationCode(email);
        return storedCode != null && storedCode.equals(code);
    }
    
    /**
     * 通过HTTP调用邮件服务发送邮件
     * 
     * @param emailCode 邮件验证码对象
     */
    private void sendEmailViaHttp(EmailCode emailCode) {
        try {
            String url = mailConfig.getEmailServiceUrl() + "/email/send-custom";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 转换为EmailRequest对象
            EmailRequest emailRequest = new EmailRequest(
                emailCode.getEmailAddress(),
                emailCode.getSubject(),
                emailCode.getContent()
            );
            
            HttpEntity<EmailRequest> request = new HttpEntity<>(emailRequest, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("通过HTTP调用邮件服务发送邮件失败，状态码: {}", response.getStatusCode());
                throw new RuntimeException("邮件服务调用失败，状态码: " + response.getStatusCode());
            }
            
            log.info("通过HTTP调用邮件服务发送邮件成功，收件人: {}, 主题: {}", 
                emailCode.getEmailAddress(), emailCode.getSubject());
        } catch (Exception e) {
            log.error("通过HTTP调用邮件服务发送邮件异常", e);
            throw new RuntimeException("邮件服务调用异常", e);
        }
    }
}