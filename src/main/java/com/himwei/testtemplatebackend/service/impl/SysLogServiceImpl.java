package com.himwei.testtemplatebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.himwei.testtemplatebackend.model.entity.SysLog;
import com.himwei.testtemplatebackend.service.SysLogService;
import com.himwei.testtemplatebackend.mapper.SysLogMapper;
import org.springframework.stereotype.Service;

/**
* @author himwei
* @description 针对表【sys_log(系统操作日志表)】的数据库操作Service实现
* @createDate 2025-11-27 11:01:42
*/
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog>
    implements SysLogService{

}




