package com.ronghui.service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


/**
 * @program: PassAspect
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-17 12:44
 */
@Component
@Aspect
@Slf4j
public class PassAspect {

    @Pointcut("@annotation(com.ronghui.service.annotation.Pass)")
    public void pass() {
    }

    @Before("pass()")
    public void doBefore() {
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        String requestURI = request.getRequestURI();
//        Constant.METHOD_URL_SET.add(requestURI + ":--:" + request.getMethod());
//        System.out.println(Constant.METHOD_URL_SET);
    }
}