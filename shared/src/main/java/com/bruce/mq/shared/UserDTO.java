package com.bruce.mq.shared;

/**
 * 用户DTO类
 */
public class UserDTO {
    /** 用户ID */
    private Long id;
    
    /** 用户名 */
    private String name;
    
    /** 邮箱地址 */
    private String email;
    
    /** 手机号码 */
    private String phone;
    
    /**
     * 默认构造函数
     */
    public UserDTO() {}
    
    /**
     * 带参数的构造函数
     * 
     * @param id 用户ID
     * @param name 用户名
     * @param email 邮箱地址
     * @param phone 手机号码
     */
    public UserDTO(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置用户ID
     * 
     * @param id 用户ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取用户名
     * 
     * @return 用户名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置用户名
     * 
     * @param name 用户名
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取邮箱地址
     * 
     * @return 邮箱地址
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 设置邮箱地址
     * 
     * @param email 邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 获取手机号码
     * 
     * @return 手机号码
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * 设置手机号码
     * 
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}