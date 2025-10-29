package com.bruce.mq.shared.log.model;

import java.time.LocalDateTime;

public class LogMessage {
    private String service;
    private String level;
    private String message;
    private LocalDateTime timestamp;
    private String className;
    private String methodName;
    private String traceId;
    
    // HTTP请求相关字段
    private String requestUrl;
    private String requestMethod;
    private String requestParams;
    private String requestIp;
    private Long executeTime;
    private String userAgent;
    private String statusCode;

    // Constructors
    public LogMessage() {}

    public LogMessage(String service, String level, String message, LocalDateTime timestamp, String className, String methodName, String traceId) {
        this.service = service;
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
        this.className = className;
        this.methodName = methodName;
        this.traceId = traceId;
    }

    // Getters and Setters
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "service='" + service + '\'' +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", executeTime=" + executeTime +
                ", userAgent='" + userAgent + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}