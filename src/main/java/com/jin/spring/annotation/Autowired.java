package com.jin.spring.annotation;

import java.lang.annotation.*;

/**
 * 自动装配注解
 * @author jinjin
 * @date 2019-03-25
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
