CREATE TABLE `sys_role` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `role_code` varchar(50) NOT NULL COMMENT '角色编码',
                            `role_name` varchar(50) NOT NULL COMMENT '角色名称',
                            `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
                            `status` int DEFAULT '1' COMMENT '状态 (1:正常 2:禁用)',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `deleted` int DEFAULT '0' COMMENT '是否删除 (0:否 1:是)',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_role_code` (`role_code`) COMMENT '角色编码唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色表';

CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `username` varchar(50) NOT NULL COMMENT '账号',
                            `password` varchar(100) NOT NULL COMMENT '密码(通常加密存储)',
                            `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
                            `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
                            `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                            `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                            `status` int DEFAULT '1' COMMENT '状态 (1:正常 2:禁用)',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `deleted` int DEFAULT '0' COMMENT '是否删除 (0:否 1:是)',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`) COMMENT '账号唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户测试表';


CREATE TABLE `sys_user_role` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `user_id` bigint NOT NULL COMMENT '用户ID',
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_user_role` (`user_id`,`role_id`) COMMENT '用户角色唯一索引',
                                 KEY `idx_user_id` (`user_id`),
                                 KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';


CREATE TABLE `sys_log` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                           `title` varchar(50) DEFAULT NULL COMMENT '模块标题',
                           `business_type` varchar(20) DEFAULT NULL COMMENT '业务类型',
                           `method` varchar(100) DEFAULT NULL COMMENT '方法名称',
                           `req_method` varchar(10) DEFAULT NULL COMMENT '请求方式',
                           `oper_name` varchar(50) DEFAULT NULL COMMENT '操作人员',
                           `oper_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
                           `oper_ip` varchar(50) DEFAULT NULL COMMENT '主机地址',
                           `req_param` json DEFAULT NULL COMMENT '请求参数',
                           `json_result` json DEFAULT NULL COMMENT '返回参数',
                           `status` int DEFAULT '1' COMMENT '操作状态（1正常 0异常）',
                           `error_msg` varchar(2000) DEFAULT NULL COMMENT '错误消息',
                           `oper_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                           `cost_time` bigint DEFAULT '0' COMMENT '消耗时间(ms)',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统操作日志表';




-- ==========================================
-- 1. 科室表 (t_department)
-- 相当于选课系统里的“学院/专业”
-- ==========================================
CREATE TABLE `t_department` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `dept_name` varchar(50) NOT NULL COMMENT '科室名称 (如: 心内科)',
                                `dept_code` varchar(50) DEFAULT NULL COMMENT '科室编码',
                                `location` varchar(100) DEFAULT NULL COMMENT '诊室位置',
                                `intro` varchar(500) DEFAULT NULL COMMENT '科室介绍',
                                `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='科室管理表';

-- ==========================================
-- 2. 医生详情表 (t_doctor)
-- 注意：医生也是用户，这里只存医生的专业信息，通过 user_id 关联 sys_user
-- ==========================================
CREATE TABLE `t_doctor` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `user_id` bigint NOT NULL COMMENT '关联 sys_user 的 id',
                            `dept_id` bigint NOT NULL COMMENT '关联 t_department 的 id',
                            `title` varchar(50) DEFAULT '主治医师' COMMENT '职称 (主任/副主任/主治)',
                            `price` decimal(10,2) DEFAULT '0.00' COMMENT '挂号费',
                            `intro` text COMMENT '医生简介',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_user_id` (`user_id`), -- 一个用户只能对应一个医生档案
                            KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB COMMENT='医生信息表';

-- ==========================================
-- 3. 排班表 (t_schedule)
-- 相当于“课程表”，定义哪个医生在哪天有号
-- ==========================================
CREATE TABLE `t_schedule` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '排班ID',
                              `doctor_id` bigint NOT NULL COMMENT '关联 t_doctor 的 id',
                              `work_date` date NOT NULL COMMENT '出诊日期 (2023-11-11)',
                              `shift_type` int DEFAULT '1' COMMENT '时段 (1:上午 2:下午)',
                              `quota` int DEFAULT '30' COMMENT '号源总数',
                              `booked_num` int DEFAULT '0' COMMENT '已挂号数量',
                              `status` int DEFAULT '1' COMMENT '状态 (1:正常 0:停诊)',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`),
                              KEY `idx_doctor_date` (`doctor_id`, `work_date`)
) ENGINE=InnoDB COMMENT='医生排班表';

-- ==========================================
-- 4. 挂号记录表 (t_registration)
-- 相当于“选课表”，多对多关系核心
-- ==========================================
CREATE TABLE `t_registration` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '挂号单ID',
                                  `schedule_id` bigint NOT NULL COMMENT '关联 t_schedule 的 id',
                                  `patient_user_id` bigint NOT NULL COMMENT '患者ID (关联 sys_user)',
                                  `status` int DEFAULT '0' COMMENT '状态 (0:已预约 1:已完成 2:已取消)',
                                  `visit_time` datetime DEFAULT NULL COMMENT '实际就诊时间',
                                  `diagnosis` varchar(1000) DEFAULT NULL COMMENT '医生诊断结果/医嘱',
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_patient` (`patient_user_id`),
                                  KEY `idx_schedule` (`schedule_id`)
) ENGINE=InnoDB COMMENT='挂号记录表';
