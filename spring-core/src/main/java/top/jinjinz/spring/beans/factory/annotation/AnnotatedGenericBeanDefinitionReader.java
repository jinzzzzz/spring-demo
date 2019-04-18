package top.jinjinz.spring.beans.factory.annotation;

import top.jinjinz.spring.beans.factory.support.AbstractBeanDefinitionReader;
import top.jinjinz.spring.beans.factory.support.BeanDefinitionRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取注解类为BeanDefinition
 * @author jinjin
 * @date 2019-04-15
 */
public class AnnotatedGenericBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public AnnotatedGenericBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(String location) throws IOException {

      return 0;
    }
}
