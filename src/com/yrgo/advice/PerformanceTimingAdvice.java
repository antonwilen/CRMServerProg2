package com.yrgo.advice;

import java.util.List;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTimingAdvice {

    public Object performTimingMeasurement (ProceedingJoinPoint method)
            throws Throwable {

        //before
        long startTime =System.nanoTime();

        try {
            //proceed to target
            Object value= method.proceed();
            return value;
        }finally {
            //after
            long endTime= System.nanoTime();
            long timeTaken = endTime - startTime;
            double timeTakenMilliseconds = (double) timeTaken / 1000000;
            System.out.println("The method " +
                    method.getSignature().getName() + " from the class " + method.getSignature().getDeclaringTypeName() + " took " + timeTakenMilliseconds + " ms");
        }
    }
}

