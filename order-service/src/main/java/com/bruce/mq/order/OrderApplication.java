package com.bruce.mq.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单服务启动类
 * 负责处理订单创建、查询等操作
 *
 * @author BruceXuK
 */
@SpringBootApplication(scanBasePackages = {"com.bruce.mq.order", "com.bruce.mq.shared"})
@EnableDiscoveryClient
public class OrderApplication {

    /**
     * 主函数，启动Spring Boot应用
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}