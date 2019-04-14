package top.jinjinz.spring.beans.factory.support;

import top.jinjinz.spring.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition基本实现
 * author:jinjin
 * Date:2019/4/13 23:27
 */
public class AbstractBeanDefinition implements BeanDefinition{

    private volatile String beanClass;

    private boolean lazyInit = false;

    private String factoryBeanName;

    protected AbstractBeanDefinition(){};

    protected AbstractBeanDefinition(BeanDefinition original) {
        setBeanClassName(original.getBeanClassName());
        setLazyInit(original.isLazyInit());
        setFactoryBeanName(original.getFactoryBeanName());
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return beanClass;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }
}
