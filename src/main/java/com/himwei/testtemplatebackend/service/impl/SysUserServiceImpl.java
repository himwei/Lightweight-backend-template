package com.himwei.testtemplatebackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.mapper.SysUserMapper;
import com.himwei.testtemplatebackend.model.dto.*;
import com.himwei.testtemplatebackend.model.entity.SysRole;
import com.himwei.testtemplatebackend.model.entity.SysUser;
import com.himwei.testtemplatebackend.model.enums.RoleEnum;
import com.himwei.testtemplatebackend.model.vo.LoginVO;
import com.himwei.testtemplatebackend.model.vo.UserVO;
import com.himwei.testtemplatebackend.service.SysRoleService;
import com.himwei.testtemplatebackend.service.SysUserService;
import com.himwei.testtemplatebackend.utils.PasswordUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    private static final String SALT = "himwei";

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private PasswordUtils passwordUtils; // <--- 注入刚刚写的工具类

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 校验参数
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 查询用户
        SysUser user = this.lambdaQuery()
                .eq(SysUser::getUserName, username)
                .one();

        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // ★★★ 修改点：使用工具类校验密码 ★★★
        // 将前端传来的明文密码加密后，与数据库的密文进行比对
        if (!passwordUtils.matches(loginDTO.getPassword(), user.getPassWord())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 校验状态
        if (user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "账号已被禁用");
        }

        // 登录
        StpUtil.login(user.getId());

        // 获取角色
        List<String> roles = sysRoleService.getRoleCodesByUserId(user.getId());

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setId(user.getId());
        loginVO.setUsername(user.getUserName());
        loginVO.setNickname(user.getNickName());
        loginVO.setAvatar(user.getAvatar());
        loginVO.setToken(StpUtil.getTokenValue());
        loginVO.setRoles(roles);

        return loginVO;
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        return getUserById(userId);
    }

    @Override
    public IPage<UserVO> pageUsers(UserQueryDTO queryDTO) {
        Page<SysUser> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getUsername()), SysUser::getUserName, queryDTO.getUsername())
                .like(StrUtil.isNotBlank(queryDTO.getNickname()), SysUser::getNickName, queryDTO.getNickname())
                .eq(queryDTO.getStatus() != null, SysUser::getStatus, queryDTO.getStatus())
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = this.page(page, wrapper);

        // 转换为 VO
        return userPage.convert(this::convertToVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addUser(UserAddDTO addDTO) {
        // 检查用户名是否存在
        long count = this.lambdaQuery()
                .eq(SysUser::getUserName, addDTO.getUsername())
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // 创建用户
        SysUser user = new SysUser();
        BeanUtil.copyProperties(addDTO, user);
        // 实际项目中应该加密存储密码
        // user.setPassword(DigestUtils.md5DigestAsHex((addDTO.getPassword() + SALT).getBytes()));

        this.save(user);

        // 分配角色
        if (addDTO.getRoleIds() != null && !addDTO.getRoleIds().isEmpty()) {
            sysRoleService.assignRoles(user.getId(), addDTO.getRoleIds());
        }

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO updateDTO) {
        // 检查用户是否存在
        SysUser existUser = this.getById(updateDTO.getId());
        if (existUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 更新用户
        SysUser user = new SysUser();
        BeanUtil.copyProperties(updateDTO, user);
        this.updateById(user);

        // 更新角色
        if (updateDTO.getRoleIds() != null) {
            sysRoleService.assignRoles(updateDTO.getId(), updateDTO.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 不允许删除自己
        if (id.equals(StpUtil.getLoginIdAsLong())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能删除自己");
        }

        this.removeById(id);
    }

    @Override
    public UserVO getUserById(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        return convertToVO(user);
    }

    /**
     * 转换为 VO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);

        // 获取角色信息
        List<SysRole> roles = sysRoleService.getRolesByUserId(user.getId());
        vo.setRoles(roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList()));
        vo.setRoleNames(roles.stream().map(SysRole::getRoleName).collect(Collectors.toList()));

        return vo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long userRegister(UserRegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String checkPassword = registerDTO.getCheckPassword();

        // 1. 校验密码一致性
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 2. 校验用户名是否已存在
        long count = this.lambdaQuery()
                .eq(SysUser::getUserName, username)
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // 3. 创建用户
        SysUser user = new SysUser();
        user.setUserName(username);
        // TODO: 后续这里应该加盐加密，例如: DigestUtils.md5DigestAsHex((password + SALT).getBytes())
        // ★★★ 修改点：使用工具类加密密码 ★★★
        user.setPassWord(passwordUtils.encrypt(registerDTO.getPassword()));
        user.setNickName(registerDTO.getNickname());
        user.setStatus(1); // 默认正常

        // 保存用户
        this.save(user);

        // 4. 分配默认角色 (假设 "user" 角色的 ID 是 2)
        // 你需要先去数据库 sys_role 表里确认一下普通用户的角色ID是多少
        // 或者根据 role_code = 'user' 去查 ID
//        Long defaultRoleId = 2L;

        // 如果想做的严谨一点，可以先查一下 'user' 角色的 ID
//        SysRole userRole = sysRoleService.lambdaQuery().eq(SysRole::getRoleCode, "user").one();
//        if (userRole != null) {
//            defaultRoleId = userRole.getId();
//        }

//        // 分配角色
//        List<Long> roleIds = new ArrayList<>();
//        roleIds.add(defaultRoleId);
//        sysRoleService.assignRoles(user.getId(), roleIds);

        // 4. 分配默认角色
        // 使用 JDK 9+ 提供的 List.of() 方法，创建一个只包含 "普通用户ID" 的不可变列表
        // 优点：语法简洁、内存占用极低、线程安全
        // 注意：List.of() 返回的集合是不可变的，不能执行 add/remove 操作，但作为查询参数传递非常合适
        sysRoleService.assignRoles(user.getId(), List.of(RoleEnum.USER.getId()));

        return user.getId();
    }
}
