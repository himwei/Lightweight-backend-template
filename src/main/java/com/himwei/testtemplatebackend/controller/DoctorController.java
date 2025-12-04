package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.constant.UserConstant;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.DoctorAddDTO;
import com.himwei.testtemplatebackend.model.dto.DoctorQueryDTO;
import com.himwei.testtemplatebackend.model.dto.DoctorUpdateDTO;
import com.himwei.testtemplatebackend.model.entity.TDoctor;
import com.himwei.testtemplatebackend.model.vo.DoctorVO;
import com.himwei.testtemplatebackend.service.TDoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;

@RestController
@RequestMapping("/doctor")
//@Tag(name = "医生管理模块")
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


    /**
     * 新增医生 (事务：同时创建账号和档案)
     */
    @PostMapping("/add")
    @SaCheckRole("admin")
    public BaseResponse<Long> addDoctor(@RequestBody DoctorAddDTO request) {
        if (request == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        long newId = doctorService.addDoctor(request);
        return ResultUtils.success(newId);
    }

    /**
     * 更新医生信息 (只改档案，不改账号密码)
     */
    @PostMapping("/update")
    @SaCheckRole("admin")
    public BaseResponse<Boolean> updateDoctor(@RequestBody DoctorUpdateDTO request) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 这里简单实现，实际建议写在 Service 里校验
        TDoctor doctor = new TDoctor();
        BeanUtils.copyProperties(request, doctor);

        boolean result = doctorService.updateById(doctor);
        return ResultUtils.success(result);
    }
}
