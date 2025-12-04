package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScheduleUpdateDTO implements Serializable {
    private Long id;         // 排班ID
    private Integer quota;   // 号源 (可选)
    private Integer status;  // 状态 (可选 0:停诊 1:正常)
}
