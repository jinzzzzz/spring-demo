package top.jinjinz.spring.beans.factory.support;

import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.beans.factory.config.BeanDefinition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory的默认实现
 * author:jinjin
 * Date:2019/4/14 4:00
 */
public class DefaultListableBeanFactory implements BeanFactory,BeanDefinitionRegistry {
    //存储注册信息的BeanDefinition
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /** 缓存singleton类型实例: bean name --> bean instance */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);


    @Override
    public Object getBean(String name) throws Exception{
        return doGetBean(name);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName){
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        return beanDefinition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
       return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }


    //从容器中获取beanDefinition，如果没有创建实例则创建实例并初始化
    private Object doGetBean(final String beanName) throws Exception{
        //先从缓存中取已经被创建的singleton类型的Bean
        Object singletonInstance =  this.singletonObjects.get(beanName);
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;
        //开始创建singleton实例
        if(null == singletonInstance){
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
            instance = clazz.newInstance();
            this.singletonObjects.put(beanDefinition.getBeanClassName(),instance);
            this.singletonObjects.put(beanDefinition.getFactoryBeanName(),instance);
        }
        return instance;
    }


}
