package com.bruce.mq.email.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 邮件服务安全配置类
 * 
 * @author BruceXuK
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class EmailSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置HTTP安全策略
     * 
     * @param http HttpSecurity对象
     * @throws Exception 配置异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 CSRF 保护，因为这是一个微服务
        http.csrf().disable()
                // 禁用 frame 嵌套，防止点击劫持
                .headers().frameOptions().disable()
                .and()
                // 对所有请求进行认证
                .authorizeRequests()
                // 允许访问 Actuator endpoints
                .antMatchers("/actuator/**").permitAll()
                // 允许访问邮件相关接口
                .antMatchers("/email/**").permitAll()
                // 允许其他所有请求
                .anyRequest().permitAll()
                .and()
                // 禁用 HTTP 基本认证以避免401问题
                .httpBasic().disable()
                // 禁用表单登录以避免重定向到登录页面
                .formLogin().disable()
                // 禁用登出功能
                .logout().disable();
    }
}