package top.jinjinz.spring.aop;

import java.lang.reflect.Proxy;

/**
 * 用于AOP代理的工厂，用于编程使用，而不是通过声明性
 * @author jinjin
 * @date 2019-04-18
 */
public class ProxyFactory extends AdvisedSupport{


    public Object getProxy() throws Exception {
        return createAopProxy().getProxy();
    }

    public Object getProxy(ClassLoader classLoader)  throws Exception{
        return createAopProxy().getProxy(classLoader);
    }

    private synchronized AopProxy createAopProxy() throws Exception{
            Class<?> targetClass = getTargetClass();
            if (targetClass == null) {
                throw new Exception("没有代理目标类");
            }
            if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                return new JdkDynamicAopProxy(this);
            }
            //todo 没有继承接口的类需要cglib代理
            //return new ObjenesisCglibAopProxy(config);
            return new JdkDynamicAopProxy(this);
    }


}
