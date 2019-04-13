package top.jinjinz.spring.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * 请求地址映射注解
 * @author jinjin
 * @date 2019-03-25
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
