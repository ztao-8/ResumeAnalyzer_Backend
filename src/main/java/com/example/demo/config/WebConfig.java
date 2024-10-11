package com.example.demo.config;

/**
 * content
 *
 * @author KexinDai
 * @date 2024/10/9
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // 配置允许 CORS 的 URL 路径
                .allowedOrigins("http://localhost:3000")  // 允许的来源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true);  // 是否允许发送 Cookie
    }
}
