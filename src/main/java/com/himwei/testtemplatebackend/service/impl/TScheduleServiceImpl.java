package com.himwei.testtemplatebackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.ScheduleAddDTO;
import com.himwei.testtemplatebackend.model.dto.ScheduleQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TSchedule;
import com.himwei.testtemplatebackend.model.enums.ScheduleStatusEnum;
import com.himwei.testtemplatebackend.model.vo.ScheduleVO;
import com.himwei.testtemplatebackend.service.TScheduleService;
import com.himwei.testtemplatebackend.mapper.TScheduleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author himwei
* @description 针对表【t_schedule(医生排班表)】的数据库操作Service实现
* @createDate 2025-12-02 19:05:38
*/
@Service
public class TScheduleServiceImpl extends ServiceImpl<TScheduleMapper, TSchedule>
    implements TScheduleService{

    @Override
    public IPage<ScheduleVO> listSchedules(ScheduleQueryDTO queryDTO) {
        Page<ScheduleVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        return this.baseMapper.selectScheduleVOPage(page, queryDTO);
    }

    @Override
    public boolean addSchedule(ScheduleAddDTO request) {
        // 1. 校验：同一医生、同一天、同一时段不能重复
        Long count = this.lambdaQuery()
                .eq(TSchedule::getDoctorId, request.getDoctorId())
                .eq(TSchedule::getWorkDate, request.getWorkDate())
                .eq(TSchedule::getShiftType, request.getShiftType())
                .count();

        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该时段已存在排班，请勿重复添加");
        }

        // 2. 插入数据
        TSchedule schedule = new TSchedule();
        BeanUtils.copyProperties(request, schedule);
        // 这里的 LocalDate 对应数据库的 Date 类型，MyBatisPlus 通常能自动处理
        // 如果数据库是 datetime，可能需要转一下:
        // schedule.setWorkDate(Date.from(request.getWorkDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        // 但建议数据库字段直接用 date 类型，实体类用 LocalDate


        // 初始已预约数为 0
        schedule.setBookedNum(0);
        schedule.setStatus(1); // 1:正常

        return this.save(schedule);
    }
}




