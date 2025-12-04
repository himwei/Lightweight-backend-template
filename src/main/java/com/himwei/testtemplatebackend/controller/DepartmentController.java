package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.DeleteRequest;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.DepartmentAddDTO;
import com.himwei.testtemplatebackend.model.dto.DepartmentQueryDTO;
import com.himwei.testtemplatebackend.model.dto.DepartmentUpdateDTO;
import com.himwei.testtemplatebackend.model.entity.TDepartment;
import com.himwei.testtemplatebackend.service.TDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dept")
//@Tag(name = "科室管理模块") // 加个tag后 OPENAPI那里会无法正确识别controller类别 导致会放到service.ts中 而不是DepartmentControllerService.ts中 也有可能是中文的问题
public class DepartmentController {

    @Resource
    private TDepartmentService departmentService;

    @Operation(summary = "分页获取科室列表")
    @Log(title = "科室管理", businessType = "获取科室列表", isSaveResponseData = false)
    @PostMapping("/list")
    public BaseResponse<IPage<TDepartment>> getDepartmentList(@RequestBody DepartmentQueryDTO queryDTO) {
        // 不需要判空，Spring 会把空 JSON 转为默认对象
        IPage<TDepartment> page = departmentService.getDepartmentList(queryDTO);
        return ResultUtils.success(page);
    }


    // 1. 新增
    @PostMapping("/add")
    @SaCheckRole("admin")
    @Operation(summary = "新增科室")
    @Log(title = "科室管理", businessType = "新增科室", isSaveResponseData = false)
    public BaseResponse<Long> addDepartment(@RequestBody DepartmentAddDTO request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1. DTO -> Entity
        TDepartment department = new TDepartment();
        BeanUtils.copyProperties(request, department);

        // 2. 保存
        boolean result = departmentService.save(department);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "新增失败");
        }

        return ResultUtils.success(department.getId());
    }

    // 2. 修改
    @PostMapping("/update")
    @SaCheckRole("admin")
    @Operation(summary = "修改科室")
    @Log(title = "科室管理", businessType = "修改科室", isSaveResponseData = false)
    public BaseResponse<Boolean> updateDepartment(@RequestBody DepartmentUpdateDTO request) {
        if (request == null || request.getId() == null || request.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1. DTO -> Entity
        TDepartment department = new TDepartment();
        BeanUtils.copyProperties(request, department);

        // 2. 更新
        boolean result = departmentService.updateById(department);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }

        return ResultUtils.success(true);
    }

    // 3. 删除 (逻辑删除)
    @PostMapping("/delete")
    @SaCheckRole("admin")
    @Operation(summary = "删除科室")
    @Log(title = "科室管理", businessType = "删除科室", isSaveResponseData = false)
    public BaseResponse<Boolean> deleteDepartment(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = departmentService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

}
