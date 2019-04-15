package top.jinjinz.spring.context.support;


import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.context.ApplicationContext;


/**
 * ApplicationContexts顶层抽象类
 * author:jinjin
 * Date:2019/4/14 3:35
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    public void refresh() throws Exception{
        BeanFactory beanFactory = obtainFreshBeanFactory();
    }

    protected BeanFactory obtainFreshBeanFactory() throws Exception{
        //具体实现调用子类容器的refreshBeanFactory()方法
        refreshBeanFactory();
        BeanFactory beanFactory = getBeanFactory();
        return beanFactory;
    }

    protected abstract void refreshBeanFactory() throws Exception;

    public abstract BeanFactory getBeanFactory();
}
