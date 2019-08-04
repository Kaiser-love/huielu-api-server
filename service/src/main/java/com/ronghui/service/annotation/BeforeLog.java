package com.ronghui.service.annotation;


import java.lang.annotation.*;

/**
 * 在Controller方法上加入改注解会自动记录日志
 *
 * @author : liugh
 * @date : 2018/05/08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeforeLog {
    /**
     * 描述.
     */
    String value() default "";
}
