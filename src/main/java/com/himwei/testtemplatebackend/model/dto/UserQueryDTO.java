package com.himwei.testtemplatebackend.model.dto;

import lombok.Data;

@Data
public class UserQueryDTO {

    private String username;

    private String nickname;

    private Integer status;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
