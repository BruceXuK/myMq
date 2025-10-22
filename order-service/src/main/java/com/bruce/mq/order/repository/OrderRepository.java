package com.bruce.mq.order.repository;

import com.bruce.mq.shared.order.model.Order;
import com.bruce.mq.shared.email.model.EmailRequest;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单数据访问仓库
 * 负责订单数据的存储和访问
 * 
 * @author BruceXuK
 */
@Repository
public class OrderRepository {

    /** 存储所有订单的并发安全Map */
    private final ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();

    /** 订单ID生成器 */
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 保存订单
     *
     * @param order 订单信息
     * @return 保存后的订单
     */
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
        }
        orders.put(order.getId(), order);
        return order;
    }

    /**
     * 根据ID查找订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    public Order findById(Long id) {
        return orders.get(id);
    }

    /**
     * 扣减库存操作
     * 通过消息队列通知库存服务进行库存扣减
     *
     * @param order 订单信息
     */
    public void deductInventory(Order order) {
        // 通过RocketMQ发送消息到库存服务扣减库存
        try {
            rocketMQTemplate.send("order-topic:ORDER_CREATED",
                MessageBuilder.withPayload(order).build());
            System.out.println("已通过RocketMQ发送消息通知库存服务扣减库存，订单号: " + order.getOrderNo());
        } catch (Exception e) {
            System.err.println("发送库存扣减消息失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
        }
    }

    /**
     * 发送订单确认邮件
     * 通过消息队列通知邮件服务发送订单确认邮件
     *
     * @param order 订单信息
     */
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
                "感谢您的购买！",
                order.getOrderNo(),
                order.getProductId(),
                order.getQuantity(),
                order.getPrice(),
                order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
            );

            // 创建邮件请求对象
            EmailRequest emailRequest = new EmailRequest(
                "1534975668@qq.com", // 实际应用中应从用户信息中获取
                subject,
                content
            );

            // 通过RocketMQ发送邮件消息
            rocketMQTemplate.send("email-topic:CUSTOM_EMAIL",
                MessageBuilder.withPayload(emailRequest).build());
            System.out.println("已通过RocketMQ发送订单确认邮件，订单号: " + order.getOrderNo());
        } catch (Exception e) {
            System.err.println("发送订单确认邮件失败，订单号: " + order.getOrderNo() + "，错误: " + e.getMessage());
        }
    }
}
