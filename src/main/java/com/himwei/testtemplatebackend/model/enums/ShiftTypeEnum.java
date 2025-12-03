package com.himwei.testtemplatebackend.model.enums;

import lombok.Getter;

/**
 * 排班时段枚举
 */
@Getter
public enum ShiftTypeEnum {
    MORNING(1, "上午"),
    AFTERNOON(2, "下午");

    private final int value;
    private final String text;

    ShiftTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }
}
