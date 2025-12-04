package com.himwei.testtemplatebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.mapper.SysRoleMapper;
import com.himwei.testtemplatebackend.mapper.SysUserRoleMapper;
import com.himwei.testtemplatebackend.model.entity.SysRole;
import com.himwei.testtemplatebackend.model.entity.SysUserRole;
import com.himwei.testtemplatebackend.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<String> getRoleCodesByUserId(Long userId) {
        return sysRoleMapper.selectRoleCodesByUserId(userId);
    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, List<Long> roleIds) { // 改为 void
        // 1. 先删除原有角色
        // 不管删除多少条，哪怕是0条，都属于“清理成功”，继续往下走
        sysUserRoleMapper.deleteByUserId(userId);

        // 2. 再添加新角色
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysUserRole> userRoles = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }
            // 批量插入
            // 这里建议直接用 MyBatis-Plus 的 saveBatch，或者你自己的循环插入
            for (SysUserRole userRole : userRoles) {
                int insert = sysUserRoleMapper.insert(userRole);
                if (insert <= 0) {
                    // ❌ 如果插入失败，必须抛出异常，这样外层的 userRegister 才会回滚！
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "分配角色失败");
                }
            }
        }
    }
}
