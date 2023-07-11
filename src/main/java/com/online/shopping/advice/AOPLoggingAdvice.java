package com.online.shopping.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.shopping.config.Generated;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Generated
public class AOPLoggingAdvice {
    Logger log = LoggerFactory.getLogger(AOPLoggingAdvice.class);

    @Pointcut(value = " execution(* com.online.shopping.*.*.*(..) )")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object appLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String className = proceedingJoinPoint.getTarget().getClass().toString();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] objArray = proceedingJoinPoint.getArgs();

        log.info("Initiated the method call. Class : [" + className + "] | Invoked method is : [ " + methodName + "() ] | Input RequestBody: ["
                + mapper.writeValueAsString(objArray) + "].");

        Object obj = proceedingJoinPoint.proceed();

        log.info("Completed the method call. Class : [" + className + "] | Invoked method is : [ " + methodName + "() ] | Response Body: [ "
                + mapper.writeValueAsString(obj) + " ].");
        return obj;

    }
}
