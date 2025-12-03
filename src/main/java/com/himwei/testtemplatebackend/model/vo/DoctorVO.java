package com.himwei.testtemplatebackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 医生VO 需关联医生信息
 */
@Data
public class DoctorVO implements Serializable {
    private static final long serialVersionUID = -9005682932456869875L;
    private Long id;            // t_doctor 的 ID (用于挂号传参)
    private String doctorName;  // 医生姓名 (来自 sys_user.nickname)
    private String deptName;    // 科室名称 (来自 t_department.dept_name)
    private String avatar;      // 头像 (来自 sys_user.avatar)
    private String title;       // 职称
    private BigDecimal price;   // 挂号费
    private String intro;       // 简介

}
