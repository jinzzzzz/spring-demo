package top.jinjinz.spring.beans.factory;

import top.jinjinz.spring.beans.factory.config.BeanDefinition;

/**
 * 定义IOC容器的基本功能接口
 * author:jinjin
 * Date:2019/4/13 22:19
 */
public interface BeanFactory {

    //根据bean的名字，获取在IOC容器中得到bean实例
    Object getBean(String name) throws Exception;
}
