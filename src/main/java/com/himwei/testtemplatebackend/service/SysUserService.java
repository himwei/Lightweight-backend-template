package com.himwei.testtemplatebackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.himwei.testtemplatebackend.model.dto.*;
import com.himwei.testtemplatebackend.model.entity.SysUser;
import com.himwei.testtemplatebackend.model.vo.LoginVO;
import com.himwei.testtemplatebackend.model.vo.UserVO;

public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 获取当前登录用户信息
     */
    UserVO getCurrentUser();

    /**
     * 分页查询用户
     */
    IPage<UserVO> pageUsers(UserQueryDTO queryDTO);

    /**
     * 新增用户
     */
    Long addUser(UserAddDTO addDTO);

    /**
     * 修改用户
     */
    void updateUser(UserUpdateDTO updateDTO);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 根据ID获取用户详情
     */
    UserVO getUserById(Long id);

    /**
     * 用户注册
     * @param registerDTO 注册参数
     * @return 新用户ID
     */
    Long userRegister(UserRegisterDTO registerDTO);
}
