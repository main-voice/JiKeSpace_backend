package com.tjsse.jikespace.config;

/**
 * @program: JiKeSpace
 * @description: 设置跨域问题
 * @packagename: com.tjsse.jikespace.config
 * @author: peng peng
 * @date: 2022-12-01 23:05
 **/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CorsConfig {
    // 生成一个一个bean
    // 配置所有的访问接口
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // 嵌套类
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 允许所有的ip访问
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}