package com.bruce.mq.shared.log.producer;

import com.bruce.mq.shared.log.model.LogMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class LogProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);
    
    @Autowired
    @Lazy
    private RocketMQTemplate rocketMQTemplate;
    
    private static final String LOG_TOPIC = "log-topic";
    
    public void sendLog(LogMessage logMessage) {
        try {
            if (rocketMQTemplate == null) {
                logger.error("RocketMQTemplate is not initialized");
                return;
            }
            rocketMQTemplate.sendOneWay(LOG_TOPIC, MessageBuilder.withPayload(logMessage).build());
        } catch (Exception e) {
            logger.error("Failed to send log to RocketMQ", e);
        }
    }
}