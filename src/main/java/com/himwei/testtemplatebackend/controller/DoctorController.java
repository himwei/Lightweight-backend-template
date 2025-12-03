package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.constant.UserConstant;
import com.himwei.testtemplatebackend.model.dto.DoctorQueryDTO;
import com.himwei.testtemplatebackend.model.vo.DoctorVO;
import com.himwei.testtemplatebackend.service.TDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;

@RestController
@RequestMapping("/doctor")
@Tag(name = "医生管理模块")
public class DoctorController {

    @Resource
    private TDoctorService doctorService;

    @Operation(summary = "分页获取医生列表 (支持按科室/姓名搜索)")
    @Log(title = "医生管理", businessType = "获取医生列表", isSaveResponseData = false)
    @PostMapping("/list")
    public BaseResponse<IPage<DoctorVO>> listDoctors(@RequestBody DoctorQueryDTO queryDTO) {
        // 1. 校验 (可选，比如限制 pageSize 最大不能超过100)
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }

        // 2. 查询
        IPage<DoctorVO> result = doctorService.pageDoctors(queryDTO);

        // 3. 返回
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取医生个人信息")
    @Log(title = "医生管理", businessType = "获取医生个人信息", isSaveResponseData = false)
    @SaCheckRole(UserConstant.DOCTOR_ROLE) // 只有医生能操作
    @PostMapping("/profile")
    public BaseResponse<DoctorVO> getProfile() {
        long userId = StpUtil.getLoginIdAsLong();
        DoctorVO doctorProfile = doctorService.getDoctorProfile(userId);
        return ResultUtils.success(doctorProfile);
    }
}
