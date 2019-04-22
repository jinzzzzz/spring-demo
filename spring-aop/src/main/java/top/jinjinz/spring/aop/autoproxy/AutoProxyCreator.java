package top.jinjinz.spring.aop.autoproxy;

import top.jinjinz.spring.aop.AdvisedSupport;
import top.jinjinz.spring.aop.ProxyFactory;
import top.jinjinz.spring.aop.adapter.AfterReturningAdviceInterceptor;
import top.jinjinz.spring.aop.adapter.MethodBeforeAdviceInterceptor;
import top.jinjinz.spring.aop.annotation.After;
import top.jinjinz.spring.aop.annotation.AfterThrowing;
import top.jinjinz.spring.aop.annotation.AopProxyUtils;
import top.jinjinz.spring.aop.annotation.Before;
import top.jinjinz.spring.aop.aspectj.AspectJAdvice;
import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自动创建代理
 * @author jinjin
 * @date 2019-04-18
 */
public class AutoProxyCreator implements BeanPostProcessor {

    private final Map<String,List<AspectJAdvice>> aspectMethods;

    private final List<String> patterns;

    public AutoProxyCreator(Map<String, List<AspectJAdvice>> aspectMethods, List<String> patterns) {
        this.aspectMethods = aspectMethods;
        this.patterns = patterns;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName, BeanFactory beanFactory)
            throws Exception {
        if (bean != null) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.setTargetClass(bean.getClass());
            proxyFactory.setMethodCache(getMethodCathe(proxyFactory,beanFactory,Before.class));
            return proxyFactory.getProxy();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName, BeanFactory beanFactory)
            throws Exception {
        if (bean != null) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.setTargetClass(bean.getClass());
            proxyFactory.setMethodCache(getMethodCathe(proxyFactory,beanFactory,After.class));
            return proxyFactory.getProxy();
        }
        return bean;
    }

    private List<Object> getAdvices(
            String p,BeanFactory beanFactory,Class<? extends Annotation> annotationClass) throws Exception{
        List<Object> advices = new ArrayList<>();
        List<AspectJAdvice> aspectJAdviceList = aspectMethods.get(p);
        for (AspectJAdvice aspectJAdvice:aspectJAdviceList) {
            //注入
            if(aspectJAdvice.getAspectTarget() instanceof String){
                aspectJAdvice.setAspectTarget(beanFactory.getBean((String)aspectJAdvice.getAspectTarget()));
            }
            /*if(aspectJAdvice.getAspectMethod().isAnnotationPresent(annotationClass)){
                if(annotationClass == Before.class) {
                    advices.add(new MethodBeforeAdviceInterceptor(aspectJAdvice));
                }else if(annotationClass == After.class){
                    advices.add(new AfterReturningAdviceInterceptor(aspectJAdvice));
                }
            }*/
            if(aspectJAdvice.getAspectMethod().isAnnotationPresent(Before.class)){
                advices.add(new MethodBeforeAdviceInterceptor(aspectJAdvice));
            }else if(aspectJAdvice.getAspectMethod().isAnnotationPresent(After.class)){
                advices.add(new AfterReturningAdviceInterceptor(aspectJAdvice));
             }
        }
        return advices;
    }

    private Map<Method, List<Object>> getMethodCathe(
            AdvisedSupport advisedSupport,BeanFactory beanFactory,Class<? extends Annotation> annotationClass)
            throws Exception{
        Map<Method, List<Object>> methodCache = new HashMap<>();
        Method[] methods=advisedSupport.getTargetClass().getMethods();
        List<Object> advices;
        Matcher matcher;
        String methodString;
        Pattern pattern;
        for (Method method:methods) {
            methodString = method.toString();
            if(methodString.contains("throws")){
                methodString = methodString.substring(0,methodString.lastIndexOf("throws")).trim();
            }
            //判断所有的Aspect中的方法上的正则查看是否匹配
            for (String p:patterns){
                pattern = Pattern.compile(p);
                matcher = pattern.matcher(methodString);
                if(matcher.matches()){
                    advices = getAdvices(p,beanFactory,annotationClass);
                    methodCache.put(method,advices);
                    break;
                }
            }
        }
        return methodCache;
    }

}
