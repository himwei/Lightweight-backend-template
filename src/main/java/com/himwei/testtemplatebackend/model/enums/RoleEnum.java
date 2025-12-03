package com.himwei.testtemplatebackend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统角色枚举
 * 注意：这里的 ID 必须与数据库 sys_role 表中的初始数据保持一致
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN("admin", 1L, "超级管理员"),
    USER("user", 2L, "普通用户"),
    DOCTOR("doctor", 4L, "医生"),
    PATIENT("patient", 5L, "患者");

    private final String code;
    private final Long id;
    private final String desc;

    /**
     * 根据 code 获取枚举
     */
    public static RoleEnum getByCode(String code) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }

    /**
     * 根据RoleEnum获取 code
     */
    public static String getCodeByRole(RoleEnum role) {
        return role.getCode();
    }
}
