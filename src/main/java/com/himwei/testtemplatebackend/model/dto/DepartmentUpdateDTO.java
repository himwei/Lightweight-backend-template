package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID (必填)
     */
    private Long id;

    private String deptName;
    private String deptCode;
    private String location;
    private String intro;
}
