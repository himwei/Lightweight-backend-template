package com.himwei.testtemplatebackend.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 全局配置类
 * 用于解决前后端交互中的数据序列化问题
 */
@Configuration
public class JacksonConfig {

    /**
     * 定制 Jackson 的序列化行为
     *
     * 背景：
     * Java 中的 Long 类型最大值是 2^63-1 (约 922亿亿)，而 JavaScript 的 Number 类型最大安全整数 (MAX_SAFE_INTEGER)
     * 只有 2^53-1 (约 9000万亿)。
     *
     * 问题：
     * 数据库的主键 ID (bigint) 或雪花算法生成的 ID (19位) 通常会超过 JS 的最大安全范围。
     * 当后端直接返回 Long 类型给前端时，JS 会发生精度丢失（后几位变成 0），导致前端传回来的 ID 不正确，
     * 进而导致查询、修改、删除失败。
     *
     * 解决方案：
     * 使用此 Customizer 配置，在序列化（后端 -> 前端）时，自动将所有的 Long 类型转为 String 类型。
     * String 类型在 JS 中没有任何精度限制，且前端传回时 Spring MVC 能够自动将 String 转回 Long。
     *
     * 优势：
     * 使用 Customizer 接口属于"追加配置"，不会覆盖 Spring Boot 默认的 Jackson 配置（如日期格式、时区等），
     * 是最安全、最推荐的扩展方式。
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // 为 Long 类型 (包装类) 注册 ToStringSerializer
            // 效果：Long id = 1234567890123456789L  ->  json: "1234567890123456789"
            builder.serializerByType(Long.class, ToStringSerializer.instance);

            // 为 long 类型 (基本类型) 注册 ToStringSerializer
            // 效果：long count = 100L  ->  json: "100"
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        };
    }
}
