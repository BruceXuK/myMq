package com.bruce.mq.order.repository;

import com.bruce.mq.shared.order.model.Order;
import org.springframework.stereotype.Repository;

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
}
