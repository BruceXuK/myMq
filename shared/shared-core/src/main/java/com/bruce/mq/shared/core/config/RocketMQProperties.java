package com.bruce.mq.shared.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RocketMQ通用配置类
 * 用于集中管理RocketMQ相关配置
 * 
 * @author BruceXuK
 */
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {
    
    /**
     * NameServer地址
     */
    private String nameServer = "localhost:9876";
    
    /**
     * 生产者配置
     */
    private Producer producer = new Producer();
    
    /**
     * 消费者配置
     */
    private Consumer consumer = new Consumer();

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }
    
    public Producer getProducer() {
        return producer;
    }
    
    public void setProducer(Producer producer) {
        this.producer = producer;
    }
    
    public Consumer getConsumer() {
        return consumer;
    }
    
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
    
    /**
     * 生产者配置类
     * 
     * @author BruceXuK
     */
    public static class Producer {
        private String group;
        private int sendMessageTimeout = 3000;
        private int retryTimesWhenSendFailed = 2;
        private int retryTimesWhenSendAsyncFailed = 2;
        
        public String getGroup() {
            return group;
        }
        
        public void setGroup(String group) {
            this.group = group;
        }
        
        public int getSendMessageTimeout() {
            return sendMessageTimeout;
        }
        
        public void setSendMessageTimeout(int sendMessageTimeout) {
            this.sendMessageTimeout = sendMessageTimeout;
        }
        
        public int getRetryTimesWhenSendFailed() {
            return retryTimesWhenSendFailed;
        }
        
        public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
            this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        }
        
        public int getRetryTimesWhenSendAsyncFailed() {
            return retryTimesWhenSendAsyncFailed;
        }
        
        public void setRetryTimesWhenSendAsyncFailed(int retryTimesWhenSendAsyncFailed) {
            this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
        }
    }
    
    /**
     * 消费者配置类
     * 
     * @author BruceXuK
     */
    public static class Consumer {
        private int retryTimes = 3;
        private int delayTime = 5000;
        
        public int getRetryTimes() {
            return retryTimes;
        }
        
        public void setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
        }
        
        public int getDelayTime() {
            return delayTime;
        }
        
        public void setDelayTime(int delayTime) {
            this.delayTime = delayTime;
        }
    }
}