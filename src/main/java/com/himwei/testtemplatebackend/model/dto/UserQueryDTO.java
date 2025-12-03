package com.himwei.testtemplatebackend.model.dto;

import com.himwei.testtemplatebackend.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends PageDTO {

    private String username;

    private String nickname;

    private Integer status;
}
