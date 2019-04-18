package top.jinjinz.spring.aop.intercept;

import java.lang.reflect.Method;

/**
 * 方法拦截
 * @author jinjin
 * @date 2019-04-18
 */
public interface MethodInvocation extends Joinpoint{

    Method getMethod();

    Object[] getArguments();
}
