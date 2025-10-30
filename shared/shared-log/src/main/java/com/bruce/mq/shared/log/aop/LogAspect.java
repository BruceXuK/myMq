package com.bruce.mq.shared.log.aop;

import com.bruce.mq.shared.log.annotation.Log;
import com.bruce.mq.shared.log.model.LogMessage;
import com.bruce.mq.shared.log.producer.LogProducer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private LogProducer logProducer;

    /**
     * 定义切点，拦截所有使用@Log注解的方法
     */
    @Pointcut("@annotation(com.bruce.mq.shared.log.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 环绕通知，处理日志记录
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        LogMessage logMessage = new LogMessage();

        try {
            // 执行方法
            result = point.proceed();
            return result;
        } catch (Exception e) {
            logMessage.setMessage("执行异常: " + e.getMessage());
            throw e;
        } finally {
            long executeTime = System.currentTimeMillis() - beginTime;
            // 记录日志
            handleLog(point, logMessage, executeTime, result);
        }
    }

    /**
     * 处理日志记录
     */
    private void handleLog(ProceedingJoinPoint point, LogMessage logMessage, long executeTime, Object result) {
        try {
            // 获取注解
            MethodSignature signature = (MethodSignature) point.getSignature();
            Log logAnnotation = signature.getMethod().getAnnotation(Log.class);

            // 设置基本信息
            logMessage.setService(System.getProperty("spring.application.name", "unknown-service"));
            logMessage.setLevel("INFO");
            logMessage.setTimestamp(LocalDateTime.now());
            logMessage.setClassName(point.getTarget().getClass().getName());
            logMessage.setMethodName(signature.getName());

            // 设置操作描述
            if (logAnnotation != null && StringUtils.hasText(logAnnotation.value())) {
                logMessage.setMessage(logAnnotation.value());
            } else {
                logMessage.setMessage("执行方法: " + signature.getName());
            }

            // 获取请求相关信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // 设置请求URL
                logMessage.setRequestUrl(request.getRequestURL().toString());

                // 设置请求方法
                logMessage.setRequestMethod(request.getMethod());

                // 设置请求IP
                logMessage.setRequestIp(getIpAddress(request));

                // 设置User-Agent
                logMessage.setUserAgent(request.getHeader("User-Agent"));

                // 设置状态码（默认为200，发生异常时会修改）
                logMessage.setStatusCode("200");

                // 记录请求参数
                if (logAnnotation == null || logAnnotation.recordParams()) {
                    String params = Arrays.toString(point.getArgs());
                    logMessage.setRequestParams(params);
                }

                // 记录返回结果
                if (logAnnotation != null && logAnnotation.recordResult()) {
                    logMessage.setMessage(logMessage.getMessage() + ", 返回结果: " + result);
                }
            }

            // 设置执行时间
            logMessage.setExecuteTime(executeTime);

            // 发送到MQ
            logProducer.sendLog(logMessage);
        } catch (Exception e) {
            logger.error("记录日志异常: {}", e.getMessage());
        }
    }

    /**
     * 获取请求IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
