package com.himwei.testtemplatebackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 医生排班表
 * @TableName t_schedule
 */
@TableName(value ="t_schedule")
@Data
public class TSchedule implements Serializable {
    /**
     * 排班ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联 t_doctor 的 id
     */
    @TableField(value = "doctor_id")
    private Long doctorId;

    /**
     * 出诊日期 (2023-11-11)
     */
    @TableField(value = "work_date")
    private Date workDate;

    /**
     * 时段 (1:上午 2:下午)
     */
    @TableField(value = "shift_type")
    private Integer shiftType;

    /**
     * 号源总数
     */
    @TableField(value = "quota")
    private Integer quota;

    /**
     * 已挂号数量
     */
    @TableField(value = "booked_num")
    private Integer bookedNum;

    /**
     * 状态 (1:正常 0:停诊)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     *
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
