package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    private String nickName;
    private String phone;
    private String email;
    private String avatar; // 头像地址
}
