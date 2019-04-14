package top.jinjinz.spring.beans.factory.config;

/**
 * 存储对Bean的解析信息
 * author:jinjin
 * Date:2019/4/13 23:16
 */
public interface BeanDefinition {

    void setBeanClassName(String beanClassName);

    String getBeanClassName();

    void setLazyInit(boolean lazyInit);

    boolean isLazyInit();

    void setFactoryBeanName(String factoryBeanName);

    String getFactoryBeanName();
}
