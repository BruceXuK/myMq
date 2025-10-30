package com.bruce.mq.email;

import com.bruce.mq.shared.util.StartupTimeTracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 邮件服务主应用类
 * 启动邮件服务应用
 *
 * @author BruceXuK
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bruce.mq.email", "com.bruce.mq.shared"})
public class EmailServiceApplication {

    /**
     * 应用启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EmailServiceApplication.class, args);
        
        // 打印启动时间报告
        StartupTimeTracker startupTimeTracker = context.getBean(StartupTimeTracker.class);
        startupTimeTracker.printReport();
    }
}