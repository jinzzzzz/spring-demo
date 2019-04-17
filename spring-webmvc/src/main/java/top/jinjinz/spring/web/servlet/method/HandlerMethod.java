package top.jinjinz.spring.web.servlet.method;

import java.lang.reflect.Method;

/**
 * 封装Handler的信息
 * @author jinjin
 * @date 2019-04-17
 */
public class HandlerMethod {

    private final Object bean;

    private final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
