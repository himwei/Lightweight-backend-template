package com.himwei.testtemplatebackend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.model.entity.TRegistration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.himwei.testtemplatebackend.model.vo.RegistrationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author himwei
* @description 针对表【t_registration(挂号记录表)】的数据库操作Mapper
* @createDate 2025-12-02 19:05:38
* @Entity com.himwei.testtemplatebackend.model.entity.TRegistration
*/
public interface TRegistrationMapper extends BaseMapper<TRegistration> {
    // 自定义查询：获取挂号列表 VO (通用：既可以查患者的挂号，也可以查医生的病人)
    // 记得把 List 改成 IPage，并加上第一个参数 page
    IPage<RegistrationVO> selectRegistrationVOList(IPage<RegistrationVO> page,
                                                   @Param("patientId") Long patientId,
                                                   @Param("doctorId") Long doctorId);
}




