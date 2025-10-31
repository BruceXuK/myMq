package com.bruce.mq.shared.user.service;

import com.bruce.mq.shared.user.model.User;
import com.bruce.mq.shared.user.dto.UserRegisterRequest;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author BruceXuK
 */
public interface UserService {
    
    /**
     * 用户注册
     * 
     * @param registerRequest 用户注册请求
     * @return 注册成功的用户信息
     */
    User register(UserRegisterRequest registerRequest);
    
    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱地址
     * @return 用户信息，如果不存在返回null
     */
    User findByEmail(String email);
    
    /**
     * 检查邮箱是否已被注册
     * 
     * @param email 邮箱地址
     * @return 如果已被注册返回true，否则返回false
     */
    boolean existsByEmail(String email);
    
    /**
     * 发送验证码到指定邮箱
     * 
     * @param email 邮箱地址
     * @return 发送的验证码
     */
    String sendVerificationCode(String email);
    
    /**
     * 验证验证码是否正确
     * 
     * @param email 邮箱地址
     * @param code 验证码
     * @return 验证成功返回true，否则返回false
     */
    boolean verifyCode(String email, String code);
    
    /**
     * 获取所有用户的邮箱地址
     *
     * @return 所有用户的邮箱地址列表
     */
    List<String> getAllEmails();
}