package com.bruce.mq.shared.log.producer;

import com.bruce.mq.shared.log.model.LogMessage;
import com.bruce.mq.shared.util.StartupTimeTracker;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnProperty(name = "rocketmq.enable", havingValue = "true", matchIfMissing = false)
public class LogProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);
    
    @Autowired
    @Lazy
    private RocketMQTemplate rocketMQTemplate;
    
    @Autowired
    private StartupTimeTracker startupTimeTracker;
    
    private static final String LOG_TOPIC = "log-topic";
    
    @PostConstruct
    public void init() {
        startupTimeTracker.recordEnd("LogProducer");
    }
    
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