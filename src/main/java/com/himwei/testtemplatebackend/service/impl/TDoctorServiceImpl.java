package com.himwei.testtemplatebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import com.himwei.testtemplatebackend.model.dto.DoctorAddDTO;
import com.himwei.testtemplatebackend.model.dto.DoctorQueryDTO;
import com.himwei.testtemplatebackend.model.dto.DoctorUpdateDTO;
import com.himwei.testtemplatebackend.model.entity.SysUser;
import com.himwei.testtemplatebackend.model.entity.TDepartment;
import com.himwei.testtemplatebackend.model.entity.TDoctor;
import com.himwei.testtemplatebackend.model.enums.RoleEnum;
import com.himwei.testtemplatebackend.model.vo.DoctorVO;
import com.himwei.testtemplatebackend.service.SysRoleService;
import com.himwei.testtemplatebackend.service.SysUserService;
import com.himwei.testtemplatebackend.service.TDepartmentService;
import com.himwei.testtemplatebackend.service.TDoctorService;
import com.himwei.testtemplatebackend.mapper.TDoctorMapper;
import com.himwei.testtemplatebackend.utils.PasswordUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Resource
    private SysRoleService sysRoleService; // 操作角色

    @Resource
    private PasswordUtils passwordUtils; // 密码加密工具

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

    @Override
    @Transactional(rollbackFor = Exception.class) // ✅ 开启事务：任何一步失败都回滚
    public Long addDoctor(DoctorAddDTO request) {
        // 1. 校验用户名是否存在 (逻辑同 userRegister)
        long count = userService.lambdaQuery()
                .eq(SysUser::getUserName, request.getUserName())
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // 2. 创建 SysUser (账号)
        SysUser user = new SysUser();
        user.setUserName(request.getUserName());
        user.setNickName(request.getNickName());
        // ✅ 使用加密工具加密密码
        user.setPassWord(passwordUtils.encrypt(request.getPassWord()));
        user.setStatus(1); // 默认正常

        userService.save(user);

        // 3. 分配角色 (假设 RoleEnum 里有 DOCTOR)
        // 如果没有 RoleEnum.DOCTOR，你需要去 Enum 里加一个，或者直接写死 ID
        sysRoleService.assignRoles(user.getId(), List.of(RoleEnum.DOCTOR.getId()));

        // 4. 创建 TDoctor (档案)
        TDoctor doctor = new TDoctor();
        doctor.setUserId(user.getId()); // 关联刚才生成的 UserID
        doctor.setDeptId(request.getDeptId());
        doctor.setTitle(request.getTitle());
        doctor.setPrice(request.getPrice());
        doctor.setIntro(request.getIntro());
        doctor.setDeptId(request.getDeptId());

        this.save(doctor);

        return doctor.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDoctorInfo(DoctorUpdateDTO request) {
        // 1. 查旧数据
        TDoctor oldDoctor = this.getById(request.getId());
        if (oldDoctor == null) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);

        // 2. 更新 SysUser 的昵称 (如果改了名字)
        if (request.getNickname() != null) {
            SysUser userUpdate = new SysUser();
            userUpdate.setId(oldDoctor.getUserId());
            userUpdate.setNickName(request.getNickname());
            userService.updateById(userUpdate);
        }

        // 3. 更新 TDoctor 档案
        TDoctor doctorUpdate = new TDoctor();
        doctorUpdate.setId(request.getId());
        doctorUpdate.setDeptId(request.getDeptId());
        doctorUpdate.setTitle(request.getTitle());
        doctorUpdate.setPrice(request.getPrice());
        doctorUpdate.setIntro(request.getIntro());
        doctorUpdate.setDeptId(request.getDeptId());

        return this.updateById(doctorUpdate);
    }
}




