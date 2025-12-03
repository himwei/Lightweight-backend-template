package com.himwei.testtemplatebackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.model.dto.DoctorQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TDoctor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.himwei.testtemplatebackend.model.vo.DoctorVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author himwei
* @description 针对表【t_doctor(医生信息表)】的数据库操作Mapper
* @createDate 2025-12-02 19:05:38
* @Entity com.himwei.testtemplatebackend.model.entity.TDoctor
*/
public interface TDoctorMapper extends BaseMapper<TDoctor> {
    // 自定义查询：获取医生列表 VO
    IPage<DoctorVO> selectDoctorVOPage(IPage<DoctorVO> page, @Param("query") DoctorQueryDTO queryDTO);
}




