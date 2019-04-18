package top.jinjinz.spring.test.aspect;

import top.jinjinz.spring.aop.annotation.After;
import top.jinjinz.spring.aop.annotation.AfterThrowing;
import top.jinjinz.spring.aop.annotation.Aspect;
import top.jinjinz.spring.aop.annotation.Before;
import top.jinjinz.spring.aop.intercept.Joinpoint;
import top.jinjinz.spring.beans.factory.annotation.Component;

import java.util.Arrays;

/**
 * 日志切面
 * @author jinjin
 * @date 2019-04-18
 */
@Aspect
@Component
public class LogAspect {

    //使用正则
    @Before("public .* top.jinjinz.spring.test.service..*Service..*(.*)")
    //在调用一个方法之前，执行before方法
    public void before(Joinpoint joinPoint){
        //joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(),System.currentTimeMillis());
        System.out.println("调用方法前" +
                "\nTargetObject:" +  joinPoint.getThis());
    }

    @After("public .* top.jinjinz.spring.test.service..*Service..*(.*)")
    //在调用一个方法之后，执行after方法
    public void after(Joinpoint joinPoint){
        System.out.println("调用方法后" +
                "\nTargetObject:" +  joinPoint.getThis());
    }

    @AfterThrowing(pointcut="public .* top.jinjinz.spring.test.service..*Service..*(.*)",throwing="ex")
    public void afterThrowing(Joinpoint joinPoint, Throwable ex){
        System.out.println("出现异常" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nThrows:" + ex.getMessage());
    }
}
