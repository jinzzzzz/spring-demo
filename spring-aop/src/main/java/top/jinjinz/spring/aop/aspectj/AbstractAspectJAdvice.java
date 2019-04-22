package top.jinjinz.spring.aop.aspectj;

import top.jinjinz.spring.aop.Advice;
import top.jinjinz.spring.aop.annotation.AopProxyUtils;
import top.jinjinz.spring.aop.intercept.Joinpoint;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基类  {@link top.jinjinz.spring.aop.Advice}
 * @author jinjin
 * @date 2019-04-22
 */
public abstract class AbstractAspectJAdvice implements Advice {

    private AspectJAdvice aspectJAdvice;

    public AbstractAspectJAdvice(AspectJAdvice aspectJAdvice) {
        this.aspectJAdvice = aspectJAdvice;
    }

    protected Object invokeAdviceMethod(Joinpoint joinPoint, Object returnValue, Throwable tx) throws Throwable{
        Class<?> [] paramTypes = this.aspectJAdvice.getAspectMethod().getParameterTypes();
        if(paramTypes.length == 0){
            return this.aspectJAdvice.getAspectMethod().invoke(aspectJAdvice.getAspectTarget());
        }else{
            Object [] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i ++) {
                if(paramTypes[i] == Throwable.class){
                    args[i] = tx;
                }else if(paramTypes[i] == Object.class){
                    args[i] = returnValue;
                }else if(paramTypes[i] == Joinpoint.class){
                    args[i] = joinPoint;
                }
            }
            return this.aspectJAdvice.getAspectMethod().invoke(
                    AopProxyUtils.getTargetObject(aspectJAdvice.getAspectTarget()),args);
        }
    }
}
