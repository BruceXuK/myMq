package com.bruce.mq.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 *
 * @author BruceXuK
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.bruce.mq.user", "com.bruce.mq.shared"})
public class UserApplication {

    /**
     * 主函数，启动Spring Boot应用
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}