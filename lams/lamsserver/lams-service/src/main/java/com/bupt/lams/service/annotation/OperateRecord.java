package com.bupt.lams.service.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateRecord {
    /**
     * 记录操作描述
     */
    String description() default "";

    /**
     * 策略类的类型
     */
    Class clazz();
}