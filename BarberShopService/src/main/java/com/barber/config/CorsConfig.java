package com.barber.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: CorsConfig
 * Description: 跨域配置
 */
@Configuration
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 拦截所有的请求 Intercept all requests
                .allowedOrigins("*")  // 可跨域的域名，可以为 *   A cross-domain name, which can be *
                .allowCredentials(true)
                .allowedMethods("*")   // 允许跨域的方法，可以单独配置  Allow cross-domain methods that can be configured separately
                .allowedHeaders("*");  // 允许跨域的请求头，可以单独配置 Cross-domain request headers can be configured separately.
    }

}
