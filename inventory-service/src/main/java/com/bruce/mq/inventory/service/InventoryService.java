package com.bruce.mq.inventory.service;

import com.bruce.mq.shared.inventory.model.Inventory;

/**
 * 库存服务接口
 * 定义库存相关业务操作的方法
 * 
 * @author BruceXuK
 */
public interface InventoryService {
    
    /**
     * 扣减库存
     * 
     * @param inventory 库存信息
     * @return 扣减结果，成功返回true，失败返回false
     */
    boolean deductInventory(Inventory inventory);
    
    /**
     * 根据商品ID获取库存
     * 
     * @param productId 商品ID
     * @return 库存信息
     */
    Inventory getInventoryByProductId(Long productId);
    
    /**
     * 保存库存信息
     * 
     * @param inventory 库存信息
     * @return 保存后的库存信息
     */
    Inventory save(Inventory inventory);
}