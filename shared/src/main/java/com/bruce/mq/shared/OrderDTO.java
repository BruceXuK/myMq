package com.bruce.mq.shared;

import java.math.BigDecimal;

/**
 * 订单DTO类
 */
public class OrderDTO {
    /** 订单ID */
    private Long id;
    
    /** 商品名称 */
    private String product;
    
    /** 商品数量 */
    private Integer quantity;
    
    /** 商品价格 */
    private BigDecimal price;
    
    /** 用户ID */
    private Long userId;
    
    /**
     * 默认构造函数
     */
    public OrderDTO() {}
    
    /**
     * 带参数的构造函数
     * 
     * @param id 订单ID
     * @param product 商品名称
     * @param quantity 商品数量
     * @param price 商品价格
     * @param userId 用户ID
     */
    public OrderDTO(Long id, String product, Integer quantity, BigDecimal price, Long userId) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.userId = userId;
    }
    
    // Getters and Setters
    /**
     * 获取订单ID
     * 
     * @return 订单ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置订单ID
     * 
     * @param id 订单ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取商品名称
     * 
     * @return 商品名称
     */
    public String getProduct() {
        return product;
    }
    
    /**
     * 设置商品名称
     * 
     * @param product 商品名称
     */
    public void setProduct(String product) {
        this.product = product;
    }
    
    /**
     * 获取商品数量
     * 
     * @return 商品数量
     */
    public Integer getQuantity() {
        return quantity;
    }
    
    /**
     * 设置商品数量
     * 
     * @param quantity 商品数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    /**
     * 获取商品价格
     * 
     * @return 商品价格
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * 设置商品价格
     * 
     * @param price 商品价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     * 
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", userId=" + userId +
                '}';
    }
}