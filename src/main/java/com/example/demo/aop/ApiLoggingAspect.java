package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ApiLoggingAspect {

    @Autowired
    private HttpServletRequest request;

    @Around("execution(* com.example.demo.controllers..*(..))") // 替換成你的 Controller 路徑
    public Object logApiResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(); // 執行 Controller 方法

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String method = request.getMethod();

        if (result instanceof ResponseEntity<?> responseEntity) {
            int statusCode = responseEntity.getStatusCodeValue();

            String logMessage = String.format("[%s %s] %d", method, path, statusCode);

            if (statusCode >= 200 && statusCode < 300) {
                log.info(logMessage);
            } else if (statusCode == 400) {
                log.warn(logMessage);
            } else if (statusCode == 401 || statusCode == 403) {
                log.error(logMessage);
            } else if (statusCode >= 500) {
                log.error(logMessage);
            } else {
                log.debug(logMessage);
            }
        }

        return result;
    }
}
