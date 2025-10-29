package com.bruce.mq.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 用户服务安全配置类
 * 允许所有请求匿名访问
 */
@Configuration
@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 CSRF 保护
        http.csrf().disable()
                // 禁用 frame 嵌套
                .headers().frameOptions().disable()
                .and()
                // 允许所有请求匿名访问
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                // 禁用表单登录
                .formLogin().disable()
                // 禁用HTTP基本认证
                .httpBasic().disable();
    }
}