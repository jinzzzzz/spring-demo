package top.jinjinz.spring.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 异常通知
 * @author jinjin
 * @date 2019-04-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterThrowing {

    String value() default "";

    String pointcut() default "";

    String throwing() default "";

    String argNames() default "";

}
