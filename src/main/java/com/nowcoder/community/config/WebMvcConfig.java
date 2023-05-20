package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor) // 这样就会把请求全部拦截了，要设置拦截路径
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg") // /**代表static目录下所有路径 除了这些静态资源其他请求都被拦截了
                .addPathPatterns("/register", "/login"); // 明确拦截的路径 其他就不拦截了

        registry.addInterceptor(loginTicketInterceptor) // 这样就会把请求全部拦截了，要设置拦截路径
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg"); // /**代表static目录下所有路径 除了这些静态资源其他请求都被拦截了

//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }
}
