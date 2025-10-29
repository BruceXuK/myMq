package com.bruce.mq.log.controller;

import com.bruce.mq.log.model.Log;
import com.bruce.mq.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志服务控制器
 * 提供日志查询相关的RESTful API接口
 *
 * @author BruceXuK
 */
@RestController
@RequestMapping("/logs")
public class LogController {
    
    @Autowired
    private LogService logService;
    
    /**
     * 根据服务名称和时间范围查询日志
     *
     * @param service 服务名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 符合条件的日志列表
     */
    @GetMapping("/service/{service}")
    public List<Log> getLogsByService(
            @PathVariable String service,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return logService.getLogsByServiceAndTimeRange(service, startTime, endTime);
    }
    
    /**
     * 根据日志级别查询日志
     *
     * @param level 日志级别 (如: INFO, WARN, ERROR)
     * @return 符合条件的日志列表
     */
    @GetMapping("/level/{level}")
    public List<Log> getLogsByLevel(@PathVariable String level) {
        return logService.getLogsByLevel(level);
    }
    
    /**
     * 根据追踪ID查询日志
     *
     * @param traceId 追踪ID
     * @return 符合条件的日志列表
     */
    @GetMapping("/trace/{traceId}")
    public List<Log> getLogsByTraceId(@PathVariable String traceId) {
        return logService.getLogsByTraceId(traceId);
    }
    
    /**
     * 根据IP地址查询日志
     *
     * @param ip IP地址
     * @return 符合条件的日志列表
     */
    @GetMapping("/ip/{ip}")
    public List<Log> getLogsByIp(@PathVariable String ip) {
        return logService.getLogsByIp(ip);
    }
    
    /**
     * 根据URL查询日志
     *
     * @param url 请求URL
     * @return 符合条件的日志列表
     */
    @GetMapping("/url")
    public List<Log> getLogsByUrl(@RequestParam String url) {
        return logService.getLogsByUrl(url);
    }
    
    /**
     * 根据请求方法查询日志
     *
     * @param method 请求方法 (如: GET, POST, PUT, DELETE)
     * @return 符合条件的日志列表
     */
    @GetMapping("/method/{method}")
    public List<Log> getLogsByMethod(@PathVariable String method) {
        return logService.getLogsByMethod(method);
    }
    
    /**
     * 根据响应状态码查询日志
     *
     * @param status 响应状态码 (如: 200, 404, 500)
     * @return 符合条件的日志列表
     */
    @GetMapping("/status/{status}")
    public List<Log> getLogsByStatus(@PathVariable String status) {
        return logService.getLogsByStatus(status);
    }
}