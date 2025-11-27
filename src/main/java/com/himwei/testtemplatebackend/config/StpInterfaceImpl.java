package com.himwei.testtemplatebackend.config;

import cn.dev33.satoken.stp.StpInterface;
import com.himwei.testtemplatebackend.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token 权限认证接口实现
 * 用于获取用户的角色和权限列表
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 返回一个账号所拥有的权限码集合
     * 我们只用角色校验，这里返回空
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        return sysRoleService.getRoleCodesByUserId(userId);
    }
}
