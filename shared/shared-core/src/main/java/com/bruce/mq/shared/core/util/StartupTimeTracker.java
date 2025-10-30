package com.bruce.mq.shared.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 启动时间跟踪器
 * 用于监控和记录应用启动过程中各组件的加载时间
 */
@Component
public class StartupTimeTracker {
    
    private static final Logger logger = LoggerFactory.getLogger(StartupTimeTracker.class);
    
    // 存储各组件加载时间的映射
    private final Map<String, Long> componentLoadTimes = new ConcurrentHashMap<>();
    
    // 存储开始时间的映射
    private final Map<String, Long> startTimes = new HashMap<>();
    
    /**
     * 记录组件开始加载的时间
     * 
     * @param componentName 组件名称
     */
    public void recordStart(String componentName) {
        long startTime = System.currentTimeMillis();
        startTimes.put(componentName, startTime);
        logger.info("开始加载组件: {}", componentName);
    }
    
    /**
     * 记录组件加载完成的时间
     * 
     * @param componentName 组件名称
     */
    public void recordEnd(String componentName) {
        Long startTime = startTimes.get(componentName);
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            componentLoadTimes.put(componentName, duration);
            logger.info("组件 {} 加载完成，耗时: {} ms", componentName, duration);
        } else {
            logger.warn("未找到组件 {} 的开始时间记录", componentName);
        }
    }
    
    /**
     * 获取所有组件的加载时间
     * 
     * @return 组件加载时间映射
     */
    public Map<String, Long> getComponentLoadTimes() {
        return new HashMap<>(componentLoadTimes);
    }
    
    /**
     * 打印所有组件的加载时间报告
     */
    public void printReport() {
        logger.info("======= 启动时间报告 =======");
        componentLoadTimes.forEach((component, time) -> 
            logger.info("组件: {} 加载耗时: {} ms", component, time)
        );
        logger.info("========================");
    }
}