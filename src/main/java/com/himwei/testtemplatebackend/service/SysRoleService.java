package com.himwei.testtemplatebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.himwei.testtemplatebackend.model.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID获取角色编码列表
     */
    List<String> getRoleCodesByUserId(Long userId);

    /**
     * 根据用户ID获取角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);

    /**
     * 给用户分配角色
     */
    void assignRoles(Long userId, List<Long> roleIds);
}
