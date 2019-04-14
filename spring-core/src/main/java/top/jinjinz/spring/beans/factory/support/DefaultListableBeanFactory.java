package top.jinjinz.spring.beans.factory.support;

import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory的默认实现
 * author:jinjin
 * Date:2019/4/14 4:00
 */
public class DefaultListableBeanFactory implements BeanFactory {
    //存储注册信息的BeanDefinition
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public Object getBean(String name) {
        //doGetBean才是真正向IoC容器获取被管理Bean的过程
        return doGetBean(name);
    }

    protected Object doGetBean(final String name){
        return beanDefinitionMap.get(name);
    }
}
