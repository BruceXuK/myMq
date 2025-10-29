package com.bruce.mq.shared.log.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.bruce.mq.shared.log.model.LogMessage;
import com.bruce.mq.shared.log.producer.LogProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MqLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(MqLogAppender.class);
    
    private static LogProducer logProducer;
    
    // 用于标记Spring容器是否已完全初始化
    private static final AtomicBoolean springInitialized = new AtomicBoolean(false);
    
    // 最小日志级别，默认为WARN，只发送WARN及以上级别的日志
    private String minLogLevel = "WARN";
    
    @Autowired
    public void setLogProducer(LogProducer logProducer) {
        MqLogAppender.logProducer = logProducer;
        // 标记Spring容器已完全初始化
        springInitialized.set(true);
    }
    
    public void setMinLogLevel(String minLogLevel) {
        this.minLogLevel = minLogLevel;
    }
    
    private boolean isLogLevelEnabled(Level eventLevel) {
        if (eventLevel == null) {
            return false;
        }
        
        // 获取最小日志级别的Level对象
        Level minLevel = Level.toLevel(minLogLevel, Level.WARN);
        
        // 比较日志级别
        return eventLevel.isGreaterOrEqual(minLevel);
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        try {
            // 检查日志级别是否满足最小级别要求
            if (!isLogLevelEnabled(event.getLevel())) {
                return;
            }
            
            // 检查Spring容器是否已完全初始化
            if (!springInitialized.get()) {
                // Spring容器未完全初始化时不记录日志，避免出现LogProducer未初始化的错误
                return;
            }
            
            // 检查logProducer是否已注入
            if (logProducer == null) {
                logger.error("LogProducer is not initialized");
                return;
            }
            
            // 再次检查event对象是否为null
            if (event == null) {
                logger.error("ILoggingEvent is null");
                return;
            }
            
            LogMessage logMessage = new LogMessage();
            logMessage.setService(getContext() != null ? getContext().getProperty("spring.application.name") : "unknown");
            logMessage.setLevel(event.getLevel() != null ? event.getLevel().toString() : "UNKNOWN");
            logMessage.setMessage(formatMessage(event));
            logMessage.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeStamp()), ZoneId.systemDefault()));
            logMessage.setClassName(event.getLoggerName());
            
            // 获取调用栈信息
            StackTraceElement[] callerData = event.getCallerData();
            if (callerData != null && callerData.length > 0) {
                StackTraceElement element = callerData[0];
                logMessage.setClassName(element.getClassName());
                logMessage.setMethodName(element.getMethodName());
            }
            
            // 如果有MDC信息，可以从中获取traceId等信息
            if (event.getMDCPropertyMap() != null && event.getMDCPropertyMap().containsKey("traceId")) {
                logMessage.setTraceId(event.getMDCPropertyMap().get("traceId"));
            }
            
            logProducer.sendLog(logMessage);
        } catch (Exception e) {
            // 忽略日志发送错误，避免影响主流程
            logger.error("Failed to send log to MQ", e);
        }
    }
    
    private String formatMessage(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(event.getFormattedMessage());
        
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            sb.append("\n").append(throwableProxy.getClassName()).append(": ").append(throwableProxy.getMessage());
            
            for (StackTraceElementProxy step : throwableProxy.getStackTraceElementProxyArray()) {
                sb.append("\n\tat ").append(step.getSTEAsString());
            }
        }
        
        return sb.toString();
    }
}