package com.bruce.mq.inventory.controller;

import com.bruce.mq.inventory.service.InventoryService;
import com.bruce.mq.shared.inventory.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存控制器
 * 提供库存查询和扣减的REST API接口
 * 
 * @author BruceXuK
 */
@RestController
@RequestMapping("/inventories")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * 扣减库存接口
     * 
     * @param inventory 库存信息
     * @return 扣减结果，成功返回true，失败返回false
     */
    @PostMapping("/deduct")
    public boolean deductInventory(@RequestBody Inventory inventory) {
        return inventoryService.deductInventory(inventory);
    }
    
    /**
     * 根据商品ID获取库存信息
     * 
     * @param productId 商品ID
     * @return 库存信息
     */
    @GetMapping("/product/{productId}")
    public Inventory getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }
}