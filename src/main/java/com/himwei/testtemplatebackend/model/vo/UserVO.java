package com.himwei.testtemplatebackend.model.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {

    private Long id;

    private String userName;

    private String nickName;

    private String avatar;

    private String email;

    private String phone;

    private Integer status;

    private LocalDateTime createTime;

    /** 角色编码列表 */
    private List<String> roles;

    /** 角色名称列表 */
    private List<String> roleNames;
}
