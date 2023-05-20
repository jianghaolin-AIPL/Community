package com.nowcoder.community.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ServiceLogAspect {

    public static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    // 第一个*代表返回值 *表示所有返回值均可；com.nowcoder.community.service的所有业务组件(.*)的所有方法(.*)所有参数(..)都要处理
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) { // 除了环绕通知(around)以外的通知，也可以加连接点参数
        // 用户[1.2.3.4]，在[xxx]，访问了[com.nowcoder.community.service.xxx()]

        // 用户IP得通过request获取 不能在这里声明request对象,利用RequestContextHolder工具类
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 下面这行代码报错一般是因为attributes为空，attributes是和请求有关的对象
        // 之前没有生产者与消费者对象，所有对service的访问都是通过Controller，现在消费者里面也调了service，不是通过controller去调的，在这次调用里就没有request
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 连接点指代的是程序织入的目标(目标组件调用的方法)
        // joinPoint.getSignature().getDeclaringTypeName():获取类名
        // joinPoint.getSignature().getName():获取方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s]，在[%s]，访问了[%s]。", ip, now, target));
    }
}
