package com.bruce.mq.inventory;

import com.bruce.mq.shared.util.StartupTimeTracker;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.converter.StringMessageConverter;

/**
 * 库存服务启动类
 * 负责处理库存查询、扣减等操作
 *
 * @author BruceXuK
 */
@SpringBootApplication(scanBasePackages = {"com.bruce.mq.inventory", "com.bruce.mq.shared"})
@EnableDiscoveryClient
public class InventoryApplication {

    /**
     * 主函数，启动Spring Boot应用
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(InventoryApplication.class, args);
        
        // 打印启动时间报告
        StartupTimeTracker startupTimeTracker = context.getBean(StartupTimeTracker.class);
        startupTimeTracker.printReport();
    }
}