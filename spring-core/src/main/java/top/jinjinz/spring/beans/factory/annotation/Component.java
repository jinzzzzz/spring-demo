package top.jinjinz.spring.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * 组件标识
 * @author jinjin
 * @date 2019-04-18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
