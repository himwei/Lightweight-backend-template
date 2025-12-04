package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DoctorUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // t_doctor 的主键 ID
    private Long id;

    // 允许修改姓名
    private String nickname;

    // 档案信息
    private Long deptId;
    private String title;
    private BigDecimal price;
    private String intro;
}
