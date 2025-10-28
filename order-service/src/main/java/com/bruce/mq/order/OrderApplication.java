package com.bruce.mq.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 订单服务启动类
 * 负责处理订单创建、查询等操作
 * 
 * @author BruceXuK
 */
@SpringBootApplication
public class OrderApplication {

    /**
     * 主函数，启动Spring Boot应用
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
    
    /**
     * 创建RestTemplate Bean用于服务间调用
     * 
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}