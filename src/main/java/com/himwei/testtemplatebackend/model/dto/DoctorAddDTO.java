package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DoctorAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 账号信息 (存 sys_user) ---
    private String userName;   // 账号
    private String passWord;   // 密码
    private String nickName;   // 姓名 (如: 张三)

    // --- 档案信息 (存 t_doctor) ---
    private Long deptId;       // 科室ID
    private String title;      // 职称
    private BigDecimal price;  // 挂号费
    private String intro;      // 简介
}
