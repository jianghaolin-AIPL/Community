package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    // 第一个*代表返回值 *表示所有返回值均可；com.nowcoder.community.service的所有业务组件(.*)的所有方法(.*)所有参数(..)都要处理
    public void pointcut() {
    }

    @Before("pointcut()") // 在连接点之前记录日志
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()") // 在连接点之后记录日志
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()") // 在有了返回值之后记录日志
    public void afterReturning() {
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointcut()") // 在抛异常的时候记录日志
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()") // 在前后都记录日志
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable { // 参数是连接点，即程序织入的部位
        System.out.println("around before");
        Object obj = joinPoint.proceed(); // 调目标组件的方法，目标组件可能有返回值，用obj接一下
        // 因为程序在执行的时候会执行代理对象，这个逻辑会织入到代理对象里，用来代替原始对象
        System.out.println("around after");
        return obj;
    }
}
