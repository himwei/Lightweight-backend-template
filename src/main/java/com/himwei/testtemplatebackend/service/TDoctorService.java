package com.himwei.testtemplatebackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.model.dto.DoctorQueryDTO;
import com.himwei.testtemplatebackend.model.entity.TDoctor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.himwei.testtemplatebackend.model.vo.DoctorVO;

/**
* @author himwei
* @description 针对表【t_doctor(医生信息表)】的数据库操作Service
* @createDate 2025-12-02 19:05:38
*/
public interface TDoctorService extends IService<TDoctor> {
    /**
     * 分页查询医生列表 (包含姓名、科室等信息)
     */
    IPage<DoctorVO> pageDoctors(DoctorQueryDTO queryDTO);


    // 获取当前登录医生的详细信息
    DoctorVO getDoctorProfile(Long userId);
}
