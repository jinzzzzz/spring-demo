package top.jinjinz.spring.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * Service注解
 * @author jinjin
 * @date 2019-03-25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
