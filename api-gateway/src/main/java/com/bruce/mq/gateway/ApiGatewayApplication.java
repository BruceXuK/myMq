package com.bruce.mq.gateway;

import com.bruce.mq.shared.util.StartupTimeTracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * API网关应用主类
 *
 * @author BruceXuK
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        
        // 打印启动时间报告
        StartupTimeTracker startupTimeTracker = context.getBean(StartupTimeTracker.class);
        startupTimeTracker.printReport();
    }

}