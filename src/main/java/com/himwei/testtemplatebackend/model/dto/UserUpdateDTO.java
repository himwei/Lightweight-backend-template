package com.himwei.testtemplatebackend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class UserUpdateDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String nickname;

    private String email;

    private String phone;

    private Integer status;

    /** 角色ID列表 */
    private List<Long> roleIds;
}
