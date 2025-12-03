package com.himwei.testtemplatebackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.model.dto.ScheduleQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TSchedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.himwei.testtemplatebackend.model.vo.ScheduleVO;

/**
* @author himwei
* @description 针对表【t_schedule(医生排班表)】的数据库操作Service
* @createDate 2025-12-02 19:05:38
*/
public interface TScheduleService extends IService<TSchedule> {
    boolean addSchedule(TSchedule schedule); // 发布排班
    IPage<ScheduleVO> listSchedules(ScheduleQueryDTO queryDTO); // 查询排班
}
