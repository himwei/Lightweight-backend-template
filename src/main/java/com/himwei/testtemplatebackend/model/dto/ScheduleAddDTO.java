package com.himwei.testtemplatebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ScheduleAddDTO implements Serializable {
    private Long doctorId;

    /**
     * 出诊日期 (格式 yyyy-MM-dd)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    /**
     * 时段 (1:上午 2:下午)
     */
    private Integer shiftType;

    /**
     * 号源数量
     */
    private Integer quota;
}
