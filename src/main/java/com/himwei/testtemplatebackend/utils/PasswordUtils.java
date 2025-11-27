package com.himwei.testtemplatebackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 密码加密工具类
 * 交给 Spring 管理，方便读取配置文件
 */
@Slf4j
@Component
public class PasswordUtils {

    /**
     * 从 application.yml 中读取 salt
     */
    @Value("${project.security.salt}")
    private String salt;

    /**
     * 加密方法
     * @param password 明文密码
     * @return 加密后的字符串 (MD5 hex)
     */
    public String encrypt(String password) {
        // 简单做法：密码 + 盐 -> MD5
        // 这里的逻辑可以随你定，比如：盐 + 密码 + 盐，或者多次加密
        String finalStr = password + salt;
        return DigestUtils.md5DigestAsHex(finalStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验密码
     * @param plainPassword 明文密码 (前端传来的)
     * @param encryptedPassword 密文密码 (数据库存的)
     * @return 是否匹配
     */
    public boolean matches(String plainPassword, String encryptedPassword) {
        String encrypt = encrypt(plainPassword);
        return encrypt.equals(encryptedPassword);
    }
}
