package com.himwei.testtemplatebackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.model.dto.DepartmentQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TDepartment;
import com.himwei.testtemplatebackend.service.TDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dept")
@Tag(name = "科室管理模块")
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

}
