package com.bruce.mq.shared.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 库存实体类
 * 用于表示商品库存信息
 * 
 * @author BruceXuK
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    /** 库存ID */
    private Long id;
    
    /** 商品ID，不能为空且必须大于0 */
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    /** 商品数量，不能为空且必须大于0 */
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
    
    /** 商品价格 */
    private BigDecimal price;
    

    
    /**
     * 带参数的构造函数
     * 
     * @param productId 商品ID
     * @param quantity 商品数量
     * @param price 商品价格
     */
    public Inventory(Long productId, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}