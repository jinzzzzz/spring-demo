package top.jinjinz.spring.aop;

/**
 * AOP代理的接口
 * @author jinjin
 * @date 2019-04-18
 */
public interface AopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
