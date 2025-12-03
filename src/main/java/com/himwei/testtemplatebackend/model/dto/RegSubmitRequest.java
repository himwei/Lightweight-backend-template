package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 挂号提交请求参数
 * @author himwei
 */
@Data
public class RegSubmitRequest implements Serializable {

    /**
     * 排班ID (必填，对应 t_schedule 表主键)
     */
    private Long scheduleId;

    private static final long serialVersionUID = 1L;
}
