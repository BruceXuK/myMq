package com.bruce.mq.email.service;

import com.bruce.mq.shared.email.model.EmailCode;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.shared.email.enums.EmailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务类
 *
 * @author BruceXuK
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送邮件验证码
     *
     * @param emailCode 邮件验证码对象
     * @return 是否发送成功
     */
    public boolean sendEmailCode(EmailCode emailCode) {
        logger.debug("开始发送验证码邮件: {}", emailCode);
        return sendEmail(emailCode.getEmailAddress(), emailCode.getSubject(), emailCode.getContent(), EmailType.VERIFICATION_CODE);
    }

    /**
     * 发送自定义邮件
     *
     * @param emailRequest 邮件请求对象
     * @return 是否发送成功
     */
    public boolean sendCustomEmail(EmailRequest emailRequest) {
        boolean result = sendEmail(emailRequest.getEmailAddress(), emailRequest.getSubject(), emailRequest.getContent(), EmailType.CUSTOM);
        return result;
    }

    /**
     * 发送邮件的通用方法
     *
     * @param to 收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param type 邮件类型
     * @return 是否发送成功
     */
    private boolean sendEmail(String to, String subject, String content, EmailType type) {
        Exception lastException = null;

        // 记录邮件发送尝试
        logger.info("开始发送{}到: {}，主题: {}", type.getDescription(), to, subject);

        // 检查必要参数
        if (to == null || to.isEmpty()) {
            logger.error("收件人邮箱地址为空，无法发送{}邮件", type.getDescription());
            return false;
        }

        if (subject == null || subject.isEmpty()) {
            logger.error("邮件主题为空，无法发送{}邮件到: {}", type.getDescription(), to);
            return false;
        }

        if (content == null || content.isEmpty()) {
            logger.error("邮件内容为空，无法发送{}邮件到: {}", type.getDescription(), to);
            return false;
        }

        // 检查邮件发送服务是否配置正确
        if (fromEmail == null || fromEmail.isEmpty()) {
            logger.error("发件人邮箱地址未配置，无法发送{}邮件到: {}", type.getDescription(), to);
            return false;
        }

        // 尝试发送邮件，最多重试3次
        for (int i = 0; i < 3; i++) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content);

                logger.debug("正在发送邮件 - 收件人: {}, 主题: {}, 内容长度: {}", to, subject, content.length());

                javaMailSender.send(message);
                logger.info("{}发送成功: {} (尝试次数: {})，主题: {}", type.getDescription(), to, i + 1, subject);
                return true;
            } catch (MailAuthenticationException e) {
                lastException = e;
                logger.error("{}发送失败: {}，身份验证错误，请检查邮箱用户名和密码", type.getDescription(), to, e);
                // 身份验证错误不需要重试
                break;
            } catch (MailSendException e) {
                lastException = e;
                logger.error("{}发送失败: {}，网络或服务器错误: {}", type.getDescription(), to, e.getMessage());
                logger.debug("网络或服务器错误详情", e);
                // 网络或服务器错误可以重试
            } catch (Exception e) {
                lastException = e;
                logger.warn("{}发送失败: {} (尝试次数: {})，主题: {}", type.getDescription(), to, i + 1, subject, e);
                logger.debug("发送异常详情", e);
                try {
                    // 等待一段时间再重试
                    Thread.sleep(1000 * (i + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.warn("{}发送重试被中断", type.getDescription(), ie);
                    break;
                }
            }
        }

        logger.error("{}发送最终失败: {}，主题: {}", type.getDescription(), to, subject, lastException);
        return false;
    }
}
