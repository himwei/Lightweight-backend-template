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
