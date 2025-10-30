package com.bruce.mq.shared.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重试工具类
 * 提供通用的重试机制
 * 
 * @author BruceXuK
 */
public class RetryUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(RetryUtils.class);
    
    /**
     * 带重试机制的执行器
     * 
     * @param <T> 返回值类型
     * @param operation 操作函数
     * @param maxRetries 最大重试次数
     * @param operationName 操作名称（用于日志记录）
     * @return 操作结果
     * @throws Exception 操作异常
     */
    public static <T> T executeWithRetry(Operation<T> operation, int maxRetries, String operationName) throws Exception {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                logger.debug("尝试执行操作: {} (第{}次)", operationName, attempt);
                T result = operation.execute();
                logger.debug("操作执行成功: {} (第{}次)", operationName, attempt);
                return result;
            } catch (Exception e) {
                lastException = e;
                logger.warn("操作执行失败: {} (第{}次)", operationName, attempt, e);
                
                // 如果不是最后一次尝试，等待一段时间再重试
                if (attempt < maxRetries) {
                    try {
                        long waitTime = 1000 * attempt; // 等待时间递增
                        logger.debug("等待{}毫秒后重试操作: {}", waitTime, operationName);
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.warn("重试操作被中断: {}", operationName, ie);
                        throw new RuntimeException("操作被中断: " + operationName, ie);
                    }
                }
            }
        }
        
        logger.error("操作最终失败: {}，已重试{}次", operationName, maxRetries, lastException);
        throw lastException;
    }
    
    /**
     * 带重试机制的执行器（无返回值）
     * 
     * @param operation 操作函数
     * @param maxRetries 最大重试次数
     * @param operationName 操作名称（用于日志记录）
     * @throws Exception 操作异常
     */
    public static void executeWithRetry(VoidOperation operation, int maxRetries, String operationName) throws Exception {
        executeWithRetry(() -> {
            operation.execute();
            return null;
        }, maxRetries, operationName);
    }
    
    /**
     * 有返回值的操作接口
     * 
     * @param <T> 返回值类型
     */
    @FunctionalInterface
    public interface Operation<T> {
        T execute() throws Exception;
    }
    
    /**
     * 无返回值的操作接口
     */
    @FunctionalInterface
    public interface VoidOperation {
        void execute() throws Exception;
    }
}