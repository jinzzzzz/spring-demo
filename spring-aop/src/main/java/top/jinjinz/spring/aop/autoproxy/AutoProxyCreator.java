package top.jinjinz.spring.aop.autoproxy;

import top.jinjinz.spring.aop.AdvisedSupport;
import top.jinjinz.spring.aop.ProxyFactory;
import top.jinjinz.spring.aop.annotation.After;
import top.jinjinz.spring.aop.annotation.AfterThrowing;
import top.jinjinz.spring.aop.annotation.Before;
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

    private final Map<String,List<Method>> aspectMethods;

    private final List<String> patterns;

    public AutoProxyCreator(Map<String, List<Method>> aspectMethods, List<String> patterns) {
        this.aspectMethods = aspectMethods;
        this.patterns = patterns;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        if (bean != null) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.setTargetClass(bean.getClass());
            proxyFactory.setMethodCache(getMethodCathe(proxyFactory,Before.class));
            return proxyFactory.getProxy();
        }
        return bean;
    }

    private Map<Method, List<Object>> getMethodCathe(AdvisedSupport advisedSupport,Class<? extends Annotation> annotationClass) {
        Map<Method, List<Object>> methodCache = new HashMap<>();
        Method[] methods=advisedSupport.getTargetClass().getMethods();
        List<Method> methodList;
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
                    methodList = aspectMethods.get(p);
                    for (Method annotationMethod:methodList) {
                        if(method.isAnnotationPresent(annotationClass)){

                        }
                    }
                    List<Object> advices = new ArrayList<>();
                    methodCache.put(method,advices);
                }
            }
        }
        return methodCache;
    }

}
