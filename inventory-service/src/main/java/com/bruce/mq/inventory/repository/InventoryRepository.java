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
        Inventory existing = inventories.get(inventory.getProductId());
        if (existing != null && existing.getQuantity() >= inventory.getQuantity()) {
            existing.setQuantity(existing.getQuantity() - inventory.getQuantity());
            System.out.println("成功扣减库存: 商品ID=" + inventory.getProductId() + ", 扣减数量=" + inventory.getQuantity());
            return true;
        } else {
            System.out.println("库存不足: 商品ID=" + inventory.getProductId() + ", 当前库存=" + (existing != null ? existing.getQuantity() : 0) + ", 需要扣减=" + inventory.getQuantity());
            return false;
        }
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
     * 保存库存信息
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