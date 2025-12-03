package com.himwei.testtemplatebackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.common.PageDTO;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.mapper.TScheduleMapper;
import com.himwei.testtemplatebackend.model.dto.DiagnosisRequest;
import com.himwei.testtemplatebackend.model.dto.RegSubmitRequest;
import com.himwei.testtemplatebackend.model.entity.TRegistration;
import com.himwei.testtemplatebackend.model.entity.TSchedule;
import com.himwei.testtemplatebackend.model.enums.RegStatusEnum;
import com.himwei.testtemplatebackend.model.enums.ScheduleStatusEnum;
import com.himwei.testtemplatebackend.model.vo.RegistrationVO;
import com.himwei.testtemplatebackend.service.TRegistrationService;
import com.himwei.testtemplatebackend.mapper.TRegistrationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author himwei
* @description 针对表【t_registration(挂号记录表)】的数据库操作Service实现
* @createDate 2025-12-02 19:05:38
*/
@Service
public class TRegistrationServiceImpl extends ServiceImpl<TRegistrationMapper, TRegistration>
    implements TRegistrationService{

    @Resource
    private TScheduleMapper scheduleMapper;

    @Override
    public boolean diagnosis(DiagnosisRequest request) {
        // 1. 查挂号单
        TRegistration reg = this.getById(request.getRegId());
        if (reg == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "挂号单不存在");
        }

        // 2. (可选) 校验：只能修改自己的病人
        // 这一步比较繁琐，要查排班->查医生->对比当前登录ID，如果不想太复杂可以先跳过

        // 3. 更新数据
        reg.setDiagnosis(request.getDiagnosis());
        reg.setStatus(RegStatusEnum.FINISHED.getValue()); // 状态改为：已完成

        return this.updateById(reg);
    }

    @Transactional(rollbackFor = Exception.class) // 开启事务，报错自动回滚
    @Override
    public Long submitRegistration(RegSubmitRequest request) {
        // 1. 获取当前登录患者ID
        Long patientId = StpUtil.getLoginIdAsLong();

        // 2. 校验排班是否存在
        TSchedule schedule = scheduleMapper.selectById(request.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "排班不存在");
        }

        // 3. 校验排班状态 (使用枚举)
        if (schedule.getStatus().equals(ScheduleStatusEnum.STOP.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该排班已停诊");
        }

        // 4. 校验是否重复挂号 (同一排班，同一个人，且状态不是已取消)
        Long count = this.lambdaQuery()
                .eq(TRegistration::getScheduleId, request.getScheduleId())
                .eq(TRegistration::getPatientUserId, patientId)
                .ne(TRegistration::getStatus, RegStatusEnum.CANCELED.getValue()) // 不等于已取消
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已预约该时段，请勿重复挂号");
        }

        // 5. 核心并发控制：扣减库存 (乐观锁)
        // 执行 SQL: update ... set booked_num = booked_num + 1 ...
        int updateRows = scheduleMapper.increaseBookedNum(request.getScheduleId());
        if (updateRows <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "号源已满，手慢了！");
        }

        // 6. 生成挂号单
        TRegistration reg = new TRegistration();
        reg.setScheduleId(request.getScheduleId());
        reg.setPatientUserId(patientId);
        reg.setStatus(RegStatusEnum.BOOKED.getValue()); // 状态：已预约
        // createTime 由 MyBatis-Plus 自动填充，如果没有配置自动填充，这里要手动 set
        // reg.setCreateTime(new Date());

        this.save(reg);

        return reg.getId();
    }

    @Override
    public IPage<RegistrationVO> pageMyRegistrations(PageDTO pageDTO) {
        Long patientId = StpUtil.getLoginIdAsLong();
        Page<RegistrationVO> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        // 调用之前写好的 XML 联表查询
        // doctorId 传 null，只查 patientId
        return this.baseMapper.selectRegistrationVOList(page, patientId, null);
    }
}




