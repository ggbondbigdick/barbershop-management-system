package com.barber.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: SaTokenConfigure
 * Description:
 */
@EnableWebMvc
@SpringBootConfiguration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则 Register route interceptors, customize authentication rules
        registry.addInterceptor(new SaInterceptor(handler -> {
                // 登录认证 -- 拦截所有路由 Login authentication-intercept all routes
                SaRouter.match("/**", r -> StpUtil.checkLogin());})
                // 是否注解 @SaCheckLogin 拦截  Whether to comment @SaCheckLogin interception
                .isAnnotation(true))
                // 拦截所有接口 Intercept all interfaces
                .addPathPatterns("/**")
                // 不拦截的接口 Interface not intercepted
                .excludePathPatterns("/user/login", "/file/preview/*", "/user/logout", "/user/register", "/haircuttingProject/home",
                        "/haircuttingProject/info");
    }
}
