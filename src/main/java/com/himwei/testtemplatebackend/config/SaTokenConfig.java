package com.himwei.testtemplatebackend.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    // 1. 配置拦截器 (Sa-Token)
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/file/upload",        // ✅ 如果你想上传文件也不用登录，可以放行（通常上传是需要登录的，看你需求）

                        // ✅ 核心：放行图片资源的访问路径！
                        // 必须和你下面 addResourceHandlers 里的路径匹配
                        "/images/**",

                        // Knife4j & Swagger 资源放行
                        "/doc.html",
                        "/doc.html/**",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        "/error"
                );
    }

    // 2. 配置静态资源映射 (图片本地存储)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 获取当前路径
        String projectPath = System.getProperty("user.dir");

        // 2. 关键修复：统一将 Windows 的反斜杠 \ 替换为 /，避免路径解析错误
        // 比如 D:\code\project 变成 D:/code/project
        String path = projectPath.replace("\\", "/");

        // 3. 拼接路径
        // 注意：
        // (1) 必须加 "file:" 前缀
        // (2) 必须以 "/" 结尾，否则 Spring 无法识别它是文件夹
        String locations = "file:" + path + "/uploaded_files/";

        // 打印一下路径，方便排查 (启动时在控制台看一眼)
//        System.out.println("图片映射路径: " + locations);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(locations);
    }
}
