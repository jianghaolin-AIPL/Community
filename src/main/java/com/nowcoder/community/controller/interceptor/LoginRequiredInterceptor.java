package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截到的是不是方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取拦截到的Method对象
            Method method = handlerMethod.getMethod();

            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null) {
                // 没有登录，重定向登录界面
                // 当前方法是接口声明的，所以不能像controller一样返回模板，这里需要用response重定向
                // 其实controller里的return redirect其底层也是这么写的
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }

        }
        return true;
    }
}
