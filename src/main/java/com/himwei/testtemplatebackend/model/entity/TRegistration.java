package com.himwei.testtemplatebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 挂号记录表
 * @TableName t_registration
 */
@TableName(value ="t_registration")
@Data
public class TRegistration implements Serializable {
    /**
     * 挂号单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联 t_schedule 的 id
     */
    @TableField(value = "schedule_id")
    private Long scheduleId;

    /**
     * 患者ID (关联 sys_user)
     */
    @TableField(value = "patient_user_id")
    private Long patientUserId;

    /**
     * 状态 (0:已预约 1:已完成 2:已取消)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 实际就诊时间
     */
    @TableField(value = "visit_time")
    private LocalDateTime visitTime;

    /**
     * 医生诊断结果/医嘱
     */
    @TableField(value = "diagnosis")
    private String diagnosis;

    /**
     * 预约时间
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
