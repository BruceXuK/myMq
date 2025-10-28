package com.bruce.mq.order.service;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.shared.email.model.EmailRequest;
import com.bruce.mq.order.config.MailConfig;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.apache.rocketmq.common.message.MessageConst;

import java.math.BigDecimal;

/**
 * 消息服务实现类
 * 实现消息发送相关操作
 *
 * @author BruceXuK
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

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
            rocketMQTemplate.syncSend("inventory-topic:ORDER_CREATED",
                MessageBuilder.withPayload(order)
                        .build());
            System.out.println("已通过RocketMQ发送消息通知库存服务扣减库存，订单号: " + order.getOrderNo());
        } catch (Exception e) {
            System.err.println("发送库存扣减消息失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
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
            rocketMQTemplate.syncSend("inventory-topic",
                MessageBuilder.withPayload(order).setHeader(MessageConst.PROPERTY_TAGS, "ORDER_CANCELLED").build());
            System.out.println("已通过RocketMQ发送消息通知库存服务回滚库存，订单号: " + order.getOrderNo());
        } catch (Exception e) {
            System.err.println("发送库存回滚消息失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
        }
    }

    /**
     * 发送订单确认邮件
     * 通过消息队列通知邮件服务发送订单确认邮件
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

            // 通过RocketMQ发送邮件消息
            System.out.println("准备通过RocketMQ发送订单确认邮件，订单号: " + order.getOrderNo());
            rocketMQTemplate.send("order-topic:CUSTOM_EMAIL",
                MessageBuilder.withPayload(emailRequest).build());
            System.out.println("已通过RocketMQ发送订单确认邮件，订单号: " + order.getOrderNo());
        } catch (Exception e) {
            System.err.println("发送订单确认邮件失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
            e.printStackTrace(); // 打印完整的异常堆栈信息
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
            // RocketMQ支持18个延迟级别: 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            // 这里使用第5级，延迟1分钟
            rocketMQTemplate.syncSend("order-timeout-topic",
                MessageBuilder.withPayload(order).build(),
                3000, delayLevel);
            System.out.println("已发送订单超时检查延迟消息，订单号: " + order.getOrderNo() + "，延迟级别: " + delayLevel);
        } catch (Exception e) {
            System.err.println("发送订单超时检查延迟消息失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送订单超时取消通知邮件
     * 通过消息队列通知邮件服务发送订单超时取消通知邮件
     * 此方法在订单超时未支付被取消后调用，通知用户订单已被取消
     *
     * @param order 订单信息
     */
    @Override
    public void sendOrderTimeoutNotificationEmail(Order order) {
        // 记录尝试发送邮件的日志
        System.out.println("准备发送订单超时取消通知邮件，订单号: " + order.getOrderNo());

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

            // 最多重试3次发送邮件
            Exception lastException = null;
            boolean success = false;
            for (int attempt = 1; attempt <= 3 && !success; attempt++) {
                try {
                    // 通过RocketMQ发送邮件消息，使用ORDER_TIMEOUT_CHECK标签
                    System.out.println("准备通过RocketMQ发送订单超时取消通知邮件，订单号: " + order.getOrderNo() + "，尝试次数: " + attempt);
                    rocketMQTemplate.syncSend("order-topic:ORDER_TIMEOUT_CHECK",
                        MessageBuilder.withPayload(emailRequest).build(),3000, 5 );
                    System.out.println("已通过RocketMQ发送订单超时取消通知邮件，订单号: " + order.getOrderNo() + "，尝试次数: " + attempt);
                    success = true; // 发送成功
                } catch (Exception e) {
                    lastException = e;
                    System.err.println("第" + attempt + "次发送订单超时取消通知邮件失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
                    e.printStackTrace(); // 打印完整的异常堆栈信息

                    // 如果不是最后一次尝试，等待一段时间再重试
                    if (attempt < 3) {
                        try {
                            Thread.sleep(1000 * attempt); // 等待1秒、2秒...
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            System.err.println("发送订单超时取消通知邮件重试被中断: " + ie.getMessage());
                            break;
                        }
                    }
                }
            }

            if (!success) {
                // 所有重试都失败了
                System.err.println("发送订单超时取消通知邮件最终失败，订单号: " + order.getOrderNo() + "，最后错误: " + lastException.getMessage());
            }
        } catch (Exception e) {
            System.err.println("发送订单超时取消通知邮件失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
            e.printStackTrace(); // 打印完整的异常堆栈信息
        }
    }
}
