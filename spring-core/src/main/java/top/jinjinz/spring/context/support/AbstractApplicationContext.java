package top.jinjinz.spring.context.support;


import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.context.ApplicationContext;


/**
 * ApplicationContexts顶层抽象类
 * author:jinjin
 * Date:2019/4/14 3:35
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    public void refresh(){
        BeanFactory beanFactory = obtainFreshBeanFactory();
    }

    protected BeanFactory obtainFreshBeanFactory() {
        //具体实现调用子类容器的refreshBeanFactory()方法
        refreshBeanFactory();
        BeanFactory beanFactory = getBeanFactory();
        return beanFactory;
    }

    protected abstract void refreshBeanFactory();

    public abstract BeanFactory getBeanFactory();

    @Override
    public Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }
}
