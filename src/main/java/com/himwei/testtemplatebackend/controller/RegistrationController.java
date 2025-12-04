package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.PageDTO;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.mapper.TRegistrationMapper;
import com.himwei.testtemplatebackend.model.dto.CancelRegistrationRequest;
import com.himwei.testtemplatebackend.model.dto.DiagnosisRequest;
import com.himwei.testtemplatebackend.model.dto.RegSubmitRequest;
import com.himwei.testtemplatebackend.model.entity.TDoctor;
import com.himwei.testtemplatebackend.model.enums.RoleEnum;
import com.himwei.testtemplatebackend.model.vo.RegistrationVO;
import com.himwei.testtemplatebackend.service.TDoctorService;
import com.himwei.testtemplatebackend.service.TRegistrationService;
import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import com.himwei.testtemplatebackend.constant.UserConstant;

import java.util.List;

@RestController
@RequestMapping("/reg")
//@Tag(name = "挂号管理模块")
public class RegistrationController {

    @Resource
    private TRegistrationService registrationService;

    @Resource
    private TDoctorService doctorService;

    @Resource
    private TRegistrationMapper registrationMapper;

    @Operation(summary = "医生录入医嘱 (接诊)")
    @Log(title = "挂号管理", businessType = "医生录入医嘱", isSaveResponseData = false)
    @PostMapping("/diagnosis")
    @SaCheckRole(UserConstant.DOCTOR_ROLE) // 只有医生能操作
    public BaseResponse<Boolean> diagnosis(@RequestBody DiagnosisRequest request) {
        if (request == null || request.getRegId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = registrationService.diagnosis(request);
        return ResultUtils.success(result);
    }

    /**
     * 提交挂号 (患者专用)
     */
    @Operation(summary = "提交挂号申请")
    @PostMapping("/submit")
    @Log(title = "挂号管理", businessType = "提交挂号申请", isSaveResponseData = false)
    @SaCheckRole("patient") // 只有患者角色能挂号
    public BaseResponse<Long> submitRegistration(@RequestBody RegSubmitRequest request) {
        if (request == null || request.getScheduleId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long regId = registrationService.submitRegistration(request);
        return ResultUtils.success(regId);
    }

    /**
     * 提交挂号 (患者专用)
     */
    @Operation(summary = "取消挂号")
    @PostMapping("/cancel")
    @Log(title = "挂号管理", businessType = "取消挂号", isSaveResponseData = false)
    @SaCheckRole("patient") // 只有患者角色能挂号
    public BaseResponse<Boolean> cancelRegistration(@RequestBody CancelRegistrationRequest request) {
        if (request == null || request.getRegId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean res = registrationService.cancelRegistration(request);
        return ResultUtils.success(res);
    }


    /**
     * 查询我的挂号记录
     */
    @Operation(summary = "查询挂号记录 (智能判断角色)")
    @Log(title = "挂号管理", businessType = "查询挂号记录 (智能判断角色)", isSaveResponseData = false)
    @PostMapping("/my-list")
//    @SaCheckRole("patient")
    public BaseResponse<IPage<RegistrationVO>> getMyRegistrations(@RequestBody PageDTO pageDTO) {
        // 获取当前角色
        List<String> roleList = StpUtil.getRoleList();
        Long userId = StpUtil.getLoginIdAsLong();

        Page<RegistrationVO> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        if (roleList.contains("doctor")) {
            // 如果是医生：查 t_doctor 表拿到 doctorId，再查挂了他的号
            TDoctor doc = doctorService.lambdaQuery().eq(TDoctor::getUserId, userId).one();
            if (doc != null) {
                return ResultUtils.success(
                        registrationMapper.selectRegistrationVOList(page, null, doc.getId())
                );
            }
        }

        // 默认：按患者查 (传入 userId 作为 patientId)
        return ResultUtils.success(
                registrationMapper.selectRegistrationVOList(page, userId, null)
        );
    }
}
