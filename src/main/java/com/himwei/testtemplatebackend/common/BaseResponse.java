package com.himwei.testtemplatebackend.common;

import com.himwei.testtemplatebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 全参构造函数
     *
     * @param code 状态码
     * @param data 返回数据
     * @param message 返回信息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 构造函数（不包含message）
     *
     * @param code 状态码
     * @param data 返回数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 根据错误码构造返回对象
     *
     * @param errorCode 错误码枚举
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
