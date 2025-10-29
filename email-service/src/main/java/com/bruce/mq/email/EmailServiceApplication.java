package com.bruce.mq.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 邮件服务主应用类
 * 启动邮件服务应用
 *
 * @author BruceXuK
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.bruce.mq.email", "com.bruce.mq.shared"})
public class EmailServiceApplication {

    /**
     * 应用启动入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EmailServiceApplication.class, args);
    }
}