package com.bruce.mq.user;

import com.bruce.mq.shared.util.StartupTimeTracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 *
 * @author BruceXuK
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bruce.mq.user", "com.bruce.mq.shared"})
public class UserApplication {

    /**
     * 主函数，启动Spring Boot应用
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UserApplication.class, args);
        
        // 打印启动时间报告
        StartupTimeTracker startupTimeTracker = context.getBean(StartupTimeTracker.class);
        startupTimeTracker.printReport();
    }

}