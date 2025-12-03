package com.himwei.testtemplatebackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.himwei.testtemplatebackend.common.PageDTO;
import com.himwei.testtemplatebackend.model.dto.DiagnosisRequest;
import com.himwei.testtemplatebackend.model.dto.RegSubmitRequest;
import com.himwei.testtemplatebackend.model.entity.TRegistration;
import com.baomidou.mybatisplus.extension.service.IService;
import com.himwei.testtemplatebackend.model.vo.RegistrationVO;

/**
* @author himwei
* @description 针对表【t_registration(挂号记录表)】的数据库操作Service
* @createDate 2025-12-02 19:05:38
*/
public interface TRegistrationService extends IService<TRegistration> {

    // 医生接诊/录入医嘱
    boolean diagnosis(DiagnosisRequest request);

    /**
     * 提交挂号 (核心业务)
     * @param request 挂号请求
     * @return 挂号单ID
     */
    Long submitRegistration(RegSubmitRequest request);

    /**
     * 分页查询我的挂号记录 (患者端)
     */
    IPage<RegistrationVO> pageMyRegistrations(PageDTO pageDTO);
}
