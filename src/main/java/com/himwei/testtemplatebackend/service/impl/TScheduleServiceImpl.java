package com.himwei.testtemplatebackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.ScheduleQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TSchedule;
import com.himwei.testtemplatebackend.model.enums.ScheduleStatusEnum;
import com.himwei.testtemplatebackend.model.vo.ScheduleVO;
import com.himwei.testtemplatebackend.service.TScheduleService;
import com.himwei.testtemplatebackend.mapper.TScheduleMapper;
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
    public boolean addSchedule(TSchedule schedule) {
        // 1. 校验：同一医生、同一天、同一时段不能重复排班
        Long count = this.lambdaQuery()
                .eq(TSchedule::getDoctorId, schedule.getDoctorId())
                .eq(TSchedule::getWorkDate, schedule.getWorkDate())
                .eq(TSchedule::getShiftType, schedule.getShiftType())
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该时段已存在排班，请勿重复添加");
        }

        // 2. 初始化数据
        schedule.setBookedNum(0);
        schedule.setStatus(ScheduleStatusEnum.NORMAL.getValue());

        return this.save(schedule);
    }
}




