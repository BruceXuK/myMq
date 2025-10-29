package com.bruce.mq.user.exception;

import com.bruce.mq.common.exception.handler.CommonExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 用户服务全局异常处理类
 * 
 * @author BruceXuK
 */
@ControllerAdvice
public class UserGlobalExceptionHandler extends CommonExceptionHandler {
    // 继承通用异常处理，如有特定需求可在此添加
}