package com.jin.spring.annotation;

import java.lang.annotation.*;

/**
 * 控制器注解
 * @author jinjin
 * @date 2019-03-25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
