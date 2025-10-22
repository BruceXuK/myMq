package com.bruce.mq.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 邮件服务启动类
 * 
 * @author BruceXuK
 */
@SpringBootApplication(scanBasePackages = "com.bruce.mq.email")
public class EmailServiceApplication {

    /**
     * 主函数，启动Spring Boot应用
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EmailServiceApplication.class, args);
    }

}