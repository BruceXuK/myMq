package com.bruce.mq.inventory.repository;

import com.bruce.mq.shared.inventory.model.Inventory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 库存数据访问仓库
 * 负责库存数据的存储和访问
 * 
 * @author BruceXuK
 */
@Repository
public class InventoryRepository {
    
    /** 存储所有库存的并发安全Map，以商品ID为键 */
    private final ConcurrentHashMap<Long, Inventory> inventories = new ConcurrentHashMap<>();
    
    /** 库存ID生成器 */
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * 初始化方法
     * 在仓库创建时初始化一些测试数据
     */
    @PostConstruct
    public void init() {
        // 初始化一些测试数据
        Inventory inventory1 = new Inventory(1L, 100, new BigDecimal("99.99"));
        inventory1.setId(idGenerator.getAndIncrement());
        inventories.put(inventory1.getProductId(), inventory1);
        
        Inventory inventory2 = new Inventory(2L, 200, new BigDecimal("199.99"));
        inventory2.setId(idGenerator.getAndIncrement());
        inventories.put(inventory2.getProductId(), inventory2);
        
        // 添加商品ID为1001的库存数据，用于测试
        Inventory inventory3 = new Inventory(1001L, 50, new BigDecimal("0.00"));
        inventory3.setId(idGenerator.getAndIncrement());
        inventories.put(inventory3.getProductId(), inventory3);
    }
    
    /**
     * 扣减库存
     * 
     * @param inventory 库存信息
     * @return 扣减结果，成功返回true，失败返回false
     */
    public boolean deductInventory(Inventory inventory) {
        Inventory existingInventory = inventories.get(inventory.getProductId());
        if (existingInventory == null) {
            System.err.println("商品库存不存在，商品ID: " + inventory.getProductId());
            return false;
        }
        
        // 检查库存是否充足
        if (existingInventory.getQuantity() < inventory.getQuantity()) {
            System.err.println("商品库存不足，商品ID: " + inventory.getProductId() + 
                              "，当前库存: " + existingInventory.getQuantity() + 
                              "，需扣减: " + inventory.getQuantity());
            return false;
        }
        
        // 扣减库存
        existingInventory.setQuantity(existingInventory.getQuantity() - inventory.getQuantity());
        System.out.println("成功扣减库存，商品ID: " + inventory.getProductId() + 
                          "，扣减数量: " + inventory.getQuantity() + 
                          "，剩余库存: " + existingInventory.getQuantity());
        return true;
    }
    
    /**
     * 增加库存
     * 
     * @param inventory 库存信息
     * @return 增加结果，成功返回true，失败返回false
     */
    public boolean addInventory(Inventory inventory) {
        Inventory existingInventory = inventories.get(inventory.getProductId());
        if (existingInventory == null) {
            System.err.println("商品库存不存在，商品ID: " + inventory.getProductId());
            return false;
        }
        
        // 增加库存
        existingInventory.setQuantity(existingInventory.getQuantity() + inventory.getQuantity());
        System.out.println("成功增加库存，商品ID: " + inventory.getProductId() + 
                          "，增加数量: " + inventory.getQuantity() + 
                          "，当前库存: " + existingInventory.getQuantity());
        return true;
    }
    
    /**
     * 根据商品ID查找库存
     * 
     * @param productId 商品ID
     * @return 库存信息
     */
    public Inventory findByProductId(Long productId) {
        return inventories.get(productId);
    }
    
    /**
     * 保存库存
     * 
     * @param inventory 库存信息
     * @return 保存后的库存信息
     */
    public Inventory save(Inventory inventory) {
        if (inventory.getId() == null) {
            inventory.setId(idGenerator.getAndIncrement());
        }
        inventories.put(inventory.getProductId(), inventory);
        return inventory;
    }
}