package top.jinjinz.spring.aop.adapter;

import top.jinjinz.spring.aop.aspectj.AbstractAspectJAdvice;
import top.jinjinz.spring.aop.aspectj.AspectJAdvice;
import top.jinjinz.spring.aop.intercept.Joinpoint;
import top.jinjinz.spring.aop.intercept.MethodInterceptor;
import top.jinjinz.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 拦截器-after
 * @author jinjin
 * @date 2019-04-22
 */
public class AfterReturningAdviceInterceptor extends AbstractAspectJAdvice implements MethodInterceptor {
    private Joinpoint joinPoint;
    public AfterReturningAdviceInterceptor(AspectJAdvice aspectJAdvice) {
        super(aspectJAdvice);
    }

    private void afterReturning(Object retVal,Method method, Object[] args, Object target) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object retVal = methodInvocation.proceed();
        this.joinPoint = methodInvocation;
        this.afterReturning(retVal,methodInvocation.getMethod(),methodInvocation.getArguments(),methodInvocation.getThis());
        return retVal;
    }
}
