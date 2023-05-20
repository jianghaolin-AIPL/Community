package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

// Spring Security 5.7.0废弃了 WebSecurityConfigurerAdapter
@Configuration
public class SecurityConfig implements CommunityConstant {
    // 忽略掉对静态资源的拦截
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/resources/**");
    }

    // 现在的项目已经写好了登录、退出方面的内容，就不用Security重写了

    // 对应于老版本的 configure(HttpSecurity http)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 授权
        // 把所有Controller都看一遍，把每个方法的访问路径都记录一下，哪些是不用登录就能访问的，哪些是普通用户能访问的
        // 我们现在还没有管理员和版主能访问的路径，都是普通用户就能访问的
        http.authorizeHttpRequests()
//                .requestMatchers(
//                        "/user/setting",
//                        "/user/upload",
//                        "/user/password",
//                        "/discuss/add",
//                        "/comment/add/**",
//                        "/letter/**",
//                        "/notice/**",
//                        "/follow",
//                        "/unFollow",
//                        "/like"
////                        "/logout"
//                )
//                .hasAnyAuthority(AUTHORITY_USER, AUTHORITY_ADMIN, AUTHORITY_MODERATOR)
//                .requestMatchers(
//                        "/discuss/top",
//                        "/discuss/wonderful"
//                )
//                .hasAnyAuthority(AUTHORITY_MODERATOR)
//                .requestMatchers(
//                        "/discuss/delete",
//                        "/data/**"
//                )
//                .hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll(); // 除了上面配置的请求，其他请求统统允许

        http.csrf().disable(); // 让页面不去生成CSRF凭证，不做相关检查，默认是做检查的，但所有异步请求都得自己去写

        // 权限不够时的处理
        // 上个demo只用了accessDeniedPage返回一个页面简单处理，但现在这个是正式的项目，有多种请求，有的请求是普通请求，有的请求是异步请求
        // 对于普通的请求，期望服务器返回的是html，对于异步请求，浏览器期望返回的是json，所以要区别对待
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() { // 没登录时的处理
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        // 如何判断请求是同步还是异步，主要是看请求消息头的某一个值
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if("XMLHttpRequested".equals(xRequestedWith)) {
                            // 异步请求
                            response.setContentType("application/plain;charset=utf-8"); // 声明返回的类型 application/plain表示普通字符串（要确保是JSON格式) charset=utf-8字符集 支持中文
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403, "您还没有登录！")); // 当没有权限的时候 服务器拒绝你访问的时候，通常返回403
                        } else {
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() { // 权限不足时的处理
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if("XMLHttpRequested".equals(xRequestedWith)) {
                            // 异步请求
                            response.setContentType("application/plain;charset=utf-8"); // 声明返回的类型 application/plain表示普通字符串（要确保是JSON格式) charset=utf-8字符集 支持中文
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403, "您没有访问此功能的权限！")); // 当没有权限的时候 服务器拒绝你访问的时候，通常返回403
                        } else {
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // Security底层管的比较多，关于退出登录也管，默认情况下会自动拦截名为"/logout"的退出的路径
        // Security底层都是通过Filter去拦截 做权限管理 Filter代码执行是在DispatcherServlet之前，肯定也在Controller之前
        // 所以如果它提前拦截到/logout帮我们做退出的处理之后，到此结束，就不会让程序往下走，我们自己写的/logout就不会被执行
        // 现在的想法是想走自己的/logout，不想执行他的逻辑，因为自己的代码之前都写好了
        http.logout().logoutUrl("/securitylogout"); // 将Security底层默认拦截的退出路径改为一个不存在的路径，拦截不到，这样代码就会执行到我们自己的退出逻辑

//        http.formLogin().loginPage();

        // 这里我们只配了授权，认证没处理，认证会走LoginController里自己的认证
        // 上个小demo里我们用了Security的认证，是把认证信息封装到一个token里（UsernamePasswordAuthenticationToken），这个token会被Security的一个Filter获取到
        // Filter会把这个token存到SecurityContext里，后面进行授权的时候，进行判断你有没有权限的时候，都是从SecurityContext对象里得到这个token，看这个token来判断你的权限
        // 这是Security底层的逻辑，现在走自己的认证，我们没有这东西，Security没法帮你做授权
        // 现在我们是绕过了Security的认证逻辑，但结论得存到SecurityContext里面

        return http.build();
    }
}
