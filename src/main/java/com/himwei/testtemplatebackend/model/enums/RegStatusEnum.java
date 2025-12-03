package com.himwei.testtemplatebackend.model.enums;

import lombok.Getter;

/**
 * 挂号单状态枚举
 */
@Getter
public enum RegStatusEnum {

    BOOKED(0, "已预约"),
    FINISHED(1, "已完成"),
    CANCELED(2, "已取消");

    private final int value;
    private final String text;

    RegStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据 value 获取枚举 (可选，用于回显)
     */
    public static RegStatusEnum getEnumByValue(int value) {
        for (RegStatusEnum anEnum : RegStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
