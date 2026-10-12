package com.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut: all methods inside ProductService
    @Pointcut("execution(* com.example.service.ProductService.*(..))")
    public void productServiceMethods() {
    }

    @Pointcut("execution(* com.example.dao.ProductRepository.*(..))")
    public void productRepositoryMethods() {
    }

    // BEFORE advice
    @Before("productServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("ProductService - Entering method: {} with args: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    // AFTER advice
    @AfterReturning(pointcut = "productServiceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("ProductService - Exiting method: {} with result: {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @Around ("productRepositoryMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("ProductRepository - Entering method: {} with args: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
        Object result = joinPoint.proceed();
        log.info("ProductRepository - Exiting method: {} with result: {}",
                joinPoint.getSignature().getName(),
                result);
        return result;
    }
}
