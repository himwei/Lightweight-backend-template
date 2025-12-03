package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.ScheduleQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TSchedule;
import com.himwei.testtemplatebackend.model.vo.ScheduleVO;
import com.himwei.testtemplatebackend.service.TScheduleService;
import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.himwei.testtemplatebackend.constant.UserConstant;


@RestController
@RequestMapping("/schedule")
@Tag(name = "排班管理模块")
public class ScheduleController {

    @Resource
    private TScheduleService scheduleService;

    @Operation(summary = "分页查询排班列表")
    @Log(title = "排班管理", businessType = "分页查询排班列表", isSaveResponseData = false)
    @PostMapping("/list")
    @SaCheckRole(value = {UserConstant.DOCTOR_ROLE, UserConstant.PATIENT_ROLE},mode = SaMode.OR) // 只有医生和患者
    public BaseResponse<IPage<ScheduleVO>> listSchedules(@RequestBody ScheduleQueryDTO queryDTO) {
        IPage<ScheduleVO> result = scheduleService.listSchedules(queryDTO);
        return ResultUtils.success(result);
    }

    @Operation(summary = "发布排班 (管理员)")
    @Log(title = "排班管理", businessType = "发布排班", isSaveResponseData = false)
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE) // 只有管理员能排班
    public BaseResponse<Boolean> addSchedule(@RequestBody TSchedule schedule) {
        if (schedule.getDoctorId() == null || schedule.getWorkDate() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = scheduleService.addSchedule(schedule);
        return ResultUtils.success(result);
    }
}
