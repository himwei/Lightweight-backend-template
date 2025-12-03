package com.himwei.testtemplatebackend.model.dto;

import com.himwei.testtemplatebackend.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * 排班查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleQueryDTO extends PageDTO implements Serializable {
    /**
     * 按医生ID查
     */
    private Long doctorId;

    /**
     * 按科室ID查 (可选，用于患者端挂号页面筛选)
     */
    private Long deptId;

    /**
     * 状态 (1正常 0停诊)
     */
    private Integer status;
}
