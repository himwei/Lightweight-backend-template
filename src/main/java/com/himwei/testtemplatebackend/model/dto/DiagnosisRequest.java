package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 录入医嘱
 */
@Data
public class DiagnosisRequest implements Serializable {
    /**
     * 挂号单 ID
     */
    private Long regId;

    /**
     * 医嘱/诊断结果 (例如: "感冒，建议多喝热水")
     */
    private String diagnosis;
}
