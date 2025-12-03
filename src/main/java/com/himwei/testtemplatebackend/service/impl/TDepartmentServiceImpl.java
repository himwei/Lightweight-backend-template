package com.himwei.testtemplatebackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.model.dto.DepartmentQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TDepartment;
import com.himwei.testtemplatebackend.service.TDepartmentService;
import com.himwei.testtemplatebackend.mapper.TDepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author himwei
* @description 针对表【t_department(科室管理表)】的数据库操作Service实现
* @createDate 2025-12-02 19:05:38
*/
@Service
public class TDepartmentServiceImpl extends ServiceImpl<TDepartmentMapper, TDepartment>
    implements TDepartmentService{

    @Override
    public IPage<TDepartment> getDepartmentList(DepartmentQueryDTO  queryDTO) {
        // 1. 构建分页对象 (MyBatis-Plus)
        Page<TDepartment> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<TDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getDeptName()), TDepartment::getDeptName, queryDTO.getDeptName());

        // 3. 默认按 ID 升序 (或者按创建时间倒序)
        wrapper.orderByAsc(TDepartment::getId);

        // 4. 执行分页查询
        return this.page(page, wrapper);
    }
}




