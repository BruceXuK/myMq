package com.bruce.mq.log.rocketmq;

import com.bruce.mq.log.service.LogService;
import com.bruce.mq.shared.log.model.LogMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 日志消息监听器
 * 负责接收并处理日志消息
 *
 * @author BruceXuK
 */
@Component
@ConditionalOnProperty(name = "rocketmq.enable", havingValue = "true", matchIfMissing = false)
@RocketMQMessageListener(
        topic = "log-topic",
        consumerGroup = "log-consumer-group"
)
public class LogMessageListener implements RocketMQListener<LogMessage> {
    
    private static final Logger log = LoggerFactory.getLogger(LogMessageListener.class);

    @Autowired
    private LogService logService;

    /**
     * 处理日志消息
     *
     * @param logMessage 日志消息
     */
    @Override
    public void onMessage(LogMessage logMessage) {
        try {
            logService.saveLog(logMessage);
        } catch (Exception e) {
            log.error("日志服务处理日志消息失败", e);
        }
    }
}