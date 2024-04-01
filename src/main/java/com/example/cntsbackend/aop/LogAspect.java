package com.example.cntsbackend.aop;

import com.alibaba.fastjson.JSON;
import com.example.cntsbackend.annotation.LOG;
import com.example.cntsbackend.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 正常日志切入点
     */
    @Pointcut("@annotation(com.example.cntsbackend.annotation.LOG)")
    public void LogPointCut() {
    }

    /**
     * 异常日志切入点
     */
    @Pointcut("execution(* com.example.cntsbackend.controller..*.*(..))")
    public void ExceptionLogPointCut() {
    }


    /**
     * 后置通知
     *
     * @param joinPoint 切入点
     * @param result    返回结果
     */
    @AfterReturning(value = "LogPointCut()", returning = "result")
    public void saveCommonLog(JoinPoint joinPoint, Object result) {
        StringBuilder logStr = getInfo(joinPoint);

//            获取请求结果
        if (result instanceof CommonResponse) {
            int code = ((CommonResponse<?>) result).getCode();
            if (code == 0) {
                logStr.append("请求状态：").append("成功(code0)").append("，");
            } else {
                logStr.append("请求状态：").append("失败(code").append(code).append(")").append("，");
            }
        } else if (result instanceof String) {
            if ("success".equals(result) || "SUCCESS".equals(result)) {
                logStr.append("请求状态：").append("成功(SUCCESS)").append("，");
            } else {
                logStr.append("请求状态：").append("失败(FAILURE)").append("，");
            }
        } else {
            logStr.append("请求状态：").append("未知(未能成功解析)").append("，");
        }
        log.info(logStr.toString());
    }

    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "ExceptionLogPointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        StringBuilder logStr = getInfo(joinPoint);
        logStr.append("异常信息：").append(e.getMessage()).append("，");
        log.error(logStr.toString());
    }

    public StringBuilder getInfo(JoinPoint joinPoint) {
        StringBuilder logStr = new StringBuilder();
        // 获取requestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 获取request
        HttpServletRequest request = requestAttributes == null ? null : (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取方法
            Method method = signature.getMethod();
            // 获取类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取方法名
            String methodName = signature.getName();
            methodName = className + "." + methodName + "()";
            // 获取注解
            LOG logAnnotation = method.getAnnotation(LOG.class);

            if (logAnnotation != null) {
                logStr.append("模块名称：").append(logAnnotation.moduleName()).append("，");
                logStr.append("模块版本：").append(logAnnotation.moduleVersion()).append("，");
            }
            logStr.append("请求时间：").append(getCurrentTime()).append("，");
            logStr.append("请求方法：").append(methodName).append("，");

            // 将参数转为json
            String params = paramsToString(joinPoint.getArgs());
            logStr.append("请求参数：").append(params).append("，");
            if (request != null) {
                logStr.append("请求方式：").append(request.getMethod()).append("，");
            } else {
                logStr.append("请求方式：").append("未知").append("，");
            }
            if (request != null) {
                logStr.append("请求IP：").append(getIp(request)).append("，");
            } else {
                logStr.append("请求IP：").append("未知").append("，");
            }
            logStr.append("请求URI：").append(request == null ? "未知" : request.getRequestURI()).append("，");
        } catch (Exception e) {
            log.error("日志记录异常", e);
        }

        return logStr;
    }


    /**
     * 将参数转为json
     *
     * @param args 参数
     * @return json字符串
     */
    private String paramsToString(Object[] args) {
        StringBuilder params = new StringBuilder();
        params.append("(");
        if (args != null) {
            for (Object o : args) {
                if (o != null) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params.append(jsonObj.toString()).append(",");
                    } catch (Exception e) {
                        log.error("转换参数异常", e);
                    }
                }
            }
        }
        params.append(")");
        return params.toString().trim();

    }


    /**
     * 根据HttpServletRequest获取访问者的IP地址
     *
     * @param request 请求
     * @return ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getCurrentTime() {
        Date currentTime = new Date();
        return dateFormatThreadLocal.get().format(currentTime);
    }


}
