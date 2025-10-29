package com.bruce.mq.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * RocketMQ重试配置类
 * 解决应用启动时RocketMQ连接不稳定的问题
 */
@Slf4j
@Configuration
public class RocketMQRetryConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    @Autowired
    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 在Spring上下文刷新完成后执行
     * 用于重新初始化RocketMQ监听容器，解决启动时连接失败的问题
     */
    @EventListener(ContextRefreshedEvent.class)
    public void reinitializeRocketMQContainers() {
        try {
            // 获取重试次数和延迟时间配置
            int retryTimes = environment.getProperty("rocketmq.consumer.retry-times", Integer.class, 3);
            int delayTime = environment.getProperty("rocketmq.consumer.delay-time", Integer.class, 5000);

            log.info("开始检查RocketMQ监听容器状态，最大重试次数: {}, 延迟时间: {}ms", retryTimes, delayTime);

            // 获取所有RocketMQ监听容器Bean
            String[] beanNames = applicationContext.getBeanNamesForType(DefaultRocketMQListenerContainer.class);

            for (String beanName : beanNames) {
                try {
                    DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer)
                            applicationContext.getBean(beanName);

                    // 检查容器状态，如果未运行则尝试重启
                    if (!container.isRunning()) {
                        log.warn("发现未运行的RocketMQ监听容器: {}, 尝试重启...", beanName);
                        
                        // 延迟一段时间再尝试重启
                        TimeUnit.MILLISECONDS.sleep(delayTime);
                        
                        // 尝试重启容器
                        restartContainer(container, beanName, retryTimes, delayTime);
                    }
                } catch (Exception e) {
                    log.error("处理RocketMQ监听容器 {} 时发生异常", beanName, e);
                }
            }
        } catch (Exception e) {
            log.error("检查RocketMQ监听容器状态时发生异常", e);
        }
    }

    /**
     * 重启RocketMQ监听容器
     *
     * @param container  监听容器
     * @param beanName   容器Bean名称
     * @param retryTimes 重试次数
     * @param delayTime  延迟时间
     */
    private void restartContainer(DefaultRocketMQListenerContainer container, String beanName,
                                  int retryTimes, int delayTime) {
        for (int i = 1; i <= retryTimes; i++) {
            try {
                log.info("第 {} 次尝试重启RocketMQ监听容器: {}", i, beanName);
                
                // 停止容器（如果正在运行）
                if (container.isRunning()) {
                    container.stop();
                    TimeUnit.MILLISECONDS.sleep(delayTime / 2);
                }
                
                // 启动容器
                container.start();
                
                // 检查是否启动成功
                if (container.isRunning()) {
                    log.info("RocketMQ监听容器 {} 重启成功", beanName);
                    return;
                } else {
                    log.warn("RocketMQ监听容器 {} 重启后仍未运行", beanName);
                }
            } catch (Exception e) {
                log.error("第 {} 次重启RocketMQ监听容器 {} 失败", i, beanName, e);
            }
            
            // 如果不是最后一次重试，则等待一段时间再重试
            if (i < retryTimes) {
                try {
                    TimeUnit.MILLISECONDS.sleep(delayTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("等待重试时线程被中断", ie);
                    return;
                }
            }
        }
        
        log.error("经过 {} 次尝试后，RocketMQ监听容器 {} 仍然无法正常运行", retryTimes, beanName);
    }
}