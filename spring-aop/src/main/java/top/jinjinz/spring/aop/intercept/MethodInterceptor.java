package top.jinjinz.spring.aop.intercept;

/**
 * 方法调用过程中调用
 * @author jinjin
 * @date 2019-04-18
 */
public interface MethodInterceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
