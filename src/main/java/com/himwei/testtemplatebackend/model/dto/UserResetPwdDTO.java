package com.himwei.testtemplatebackend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserResetPwdDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

}
