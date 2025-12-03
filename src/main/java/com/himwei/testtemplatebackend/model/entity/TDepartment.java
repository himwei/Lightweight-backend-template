package com.himwei.testtemplatebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 科室管理表
 * @TableName t_department
 */
@TableName(value ="t_department")
@Data
public class TDepartment implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 科室名称 (如: 心内科)
     */
    @TableField(value = "dept_name")
    private String deptName;

    /**
     * 科室编码
     */
    @TableField(value = "dept_code")
    private String deptCode;

    /**
     * 诊室位置
     */
    @TableField(value = "location")
    private String location;

    /**
     * 科室介绍
     */
    @TableField(value = "intro")
    private String intro;

    /**
     *
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
