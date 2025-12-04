package com.himwei.testtemplatebackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class UserAddDTO {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String passWord;

    private String nickName;

    private String email;

    private String phone;

    private Integer status = 1;

    /** 角色ID列表 */
    private List<Long> roleIds;
}
