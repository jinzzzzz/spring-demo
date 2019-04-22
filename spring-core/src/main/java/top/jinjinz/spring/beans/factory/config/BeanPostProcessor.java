package top.jinjinz.spring.beans.factory.config;

import top.jinjinz.spring.beans.factory.BeanFactory;

/**
 * 允许自定义修改代理的实例的工厂钩子
 * @author jinjin
 * @date 2019-04-18
 */
public interface BeanPostProcessor {
    //为在Bean的初始化前提供回调入口
    default Object postProcessBeforeInitialization(Object bean, String beanName, BeanFactory beanFactory) throws Exception {
        return bean;
    }

    //为在Bean的初始化之后提供回调入口
    default Object postProcessAfterInitialization(Object bean, String beanName, BeanFactory beanFactory) throws Exception {
        return bean;
    }
}
