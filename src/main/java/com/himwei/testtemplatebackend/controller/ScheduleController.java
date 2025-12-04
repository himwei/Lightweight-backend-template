package com.himwei.testtemplatebackend.controller;

import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.DeleteRequest;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.ScheduleAddDTO;
import com.himwei.testtemplatebackend.model.dto.ScheduleQueryDTO;
import com.himwei.testtemplatebackend.model.dto.ScheduleUpdateDTO;
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
//@Tag(name = "排班管理模块")
public class ScheduleController {

    @Resource
    private TScheduleService scheduleService;

    @Operation(summary = "分页查询排班列表")
    @Log(title = "排班管理", businessType = "分页查询排班列表", isSaveResponseData = false)
    @PostMapping("/list")
    public BaseResponse<IPage<ScheduleVO>> listSchedules(@RequestBody ScheduleQueryDTO queryDTO) {
        IPage<ScheduleVO> result = scheduleService.listSchedules(queryDTO);
        return ResultUtils.success(result);
    }

    @Operation(summary = "发布排班 (管理员)")
    @Log(title = "排班管理", businessType = "发布排班", isSaveResponseData = false)
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE) // 只有管理员能排班
    public BaseResponse<Boolean> addSchedule(@RequestBody ScheduleAddDTO schedule) {
        if (schedule.getDoctorId() == null || schedule.getWorkDate() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = scheduleService.addSchedule(schedule);
        return ResultUtils.success(result);
    }


    /**
     * 删除排班
     * 逻辑：只有没人挂号时才能删除
     */
    @PostMapping("/delete")
    @SaCheckRole("admin")
    @Operation(summary = "删除排班 (管理员)")
    @Log(title = "排班管理", businessType = "删除排班", isSaveResponseData = false)
    public BaseResponse<Boolean> deleteSchedule(@RequestBody DeleteRequest request) {
        if (request == null || request.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        TSchedule schedule = scheduleService.getById(request.getId());
        if (schedule == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 🚨 关键校验
        if (schedule.getBookedNum() > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已有患者挂号，无法删除，请尝试【停诊】");
        }

        boolean result = scheduleService.removeById(request.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新排班 (修改号源 或 停诊)
     */
    @PostMapping("/update")
    @SaCheckRole("admin")
    @Operation(summary = "更新排班 (管理员)")
    @Log(title = "排班管理", businessType = "更新排班 (修改号源 或 停诊)", isSaveResponseData = false)
    public BaseResponse<Boolean> updateSchedule(@RequestBody ScheduleUpdateDTO request) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        TSchedule oldSchedule = scheduleService.getById(request.getId());
        if (oldSchedule == null) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        TSchedule updateBean = new TSchedule();
        updateBean.setId(request.getId());

        // 1. 修改状态
        if (request.getStatus() != null) {
            updateBean.setStatus(request.getStatus());
        }

        // 2. 修改号源 (不能小于已挂号数)
        if (request.getQuota() != null) {
            if (request.getQuota() < oldSchedule.getBookedNum()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "总号源不能少于已挂号数量");
            }
            updateBean.setQuota(request.getQuota());
        }

        boolean result = scheduleService.updateById(updateBean);
        return ResultUtils.success(result);
    }
}
