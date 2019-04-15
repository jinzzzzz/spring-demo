package top.jinjinz.spring.beans.factory.support;

import java.io.IOException;

/**
 * 将Bean定义信息读取为BeanDefinition
 * @author jinjin
 * @date 2019-04-15
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    int loadBeanDefinitions(String location) throws IOException;

    int loadBeanDefinitions(String... locations) throws IOException;
}
