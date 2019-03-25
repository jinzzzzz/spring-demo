package com.jin.spring.annotation;

import java.lang.annotation.*;

/**
 * 请求参数注解
 * @author jinjin
 * @date 2019-03-25
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
