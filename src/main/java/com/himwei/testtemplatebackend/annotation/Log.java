package com.himwei.testtemplatebackend.annotation;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 */
@Target(ElementType.METHOD) // 只能用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
@Documented
public @interface Log {

    /** 模块名称 */
    String title() default "";

    /** 业务类型 (如：新增、修改、删除、导出等) */
    String businessType() default "";

    /** 是否保存请求参数 */
    boolean isSaveRequestData() default true;

    /** 是否保存响应数据 */
    boolean isSaveResponseData() default true;
}
