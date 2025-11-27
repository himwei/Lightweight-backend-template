package com.himwei.testtemplatebackend.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",

                        // Knife4j & Swagger 资源放行
                        "/doc.html",           // Knife4j 主页
                        "/doc.html/**",
                        "/webjars/**",         // 静态资源
                        "/v3/api-docs/**",     // OpenAPI JSON 数据 (前端生成代码用这个)
                        "/swagger-resources/**",

                        "/favicon.ico",
                        "/error"
                );
    }
}
