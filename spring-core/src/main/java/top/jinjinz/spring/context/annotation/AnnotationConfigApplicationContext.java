package top.jinjinz.spring.context.annotation;

import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.beans.factory.support.DefaultListableBeanFactory;
import top.jinjinz.spring.context.support.AbstractApplicationContext;

/**
 * 基于注解初始化容器
 * author:jinjin
 * Date:2019/4/14 3:57
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {
    private DefaultListableBeanFactory beanFactory;


    @Override
    protected void refreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        this.beanFactory = beanFactory;
    }

    @Override
    public BeanFactory getBeanFactory() {
        return null;
    }
}
