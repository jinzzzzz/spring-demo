package top.jinjinz.spring.context.support;

import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.beans.factory.config.BeanDefinition;
import top.jinjinz.spring.beans.factory.support.BeanDefinitionRegistry;
import top.jinjinz.spring.beans.factory.support.DefaultListableBeanFactory;
import top.jinjinz.spring.context.annotation.AnnotatedBeanDefinitionReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * PropertiesApplication方式
 * @author jinjin
 * @date 2019-04-15
 */
public class PropertiesApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    //private final AnnotatedGenericBeanDefinitionReader reader;

    private Properties config = new Properties();

    private final String SCAN_PACKAGE = "scanPackage";

    private Set<Class<?>> annotatedClasses =  new LinkedHashSet<>();

    private DefaultListableBeanFactory beanFactory;


    public PropertiesApplicationContext(String... locations) throws Exception{
        InputStream is =
                this.getClass().getClassLoader().getResourceAsStream(
                        locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
        refresh();
    }

    @Override
    protected void refreshBeanFactory() throws Exception{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        this.beanFactory = beanFactory;
        loadBeanDefinitions(beanFactory);
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception{
        this.beanFactory.registerBeanDefinition(beanName,beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanFactory.removeBeanDefinition(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanFactory.containsBeanDefinition(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanFactory.getBeanDefinitionNames();
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanFactory.getBeanDefinitionCount();
    }

    private void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws Exception{
        AnnotatedBeanDefinitionReader reader =
                new AnnotatedBeanDefinitionReader(beanFactory);
        if (!this.annotatedClasses.isEmpty()) {
            reader.register(this.annotatedClasses.toArray(new Class<?>[0]));
        }

        //将注册的bean进行初始化,默认不进行懒加载
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName:beanNames) {
            getBean(beanName);
        }
        System.out.println("debug");
    }

    private void doScanner(String scanPackage) throws Exception {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if (null == url) {
            return;
        }
        File dir = new File(url.getFile());
        File[] dirs = dir.listFiles();
        if (null == dirs) {
            return;
        }
        //找出所有的文件名称,去除.class后缀
        for (File file : dirs) {
            //如果是文件夹则递归
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                Class<?> beanClass = Class.forName(
                        scanPackage + "." + file.getName().replace(".class", "").trim());
                if (!beanClass.isInterface()) {
                    this.annotatedClasses.add(beanClass);
                }
            }
        }
    }
}
