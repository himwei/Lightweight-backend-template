package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 科室名称 (必填)
     */
    private String deptName;

    /**
     * 科室编码
     */
    private String deptCode;

    /**
     * 诊室位置
     */
    private String location;

    /**
     * 科室介绍
     */
    private String intro;
}
