package top.jinjinz.spring.aop;

import top.jinjinz.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * JDK动态代理实现{@link AopProxy}
 * @author jinjin
 * @date 2019-04-18
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport config) throws Exception {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.advised.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(
                method,this.advised.getTargetClass());
        //如果没有可以应用到此方法的通知(Interceptor)，直接反射调用
        if(chain.isEmpty()){
            return method.invoke(proxy, args);
        }
        MethodInvocation invocation = new ReflectiveMethodInvocation(
                proxy,advised.getTarget(),method,args,this.advised.getTargetClass(),chain);
        return invocation.proceed();
    }
}
