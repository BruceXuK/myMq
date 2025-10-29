package com.bruce.mq.inventory.service;

import com.bruce.mq.shared.inventory.model.Inventory;
import com.bruce.mq.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库存服务实现类
 * 实现库存查询、扣减等业务逻辑
 * 
 * @author BruceXuK
 */
@Service
public class InventoryServiceImpl implements InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    /**
     * 扣减库存
     * 
     * @param inventory 库存信息
     * @return 扣减结果，成功返回true，失败返回false
     */
    @Override
    public boolean deductInventory(Inventory inventory) {
        return inventoryRepository.deductInventory(inventory);
    }
    
    /**
     * 根据商品ID获取库存
     * 
     * @param productId 商品ID
     * @return 库存信息
     */
    @Override
    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }
    
    /**
     * 保存库存信息
     * 
     * @param inventory 库存信息
     * @return 保存后的库存信息
     */
    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    
    /**
     * 增加库存
     * 
     * @param inventory 库存信息
     * @return 增加结果，成功返回true，失败返回false
     */
    @Override
    public boolean addInventory(Inventory inventory) {
        return inventoryRepository.addInventory(inventory);
    }
    
    /**
     * 恢复库存（订单取消时调用）
     * 
     * @param productId 商品ID
     * @param quantity 恢复的数量
     * @return 恢复结果，成功返回true，失败返回false
     */
    @Override
    public boolean restoreInventory(Long productId, Integer quantity) {
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantity(quantity);
        return inventoryRepository.addInventory(inventory);
    }
}