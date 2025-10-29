package com.bruce.mq.log.repository;

import com.bruce.mq.log.model.Log;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends ElasticsearchRepository<Log, String> {
    List<Log> findByServiceAndTimestampBetween(String service, LocalDateTime startTime, LocalDateTime endTime);
    List<Log> findByLevel(String level);
    List<Log> findByTraceId(String traceId);
    
    // 根据请求IP查询
    List<Log> findByRequestIp(String requestIp);
    
    // 根据请求URL查询
    List<Log> findByRequestUrl(String requestUrl);
    
    // 根据请求方法查询
    List<Log> findByRequestMethod(String requestMethod);
    
    // 根据状态码查询
    List<Log> findByStatusCode(String statusCode);
}