package com.dum.design.strategy.v2.client;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 策略模式性能切面
 */
@Component
@Aspect
@Slf4j
public class PerformanceAspect {
    @Around("strategyOps()")
    public Object logPerformance(ProceedingJoinPoint pjp){
        log.info("logPerformance start...");
        long startTime = System.currentTimeMillis();
        String name="";
        try {
            name = pjp.getSignature().toShortString();
            return pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("{};{}ms", name,endTime-startTime);
        }

    }

    @Pointcut("execution(* com.dum.design.strategy.v2..*(..))")
    public void strategyOps(){

    }
}
