package top.jinjinz.spring.context.annotation;

import top.jinjinz.spring.aop.annotation.After;
import top.jinjinz.spring.aop.annotation.AfterThrowing;
import top.jinjinz.spring.aop.annotation.Aspect;
import top.jinjinz.spring.aop.annotation.Before;
import top.jinjinz.spring.aop.autoproxy.AutoProxyCreator;
import top.jinjinz.spring.beans.factory.annotation.AnnotatedBeanDefinition;
import top.jinjinz.spring.beans.factory.annotation.Component;
import top.jinjinz.spring.beans.factory.annotation.Controller;
import top.jinjinz.spring.beans.factory.annotation.Service;
import top.jinjinz.spring.beans.factory.config.BeanDefinition;
import top.jinjinz.spring.beans.factory.config.BeanPostProcessor;
import top.jinjinz.spring.beans.factory.support.BeanDefinitionRegistry;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 注解bean类的注册器
 * @author jinjin
 * @date 2019-04-15
 */
public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private Map<String,List<Method>> aspectMethods = new HashMap<>();

    private List<String> patterns = new ArrayList<>();

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?>... annotatedClasses) throws Exception{
        for (Class<?> annotatedClass : annotatedClasses) {
            registerBean(annotatedClass);
        }
        BeanPostProcessor autoProxyCreator = new AutoProxyCreator(aspectMethods,
                patterns.stream().distinct().collect(Collectors.toList()));
        registry.addBeanPostProcessor(autoProxyCreator);
    }

    public void registerBean(Class<?> annotatedClass) throws Exception{
        //IOC容器中只注册加了指定注解的类
        if(isRegister(annotatedClass)){
            doRegisterBean(annotatedClass);
            parseBean(annotatedClass);
        }
    }

    //解析bean的其他注解信息并注册其功能
    private void parseBean(Class<?> annotatedClass) {
        //注册aop注解类
        if(annotatedClass.isAnnotationPresent(Aspect.class)){
            parseAspect(annotatedClass);
        }
    }

    //注册Aspect信息
    private void parseAspect(Class<?> annotatedClass) {
        List<Method> methodList;
        Method[] methods=annotatedClass.getMethods();
        String pointCut = "";
        for (Method method:methods) {
            if(method.isAnnotationPresent(Before.class)){
                pointCut = method.getAnnotation(Before.class).value();
            }
            if(method.isAnnotationPresent(After.class)){
                pointCut = method.getAnnotation(After.class).value();
            }
            if(method.isAnnotationPresent(AfterThrowing.class)){
                pointCut = method.getAnnotation(AfterThrowing.class).pointcut();
            }
            if("".equals(pointCut)){
                continue;
            }
            if(!patterns.contains(pointCut)){
                patterns.add(pointCut);
            }
            methodList=aspectMethods.get(pointCut);
            if(null == methodList){
                methodList = new ArrayList<>();
            }
            methodList.add(method);
            aspectMethods.put(pointCut,methodList);
        }
    }

    private boolean isRegister(Class<?> annotatedClass){
        return annotatedClass.isAnnotationPresent(Component.class)||
               annotatedClass.isAnnotationPresent(Controller.class)||annotatedClass.isAnnotationPresent(Service.class);
    }

    //注册bean信息
    private <T> void doRegisterBean(Class<T> annotatedClass) throws Exception{
        registry.registerBeanDefinition(annotatedClass.getName(),doCreateBeanDefinition(
                toLowerFirstCase(annotatedClass.getSimpleName()),annotatedClass.getName()));
        Class<?> [] interfaces = annotatedClass.getInterfaces();
        for (Class<?> i : interfaces) {
            registry.registerBeanDefinition(i.getName(),doCreateBeanDefinition(i.getName(),annotatedClass.getName()));
        }
    }

    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName){
        BeanDefinition beanDefinition = new AnnotatedBeanDefinition();

        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
