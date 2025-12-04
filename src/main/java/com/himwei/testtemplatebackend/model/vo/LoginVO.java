package com.himwei.testtemplatebackend.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class LoginVO {

    private Long id;

    private String userName;

    private String nickName;

    private String avatar;

    private String token;

    private List<String> roles;
}
