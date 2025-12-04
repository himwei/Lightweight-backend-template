package com.himwei.testtemplatebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 医生信息表
 * @TableName t_doctor
 */
@TableName(value ="t_doctor")
@Data
public class TDoctor implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联 sys_user 的 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 关联 t_department 的 id
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 职称 (主任/副主任/主治)
     */
    @TableField(value = "title")
    private String title;

    /**
     * 挂号费
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 医生简介
     */
    @TableField(value = "intro")
    private String intro;

    /**
     *
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
