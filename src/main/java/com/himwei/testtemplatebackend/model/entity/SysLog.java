package com.himwei.testtemplatebackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 系统操作日志表
 * @TableName sys_log
 */
@TableName(value ="sys_log")
@Data
public class SysLog implements Serializable {
    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模块标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 方法名称
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求方式
     */
    @TableField(value = "req_method")
    private String reqMethod;

    /**
     * 操作人员
     */
    @TableField(value = "oper_name")
    private String operName;

    /**
     * 请求URL
     */
    @TableField(value = "oper_url")
    private String operUrl;

    /**
     * 主机地址
     */
    @TableField(value = "oper_ip")
    private String operIp;

    /**
     * 请求参数
     */
    @TableField(value = "req_param")
    private Object reqParam;

    /**
     * 返回参数
     */
    @TableField(value = "json_result")
    private Object jsonResult;

    /**
     * 操作状态（1正常 0异常）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 错误消息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableField(value = "oper_time")
    private LocalDateTime operTime;

    /**
     * 消耗时间(ms)
     */
    @TableField(value = "cost_time")
    private Long costTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
