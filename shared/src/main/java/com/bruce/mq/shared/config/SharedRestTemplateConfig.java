package com.bruce.mq.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 共享的RestTemplate配置类
 * 提供统一的RestTemplate Bean配置
 * 
 * @author BruceXuK
 */
@Configuration
public class SharedRestTemplateConfig {
    
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