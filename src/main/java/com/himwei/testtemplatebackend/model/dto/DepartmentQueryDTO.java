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
public class DepartmentQueryDTO  extends PageDTO implements Serializable {
    /**
     * 科室名称 (可选，用于模糊搜索)
     */
    private String deptName;

    private static final long serialVersionUID = 1L;
}
