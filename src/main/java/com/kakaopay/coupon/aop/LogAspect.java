package com.kakaopay.coupon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LogAspect {

        Logger logger = LoggerFactory.getLogger(LogAspect.class);
    //@Around(“@annotation(com.example.demo.Clock)”)

        //@Around("@annotation(com.kakaopay.coupon.controller)")
        //@Pointcut("within(@com.kakaopay.coupon.controller *)")
        //@Pointcut("execution(* com.kakaopay.coupon.controller.*.*(..))")
        @Around("@annotation(LogExecutionTime)")
        public Object executionAspect(ProceedingJoinPoint joinPoint) throws Throwable
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Object result = joinPoint.proceed();

            stopWatch.stop();
            //System.out.println(stopWatch.getTime());
            logger.info(String.valueOf(stopWatch.getTotalTimeSeconds()));

            return result;
        }


}
