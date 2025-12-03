package com.himwei.testtemplatebackend.model.enums;

import lombok.Getter;

/**
 * 排班状态枚举
 */
@Getter
public enum ScheduleStatusEnum {
    STOP(0, "停诊"),
    NORMAL(1, "正常");

    private final int value;
    private final String text;

    ScheduleStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }
}
