package com.himwei.testtemplatebackend.exception;

/**
 * 抛异常工具类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常 基础版
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException  exception){
        if ( condition){
            throw exception;
        }
    }
    /**
     * 条件成立则抛异常 高级版配合自定义异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
//        if ( condition){
//            throw new BusinessException(errorCode);
//        }
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常 高级版配合自定义异常和异常信息
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {

        throwIf(condition, new BusinessException(errorCode,  message));

    }
}
