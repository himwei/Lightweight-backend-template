package com.himwei.testtemplatebackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.model.dto.DepartmentQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.himwei.testtemplatebackend.model.dto.DiagnosisRequest;

import java.util.List;

/**
* @author himwei
* @description 针对表【t_department(科室管理表)】的数据库操作Service
* @createDate 2025-12-02 19:05:38
*/
public interface TDepartmentService extends IService<TDepartment> {
    // 获取科室列表
    IPage<TDepartment> getDepartmentList(DepartmentQueryDTO  queryDTO);

}
