package com.himwei.testtemplatebackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.model.dto.ScheduleQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.himwei.testtemplatebackend.model.vo.ScheduleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author himwei
* @description 针对表【t_schedule(医生排班表)】的数据库操作Mapper
* @createDate 2025-12-02 19:05:38
* @Entity com.himwei.testtemplatebackend.model.entity.TSchedule
*/
public interface TScheduleMapper extends BaseMapper<TSchedule> {
    // 注意：第一个参数必须是 IPage
    IPage<ScheduleVO> selectScheduleVOPage(IPage<ScheduleVO> page, @Param("query") ScheduleQueryDTO queryDTO);

    /**
     * 扣减号源 (乐观锁)
     * SQL: update t_schedule set booked_num = booked_num + 1 where id = ? and booked_num < quota
     * @param scheduleId 排班ID
     * @return 更新行数 (1表示成功，0表示失败/号已满)
     */
    @Update("update t_schedule set booked_num = booked_num + 1 where id = #{scheduleId} and booked_num < quota")
    int increaseBookedNum(@Param("scheduleId") Long scheduleId);

    /**
     * 取消：回退号源
     * 逻辑：已预约数 - 1，且不能小于 0 (防止数据错乱)
     */
    @Update("update t_schedule set booked_num = booked_num - 1 where id = #{scheduleId} and booked_num > 0")
    int decreaseBookedNum(@Param("scheduleId") Long scheduleId);
}




