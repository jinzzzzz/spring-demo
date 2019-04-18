package top.jinjinz.spring.aop.autoproxy;

import top.jinjinz.spring.aop.ProxyFactory;
import top.jinjinz.spring.beans.factory.config.BeanPostProcessor;

/**
 * 自动创建代理
 * @author jinjin
 * @date 2019-04-18
 */
public class AutoProxyCreator implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if (bean != null) {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.setTargetClass(bean.getClass());
            return proxyFactory.getProxy();
        }
        return bean;
    }
}
