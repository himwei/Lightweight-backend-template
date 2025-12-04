package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 挂号取消请求参数
 *
 * @author himwei
 */
@Data
public class CancelRegistrationRequest implements Serializable {

    /**
     * 挂号ID (必填，对应 t_schedule 表主键)
     */
    private Long regId;

    private static final long serialVersionUID = 1L;
}
