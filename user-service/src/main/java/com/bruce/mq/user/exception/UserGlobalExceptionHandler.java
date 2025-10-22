package com.bruce.mq.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务全局异常处理类
 * 
 * @author BruceXuK
 */
@ControllerAdvice
public class UserGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserGlobalExceptionHandler.class);

    /**
     * 处理通用异常
     * 
     * @param ex 异常对象
     * @param request Web请求对象
     * @return 响应实体
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        logger.error("发生未处理的异常: ", ex);
        
        Map<String, Object> body = new HashMap<>();
        body.put("message", "系统内部错误");
        body.put("details", ex.getMessage());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理非法参数异常
     * 
     * @param ex 非法参数异常对象
     * @param request Web请求对象
     * @return 响应实体
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("发生非法参数异常: ", ex);
        
        Map<String, Object> body = new HashMap<>();
        body.put("message", "请求参数错误");
        body.put("details", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}