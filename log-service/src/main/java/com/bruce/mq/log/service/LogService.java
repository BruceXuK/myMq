package com.bruce.mq.log.service;

import com.bruce.mq.log.model.Log;
import com.bruce.mq.log.repository.LogRepository;
import com.bruce.mq.shared.log.model.LogMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    
    @Autowired
    private LogRepository logRepository;
    
    public void saveLog(LogMessage logMessage) {
        Log log = new Log();
        BeanUtils.copyProperties(logMessage, log);
        
        // 处理新增的HTTP请求字段
        log.setRequestUrl(logMessage.getRequestUrl());
        log.setRequestMethod(logMessage.getRequestMethod());
        log.setRequestParams(logMessage.getRequestParams());
        log.setRequestIp(logMessage.getRequestIp());
        log.setExecuteTime(logMessage.getExecuteTime());
        log.setUserAgent(logMessage.getUserAgent());
        log.setStatusCode(logMessage.getStatusCode());
        
        // 如果时间戳为null，则使用当前时间
        if (log.getTimestamp() == null) {
            log.setTimestamp(LocalDateTime.now());
        }
        logRepository.save(log);
    }
    
    public List<Log> getLogsByServiceAndTimeRange(String service, LocalDateTime startTime, LocalDateTime endTime) {
        return logRepository.findByServiceAndTimestampBetween(service, startTime, endTime);
    }
    
    public List<Log> getLogsByLevel(String level) {
        return logRepository.findByLevel(level);
    }
    
    public List<Log> getLogsByTraceId(String traceId) {
        return logRepository.findByTraceId(traceId);
    }
    
    public List<Log> getLogsByIp(String ip) {
        return logRepository.findByRequestIp(ip);
    }
    
    public List<Log> getLogsByUrl(String url) {
        return logRepository.findByRequestUrl(url);
    }
    
    public List<Log> getLogsByMethod(String method) {
        return logRepository.findByRequestMethod(method);
    }
    
    public List<Log> getLogsByStatus(String status) {
        return logRepository.findByStatusCode(status);
    }
}