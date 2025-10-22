package com.bruce.mq.common.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 通用安全配置类
 */
@Configuration
@EnableWebSecurity
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {

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
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
                .and()
                // 启用 HTTP 基本认证
                .httpBasic();
    }
}