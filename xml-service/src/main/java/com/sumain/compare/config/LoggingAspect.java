package com.sumain.compare.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Around("execution(* com.sumain.compare.service..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("------> {}.{},request: {}", className, methodName, Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
            log.info("<------ {}.{},response: {}", className, methodName, result);
        } catch (Throwable throwable) {
            log.error("<------ {}.{} error: ", className, methodName, throwable);
            throw throwable; // 重新抛出异常以确保事务回滚
        }
        return result;
    }
}
