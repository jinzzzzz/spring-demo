package top.jinjinz.spring.context.annotation;

import top.jinjinz.spring.beans.factory.annotation.AnnotatedBeanDefinition;
import top.jinjinz.spring.beans.factory.annotation.Component;
import top.jinjinz.spring.beans.factory.annotation.Controller;
import top.jinjinz.spring.beans.factory.annotation.Service;
import top.jinjinz.spring.beans.factory.config.BeanDefinition;
import top.jinjinz.spring.beans.factory.support.BeanDefinitionRegistry;

/**
 * 注解bean类的注册器
 * @author jinjin
 * @date 2019-04-15
 */
public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?>... annotatedClasses) throws Exception{
        for (Class<?> annotatedClass : annotatedClasses) {
            registerBean(annotatedClass);
        }
    }

    public void registerBean(Class<?> annotatedClass) throws Exception{
        //IOC容器中只注册加了指定注解的类
        if(isRegister(annotatedClass)){
            doRegisterBean(annotatedClass);
        }
    }

    private boolean isRegister(Class<?> annotatedClass){
        return annotatedClass.isAnnotationPresent(Component.class)||
               annotatedClass.isAnnotationPresent(Controller.class)||annotatedClass.isAnnotationPresent(Service.class);
    }

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
