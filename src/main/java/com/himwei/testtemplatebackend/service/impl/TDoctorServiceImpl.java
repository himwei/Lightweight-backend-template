package com.himwei.testtemplatebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.model.dto.DoctorQueryDTO;
import com.himwei.testtemplatebackend.model.entity.SysUser;
import com.himwei.testtemplatebackend.model.entity.TDepartment;
import com.himwei.testtemplatebackend.model.entity.TDoctor;
import com.himwei.testtemplatebackend.model.vo.DoctorVO;
import com.himwei.testtemplatebackend.service.SysUserService;
import com.himwei.testtemplatebackend.service.TDepartmentService;
import com.himwei.testtemplatebackend.service.TDoctorService;
import com.himwei.testtemplatebackend.mapper.TDoctorMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author himwei
* @description 针对表【t_doctor(医生信息表)】的数据库操作Service实现
* @createDate 2025-12-02 19:05:38
*/
@Service
public class TDoctorServiceImpl extends ServiceImpl<TDoctorMapper, TDoctor>
    implements TDoctorService{

    @Resource
    private TDepartmentService departmentService;

    @Resource
    private SysUserService userService;

    @Override
    public IPage<DoctorVO> pageDoctors(DoctorQueryDTO queryDTO) {
        // 1. 构建分页对象
        // 这里的泛型一定要是 DoctorVO，因为我们要查出来的是 VO
        Page<DoctorVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 2. 调用 Mapper 的自定义方法 (对应 XML 里的 SQL)
        // 注意：MyBatis-Plus 会自动处理 SQL 分页，不用手写 limit
        return this.baseMapper.selectDoctorVOPage(page, queryDTO);
    }

    @Override
    public DoctorVO getDoctorProfile(Long userId) {
        // 1. 查 t_doctor 表
        TDoctor doctor = this.lambdaQuery().eq(TDoctor::getUserId, userId).one();
        if (doctor == null) {
            return null; // 或者抛个异常：您不是医生
        }

        // 2. 查 sys_user 表 (你需要注入 UserService)
        SysUser user = userService.getById(userId);

        // 3. 查 t_department 表 (你需要注入 DepartmentService)
        TDepartment dept = departmentService.getById(doctor.getDeptId());

        // 4. 组装 VO
        DoctorVO vo = new DoctorVO();
        BeanUtil.copyProperties(doctor, vo); // id, title, price, intro
        if (user != null) {
            vo.setDoctorName(user.getNickName());
            vo.setAvatar(user.getAvatar());
        }
        if (dept != null) {
            vo.setDeptName(dept.getDeptName());
        }

        return vo;
    }
}




