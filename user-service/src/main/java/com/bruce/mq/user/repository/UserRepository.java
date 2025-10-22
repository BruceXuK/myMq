package com.bruce.mq.user.repository;

import com.bruce.mq.shared.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户仓库 - 使用内存存储模拟数据库存储
 * 在生产环境中，应该使用真实的数据库存储
 * 
 * @author BruceXuK
 */
@Repository
public class UserRepository {
    
    // 使用内存存储模拟数据库，实际项目中应使用数据库存储
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    
    // 验证码存储，实际项目中应使用Redis等缓存存储
    private final Map<String, String> verificationCodeStore = new ConcurrentHashMap<>();
    
    /**
     * 保存用户
     * 
     * @param user 用户实体
     * @return 保存后的用户实体
     */
    public User save(User user) {
        userStore.put(user.getEmail(), user);
        return user;
    }
    
    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱地址
     * @return 用户实体，如果不存在返回null
     */
    public User findByEmail(String email) {
        return userStore.get(email);
    }
    
    /**
     * 检查邮箱是否已被注册
     * 
     * @param email 邮箱地址
     * @return 如果已被注册返回true，否则返回false
     */
    public boolean existsByEmail(String email) {
        return userStore.containsKey(email);
    }
    
    /**
     * 保存验证码
     * 
     * @param email 邮箱地址
     * @param code 验证码
     */
    public void saveVerificationCode(String email, String code) {
        verificationCodeStore.put(email, code);
    }
    
    /**
     * 获取验证码
     * 
     * @param email 邮箱地址
     * @return 验证码
     */
    public String getVerificationCode(String email) {
        return verificationCodeStore.get(email);
    }
    
    /**
     * 删除验证码（验证成功后删除）
     * 
     * @param email 邮箱地址
     */
    public void removeVerificationCode(String email) {
        verificationCodeStore.remove(email);
    }
}