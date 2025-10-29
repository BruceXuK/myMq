package com.bruce.mq.shared.config;

import org.springframework.context.annotation.Configuration;

/**
 * Actuator监控配置说明类
 * 
 * 所有服务的Actuator配置应该保持一致，标准配置如下：
 * 
 * management:
 *   endpoints:
 *     web:
 *       exposure:
 *         include: "*"
 * 
 * 这样的配置会暴露所有Actuator端点，便于监控和管理。
 */
@Configuration
public class ActuatorConfig {
    
    // 此类仅用于文档说明目的
    // 实际配置在各服务的application.yml中定义
    
}