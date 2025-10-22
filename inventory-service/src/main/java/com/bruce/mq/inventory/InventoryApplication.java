package com.bruce.mq.inventory;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.StringMessageConverter;

/**
 * 库存服务启动类
 * 负责处理库存查询、扣减等操作
 * 
 * @author BruceXuK
 */
@SpringBootApplication(scanBasePackages = "com.bruce.mq.inventory")
public class InventoryApplication {

    /**
     * 主函数，启动Spring Boot应用
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

}