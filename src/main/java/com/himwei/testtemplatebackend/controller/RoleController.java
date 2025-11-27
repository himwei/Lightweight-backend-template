package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.model.entity.SysRole;
import com.himwei.testtemplatebackend.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取所有角色列表 - 用于下拉选择
     * 建议：对于列表查询，设置 isSaveResponseData = false，避免日志表存入大量重复数据
     */
    @GetMapping("/list")
    @SaCheckRole("admin")
    @Log(title = "角色管理", businessType = "获取角色列表", isSaveResponseData = false) // <--- 加上这行
    public BaseResponse<List<SysRole>> listRoles() {
        List<SysRole> list = sysRoleService.lambdaQuery()
                .eq(SysRole::getStatus, 1)
                .list();
        return ResultUtils.success(list);
    }
}
