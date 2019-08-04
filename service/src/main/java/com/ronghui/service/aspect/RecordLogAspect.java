package com.ronghui.service.aspect;

import com.alibaba.fastjson.JSONObject;
import com.ronghui.service.annotation.Log;
import com.ronghui.service.common.constant.EntityConstant;
import com.ronghui.service.common.util.*;
import com.ronghui.service.context.SpringContextBeanService;
import com.ronghui.service.entity.OperationLog;
import com.ronghui.service.entity.User;
import com.ronghui.service.jpa.dao.OperationLogDao;
import com.ronghui.service.service.IOperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.request.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RecordLogAspect extends AbstractAspectManager {
    @Autowired
    private OperationLogDao operationLogDao;

    public RecordLogAspect(AspectApi aspectApi) {
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        super.doHandlerAspect(pjp, method);
        return execute(pjp, method);
    }

    private Logger logger = LoggerFactory.getLogger(RecordLogAspect.class);

    @Override
    @Async
    protected Object execute(ProceedingJoinPoint pjp, Method method) throws Throwable {
        Log log = method.getAnnotation(Log.class);
        // 异常日志信息
        String actionLog = null;
        StackTraceElement[] stackTrace = null;
        // 是否执行异常
        boolean isException = false;
        // 接收时间戳
        long endTime;
        // 开始时间戳
        long operationTime = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (Throwable throwable) {
            isException = true;
            actionLog = throwable.getMessage();
            stackTrace = throwable.getStackTrace();
            throw throwable;
        } finally {
            // 日志处理
            logHandle(pjp, method, log, actionLog, operationTime, isException, stackTrace);
        }
    }

    private void logHandle(ProceedingJoinPoint joinPoint,
                           Method method,
                           Log log,
                           String actionLog,
                           long startTime,
                           boolean isException,
                           StackTraceElement[] stackTrace) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        IOperationLogService operationLogService = SpringContextBeanService.getBean(IOperationLogService.class);
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        OperationLog operationLog = new OperationLog();
        User user = null;
        try {
            user = SecureUtil.getDataBaseUser();
        } catch (Exception e) {
        }
        operationLog.setUserName(user != null ? user.getNickName() : "游客");
        operationLog.setIp(getIpAddress(request));
        operationLog.setClassName(joinPoint.getTarget().getClass().getName());
        operationLog.setCreateTime(startTime);
        operationLog.setLogDescription(log.value());
        operationLog.setModelName(log.modelName());
        operationLog.setAction(log.action());
        if (isException) {
            StringBuilder sb = new StringBuilder();
            sb.append(actionLog + " &#10; ");
            for (int i = 0; i < stackTrace.length; i++) {
                sb.append(stackTrace[i] + " &#10; ");
            }
            operationLog.setMessage(sb.toString());
        }
        operationLog.setMethodName(method.getName());
        operationLog.setSucceed(isException ? EntityConstant.BADREQUEST : EntityConstant.OK);
        operationLog.setActionArgs(operationLogService.getMethodArgs(joinPoint.getArgs()));
        logger.info("执行方法信息:" + JSONObject.toJSON(operationLog));
        operationLogService.deleteLogs();
        operationLogService.save(operationLog);
    }


    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip + ":" + request.getRemotePort();
    }
}
