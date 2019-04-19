package top.jinjinz.spring.beans.factory.support;

import top.jinjinz.spring.beans.factory.config.BeanDefinition;
import top.jinjinz.spring.beans.factory.config.BeanPostProcessor;

/**
 * 保存BeanDefinition的容器
 * @author jinjin
 * @date 2019-04-15
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    //增加bean回调处理器
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
