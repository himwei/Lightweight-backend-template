package com.himwei.testtemplatebackend.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.himwei.testtemplatebackend.annotation.Log;
import com.himwei.testtemplatebackend.model.entity.SysLog;
import com.himwei.testtemplatebackend.service.SysLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 操作日志切面处理
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private SysLogService sysLogService;

    // 用于计算耗时
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<>();

    /**
     * 处理请求前执行
     */
    @Before("@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     * @param jsonResult 控制台返回的对象
     */
    @AfterReturning(value = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 获取当前请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) return;
            HttpServletRequest request = attributes.getRequest();

            // 创建日志对象
            SysLog operLog = new SysLog();
            operLog.setStatus(1); // 1 正常

            // 记录耗时
            long startTime = TIME_THREADLOCAL.get();
            operLog.setCostTime(System.currentTimeMillis() - startTime);
            operLog.setOperTime(LocalDateTime.now());

            // 请求IP地址
            operLog.setOperIp(JakartaServletUtil.getClientIP(request));
            // 请求URL
            operLog.setOperUrl(request.getRequestURI());
            // 请求方式
            operLog.setReqMethod(request.getMethod());
            // 方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");

            // 设置操作人员
            if (StpUtil.isLogin()) {
                // 这里假设你已经把 username 存到了 Session 或者能通过 ID 查到
                // 简单起见，这里记录 ID，实际可拓展为记录 username
                operLog.setOperName(StpUtil.getLoginIdAsString());
            } else {
                operLog.setOperName("匿名用户");
            }

            // 处理注解上的参数
            if (controllerLog != null) {
                operLog.setTitle(controllerLog.title());
                operLog.setBusinessType(controllerLog.businessType());

                // 是否保存请求数据
                if (controllerLog.isSaveRequestData()) {
                    Object[] args = joinPoint.getArgs();
                    if (args != null && args.length > 0) {
                         // 简单转JSON，实际建议过滤掉 HttpServletRequest 等无法序列化的参数
                        try {
                            String params = JSONUtil.toJsonStr(args[0]); // 取第一个参数通常是 DTO
                            operLog.setReqParam(StrUtil.sub(params, 0, 2000));
                        } catch (Exception ex) {
                            // 忽略序列化失败
                        }
                    }
                }

                // 是否保存响应数据
                if (controllerLog.isSaveResponseData() && jsonResult != null) {
                    operLog.setJsonResult(StrUtil.sub(JSONUtil.toJsonStr(jsonResult), 0, 2000));
                }
            }

            // 如果有异常
            if (e != null) {
                operLog.setStatus(0); // 异常状态
                operLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
            }

            // 异步保存数据库（实际项目中建议使用 @Async 或消息队列，避免阻塞主线程）
            sysLogService.save(operLog);

        } catch (Exception exp) {
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            TIME_THREADLOCAL.remove(); // 防止内存泄漏
        }
    }
}
