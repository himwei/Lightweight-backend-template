package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.model.dto.*;
import com.himwei.testtemplatebackend.model.vo.LoginVO;
import com.himwei.testtemplatebackend.model.vo.UserVO;
import com.himwei.testtemplatebackend.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Log(title = "用户管理", businessType = "用户注册", isSaveRequestData = false) // 不记录密码日志
    public BaseResponse<Long> userRegister(@RequestBody @Valid UserRegisterDTO registerDTO) {
        Long userId = sysUserService.userRegister(registerDTO);
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     * 注意：设置 isSaveRequestData = false 是为了防止将密码记录到数据库日志中
     */
    @PostMapping("/login")
    @Log(title = "用户管理", businessType = "用户登录", isSaveRequestData = false)
    public BaseResponse<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginVO loginVO = sysUserService.login(loginDTO);
        return ResultUtils.success(loginVO);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Log(title = "用户管理", businessType = "用户退出")
    public BaseResponse<Void> logout() {
        StpUtil.logout();
        return ResultUtils.success(null);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    @Log(title = "用户管理", businessType = "获取当前用户")
    public BaseResponse<UserVO> getCurrentUser() {
        UserVO userVO = sysUserService.getCurrentUser();
        return ResultUtils.success(userVO);
    }

    /**
     * 分页查询用户列表 - 需要 admin 角色
     * 注意：查询列表通常数据量较大，建议设置 isSaveResponseData = false 不记录返回结果，节省数据库空间
     */
    @GetMapping("/page")
    @SaCheckRole("admin")
    @Log(title = "用户管理", businessType = "分页查询用户", isSaveResponseData = false)
    public BaseResponse<IPage<UserVO>> pageUsers(UserQueryDTO queryDTO) {
        IPage<UserVO> page = sysUserService.pageUsers(queryDTO);
        return ResultUtils.success(page);
    }

    /**
     * 新增用户 - 需要 admin 角色
     */
    @PostMapping("/add")
    @SaCheckRole("admin")
    @Log(title = "用户管理", businessType = "新增用户")
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddDTO addDTO) {
        Long id = sysUserService.addUser(addDTO);
        return ResultUtils.success(id);
    }

    /**
     * 修改用户 - 需要 admin 角色
     */
    @PostMapping("/update")
    @SaCheckRole("admin")
    @Log(title = "用户管理", businessType = "修改用户")
    public BaseResponse<Void> updateUser(@RequestBody @Valid UserUpdateDTO updateDTO) {
        sysUserService.updateUser(updateDTO);
        return ResultUtils.success(null);
    }

    /**
     * 删除用户 - 需要 admin 角色
     */
    @PostMapping("/delete/{id}")
    @SaCheckRole("admin")
    @Log(title = "用户管理", businessType = "删除用户")
    public BaseResponse<Void> deleteUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return ResultUtils.success(null);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/detail/{id}")
    @SaCheckRole("admin")
    @Log(title = "用户管理", businessType = "获取用户详情")
    public BaseResponse<UserVO> getUserDetail(@PathVariable Long id) {
        UserVO userVO = sysUserService.getUserById(id);
        return ResultUtils.success(userVO);
    }
}
