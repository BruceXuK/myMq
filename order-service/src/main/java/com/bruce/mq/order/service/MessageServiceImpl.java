package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.order.config.MailConfig;
import com.bruce.mq.shared.util.RetryUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.apache.rocketmq.common.message.MessageConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * 消息服务实现类
 * 实现消息发送相关操作
 *
 * @author BruceXuK
 */
@Service
public class MessageServiceImpl implements MessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${email-service.url:http://localhost:8088}")
    private String emailServiceUrl;

    @Autowired
    private MailConfig mailConfig;

    /**
     * 扣减库存操作
     * 通过消息队列通知库存服务进行库存扣减
     *
     * @param order 订单信息
     */
    @Override
    public void deductInventory(Order order) {
        // 通过RocketMQ发送消息到库存服务扣减库存
        try {
            RetryUtils.executeWithRetry(() -> {
                rocketMQTemplate.syncSend("inventory-topic:ORDER_CREATED",
                    MessageBuilder.withPayload(order)
                            .build());
                return null;
            }, 3, "发送库存扣减消息");
            logger.info("已通过RocketMQ发送消息通知库存服务扣减库存，订单号: {}", order.getOrderNo());
        } catch (Exception e) {
            logger.error("发送库存扣减消息失败，订单号: " + order.getOrderNo(), e);
        }
    }

    /**
     * 回滚库存操作
     * 通过消息队列通知库存服务进行库存回滚
     *
     * @param order 订单信息
     */
    @Override
    public void rollbackInventory(Order order) {
        // 通过RocketMQ发送消息到库存服务回滚库存
        try {
            RetryUtils.executeWithRetry(() -> {
                rocketMQTemplate.syncSend("inventory-topic",
                    MessageBuilder.withPayload(order).setHeader(MessageConst.PROPERTY_TAGS, "ORDER_CANCELLED").build());
                return null;
            }, 3, "发送库存回滚消息");
            logger.info("已通过RocketMQ发送消息通知库存服务回滚库存，订单号: {}", order.getOrderNo());
        } catch (Exception e) {
            logger.error("发送库存回滚消息失败，订单号: " + order.getOrderNo(), e);
        }
    }

    /**
     * 发送订单确认邮件
     * 通过HTTP调用邮件服务发送订单确认邮件
     * 注意：此邮件在订单创建后立即发送，无论是否支付
     *
     * @param order 订单信息
     */
    @Override
    public void sendOrderConfirmationEmail(Order order) {
        try {
            // 构造邮件内容
            String subject = "订单确认通知 - " + order.getOrderNo();
            String content = String.format(
                "尊敬的客户，您的订单已成功创建：\n\n" +
                "订单编号：%s\n" +
                "商品ID：%d\n" +
                "商品数量：%d\n" +
                "商品单价：%.2f\n" +
                "总金额：%.2f\n\n" +
                "请在1分钟内完成支付，否则订单将自动取消。\n" +
                "感谢您的购买！",
                order.getOrderNo(),
                order.getProductId(),
                order.getQuantity(),
                order.getPrice(),
                order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
            );

            // 创建邮件请求对象
            EmailRequest emailRequest = new EmailRequest(
                mailConfig.getDefaultToAddress(), // 实际应用中应从用户信息中获取
                subject,
                content
            );

            // 通过HTTP调用邮件服务发送邮件
            logger.info("准备通过HTTP调用邮件服务发送订单确认邮件，订单号: {}", order.getOrderNo());
            RetryUtils.executeWithRetry(() -> {
                sendEmailViaHttp(emailRequest);
                return null;
            }, 3, "发送订单确认邮件");
            logger.info("已通过HTTP调用邮件服务发送订单确认邮件，订单号: {}", order.getOrderNo());
        } catch (Exception e) {
            logger.error("发送订单确认邮件失败，订单号: " + order.getOrderNo(), e);
        }
    }

    /**
     * 发送订单超时检查延迟消息
     * 发送延迟消息用于检查订单是否超时未支付
     *
     * @param order 订单信息
     * @param delayLevel 延迟级别 (1-18)
     */
    @Override
    public void sendOrderTimeoutCheckMessage(Order order, int delayLevel) {
        try {
            // 通过RocketMQ发送延迟消息，delayLevel指定延迟时间
            RetryUtils.executeWithRetry(() -> {
                rocketMQTemplate.syncSend("order-timeout-topic",
                    MessageBuilder.withPayload(order).build(),
                    3000, delayLevel);
                return null;
            }, 3, "发送订单超时检查延迟消息");
            logger.info("已发送订单超时检查延迟消息，订单号: {}，延迟级别: {}", order.getOrderNo(), delayLevel);
        } catch (Exception e) {
            logger.error("发送订单超时检查延迟消息失败，订单号: " + order.getOrderNo(), e);
        }
    }

    /**
     * 发送订单超时取消通知邮件
     * 通过HTTP调用邮件服务发送订单超时取消通知邮件
     * 此方法在订单超时未支付被取消后调用，通知用户订单已被取消
     *
     * @param order 订单信息
     */
    @Override
    public void sendOrderTimeoutNotificationEmail(Order order) {
        // 记录尝试发送邮件的日志
        logger.info("准备发送订单超时取消通知邮件，订单号: {}", order.getOrderNo());

        try {
            // 构造邮件内容
            String subject = "订单超时取消通知 - " + order.getOrderNo();
            String content = String.format(
                "尊敬的客户，您的订单由于超时未支付已被自动取消：\n\n" +
                "订单编号：%s\n" +
                "商品ID：%d\n" +
                "商品数量：%d\n" +
                "商品单价：%.2f\n" +
                "总金额：%.2f\n\n" +
                "您在创建订单后1分钟内未完成支付，系统已自动取消该订单。\n" +
                "如需购买，请重新下单。\n" +
                "感谢您的理解！",
                order.getOrderNo(),
                order.getProductId(),
                order.getQuantity(),
                order.getPrice(),
                order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
            );

            // 创建邮件请求对象
            EmailRequest emailRequest = new EmailRequest(
                mailConfig.getDefaultToAddress(), // 实际应用中应从用户信息中获取
                subject,
                content
            );

            // 通过HTTP调用邮件服务发送邮件
            logger.info("准备通过HTTP调用邮件服务发送订单超时取消通知邮件，订单号: {}", order.getOrderNo());
            RetryUtils.executeWithRetry(() -> {
                sendEmailViaHttp(emailRequest);
                return null;
            }, 3, "发送订单超时取消通知邮件");
            logger.info("已通过HTTP调用邮件服务发送订单超时取消通知邮件，订单号: {}", order.getOrderNo());
        } catch (Exception e) {
            logger.error("发送订单超时取消通知邮件失败，订单号: {}", order.getOrderNo(), e);
        }
    }
    
    /**
     * 发送支付成功通知邮件
     * 
     * @param order 订单信息
     */
    @Override
    public void sendPaymentSuccessEmail(Order order) {
        try {
            // 构造邮件内容
            String subject = "订单支付成功通知 - " + order.getOrderNo();
            String content = String.format(
                "尊敬的客户，您的订单已成功支付：\n\n" +
                "订单编号：%s\n" +
                "商品ID：%d\n" +
                "商品数量：%d\n" +
                "商品单价：%.2f\n" +
                "总金额：%.2f\n\n" +
                "我们将在24小时内为您发货，请耐心等待。\n" +
                "感谢您的购买！",
                order.getOrderNo(),
                order.getProductId(),
                order.getQuantity(),
                order.getPrice(),
                order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
            );

            // 创建邮件请求对象
            EmailRequest emailRequest = new EmailRequest(
                mailConfig.getDefaultToAddress(), // 实际应用中应从用户信息中获取
                subject,
                content
            );

            // 通过HTTP调用邮件服务发送邮件
            logger.info("准备通过HTTP调用邮件服务发送支付成功通知邮件，订单号: {}", order.getOrderNo());
            RetryUtils.executeWithRetry(() -> {
                sendEmailViaHttp(emailRequest);
                return null;
            }, 3, "发送支付成功通知邮件");
            logger.info("已通过HTTP调用邮件服务发送支付成功通知邮件，订单号: {}", order.getOrderNo());
        } catch (Exception e) {
            logger.error("发送支付成功通知邮件失败，订单号: " + order.getOrderNo(), e);
        }
    }
    
    /**
     * 通过HTTP调用邮件服务发送邮件
     * 
     * @param emailRequest 邮件请求对象
     */
    private void sendEmailViaHttp(EmailRequest emailRequest) {
        try {
            String url = emailServiceUrl + "/email/send-custom";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<EmailRequest> request = new HttpEntity<>(emailRequest, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("通过HTTP调用邮件服务发送邮件失败，状态码: {}", response.getStatusCode());
                throw new RuntimeException("邮件服务调用失败，状态码: " + response.getStatusCode());
            }
            
            logger.info("通过HTTP调用邮件服务发送邮件成功，收件人: {}, 主题: {}", 
                emailRequest.getEmailAddress(), emailRequest.getSubject());
        } catch (Exception e) {
            logger.error("通过HTTP调用邮件服务发送邮件异常", e);
            throw new RuntimeException("邮件服务调用异常", e);
        }
    }
}