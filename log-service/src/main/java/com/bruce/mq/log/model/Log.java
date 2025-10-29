package com.bruce.mq.log.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "application-logs")
public class Log {
    @Id
    private String id;

    @Field(type = FieldType.Text, fielddata = true)
    private String service;

    @Field(type = FieldType.Text, fielddata = true)
    private String level;

    @Field(type = FieldType.Text, fielddata = true)
    private String message;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Field(type = FieldType.Text, fielddata = true)
    private String className;

    @Field(type = FieldType.Text, fielddata = true)
    private String methodName;

    @Field(type = FieldType.Keyword)
    private String traceId;
    
    // HTTP请求相关字段
    @Field(type = FieldType.Text, fielddata = true)
    private String requestUrl;
    
    @Field(type = FieldType.Text, fielddata = true)
    private String requestMethod;
    
    @Field(type = FieldType.Text, fielddata = true)
    private String requestParams;
    
    @Field(type = FieldType.Text, fielddata = true)
    private String requestIp;
    
    @Field(type = FieldType.Long)
    private Long executeTime;
    
    @Field(type = FieldType.Text, fielddata = true)
    private String userAgent;
    
    @Field(type = FieldType.Text, fielddata = true)
    private String statusCode;

    // Constructors
    public Log() {}

    public Log(String service, String level, String message, LocalDateTime timestamp, String className, String methodName, String traceId) {
        this.service = service;
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
        this.className = className;
        this.methodName = methodName;
        this.traceId = traceId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        return "Log{" +
                "id='" + id + '\'' +
                ", service='" + service + '\'' +
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