package com.himwei.testtemplatebackend.model.dto;

import com.himwei.testtemplatebackend.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 科室查询请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DoctorQueryDTO extends PageDTO implements Serializable {
    /**
     * 科室ID (可选，用于筛选特定科室的医生)
     */
    private Long deptId;

    /**
     * 搜索关键词 (可选，匹配医生名字 或 简介)
     */
    private String keyword;


    private static final long serialVersionUID = 1L;
}
