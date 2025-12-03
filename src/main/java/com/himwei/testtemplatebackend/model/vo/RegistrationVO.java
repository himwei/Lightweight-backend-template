package com.himwei.testtemplatebackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 挂号记录表VO 需关联患者信息
 */
@Data
public class RegistrationVO implements Serializable {
    private static final long serialVersionUID = 146919707155099858L;
    private Long id;              // 挂号单 ID

    // --- 排班/就诊信息 ---
    private Date workDate;        //就诊日期
    private Integer shiftType;    //时段 (1:上午 2:下午)
    private Integer status;       //状态 (0:预约 1:完成 2:取消)
    private String diagnosis;     //医嘱

    // --- 医生/科室信息 ---
    private String doctorName;    //医生姓名
    private String deptName;      //科室名称
    private BigDecimal price;     //挂号费

    // --- 患者信息 (医生端看的时候需要) ---
    private String patientName;   //患者姓名
    private String patientPhone;  //患者电话

}
