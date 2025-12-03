package com.himwei.testtemplatebackend.model.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ScheduleVO implements Serializable {
    private Long id;
    private Long doctorId;
    private String doctorName;   // 医生姓名
    private String deptName;     // 科室名称
    private String title;        // 医生职称
    private BigDecimal price;    // 挂号费

    private Date workDate;       // 出诊日期
    private Integer shiftType;   // 1上午 2下午
    private Integer quota;       // 总号源
    private Integer bookedNum;   // 已挂号数
    private Integer status;      // 状态
}
