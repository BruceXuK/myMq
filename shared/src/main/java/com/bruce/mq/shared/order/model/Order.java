package com.bruce.mq.shared.order.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 订单实体类
 * 用于表示系统中的订单信息
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    /** 订单ID */
    private Long id;
    
    /** 订单编号，不能为空 */
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
    
    /** 商品ID，不能为空且必须大于0 */
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    /** 商品数量，不能为空且必须大于0 */
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
    
    /** 商品价格，不能为空且必须大于0 */
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;
    
    /** 订单状态：PENDING(待处理), CONFIRMED(已确认), CANCELLED(已取消) */
    private String status;
    

    
    /**
     * 带参数的构造函数
     * 
     * @param orderNo 订单编号
     * @param productId 商品ID
     * @param quantity 商品数量
     * @param price 商品价格
     */
    public Order(String orderNo, Long productId, Integer quantity, BigDecimal price) {
        this.orderNo = orderNo;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.status = "PENDING";
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}