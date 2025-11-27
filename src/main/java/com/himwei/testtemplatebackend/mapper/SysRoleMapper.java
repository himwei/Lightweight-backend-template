package com.himwei.testtemplatebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.himwei.testtemplatebackend.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色编码列表
     */
    @Select("""
            SELECT r.role_code FROM sys_role r
            INNER JOIN sys_user_role ur ON r.id = ur.role_id
            WHERE ur.user_id = #{userId} AND r.deleted = 0 AND r.status = 1
            """)
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询角色列表
     */
    @Select("""
            SELECT r.* FROM sys_role r
            INNER JOIN sys_user_role ur ON r.id = ur.role_id
            WHERE ur.user_id = #{userId} AND r.deleted = 0
            """)
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
