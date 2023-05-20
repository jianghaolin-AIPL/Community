package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class}) // {}中可以写多个异常，这里写Exception.class(所有异常的父类)，表示所有异常都用它来处理
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException { // 这个方法可以带很多参数，可以去Spring手册中去查(太多记不住)，比较常用的就这三个
        logger.error("服务器发生异常：" + e.getMessage());
        // 上面只是对异常的一个概括，若希望把异常的详细的栈的信息都记录下来
        for(StackTraceElement element : e.getStackTrace()) {
            // 每个element记录了一条异常信息
            logger.error(element.toString());
        }

        // 浏览器访问服务器，可能是普通请求，需要返回网页，重定向到500
        // 浏览器访问服务器也可能是异步请求，希望返回json，这时重定向到500就没有意义了，需要区分处理
        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)) {
            // 希望返回json
            // response.setContentType("application/json;charset=utf-8"); 可以这么写，向浏览器返回的是json字符串，浏览器会自动把它转为js对象
            response.setContentType("application/plain;charset=utf-8"); // 也可以这么写，表示向浏览器返回的是普通的字符串，浏览器得到后需要人为的将字符串转换为js对象（$.parseJSON()）
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常"));
        } else {
            response.sendRedirect(request.getContextPath() + "/error"); // request.getContextPath():获取项目的访问路径
        }
    }
}
