package top.jinjinz.spring.beans.factory.support;

import top.jinjinz.spring.beans.BeanWrapper;
import top.jinjinz.spring.beans.factory.BeanFactory;
import top.jinjinz.spring.beans.factory.annotation.Autowired;
import top.jinjinz.spring.beans.factory.config.BeanDefinition;
import top.jinjinz.spring.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory的默认实现
 * author:jinjin
 * Date:2019/4/14 4:00
 */
public class DefaultListableBeanFactory implements BeanFactory,BeanDefinitionRegistry {
    //存储注册信息的BeanDefinition
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /** 缓存singleton类型实例: bean name --> bean instance */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /** 正在创建的bean集合 */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /** 创建bean时应用的处理器 */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws Exception{
        return doGetBean(name);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception{
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName){
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        return beanDefinition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
       return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }


    //从容器中获取beanDefinition，如果没有创建实例则创建实例并初始化
    private Object doGetBean(final String beanName) throws Exception{
        //先从缓存中取已经被创建的singleton类型的Bean
        Object singletonInstance =  this.singletonObjects.get(beanName);
        if(null != singletonInstance){
            return singletonInstance;
        }

        //循环引用
        if(singletonsCurrentlyInCreation.contains(beanName)){
            throw new RuntimeException(beanName+"发生循环引用");
        }

        //开始创建单例的实例
        Object instance = null;
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        if(null == beanDefinition){
            throw new RuntimeException(beanName+"不存在");
        }

        Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
        instance = clazz.newInstance();
        populateBean(instance,beanDefinition,clazz);

        if (false) {
            instance = applyBeanPostProcessorsBeforeInitialization(instance, beanName);
            instance = applyBeanPostProcessorsAfterInitialization(instance, beanName);
        }

        //将类名和注解值都作为key值放入map,接口将类型名称存入
        this.singletonObjects.put(beanDefinition.getBeanClassName(),instance);
        this.singletonObjects.put(beanDefinition.getFactoryBeanName(),instance);
        return instance;
    }

    //调用BeanPostProcessor 初始化调用实例之前的处理方法
    private Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws Exception {
        Object result = existingBean;
        //遍历容器为所创建的Bean添加的所有BeanPostProcessor
        for (BeanPostProcessor beanProcessor : beanPostProcessors) {
            //Bean实例对象在初始化之前做一些自定义的处理操作
            Object current = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    //调用BeanPostProcessor 初始化调用实例之后的处理方法
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws Exception {

        Object result = existingBean;
        //遍历容器为所创建的Bean添加的所有BeanPostProcessor
        for (BeanPostProcessor beanProcessor : beanPostProcessors) {
            //Bean实例对象在初始化之后做一些自定义的处理操作
            Object current = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    //对字段进行依赖注入,只注入对象类型,基本类型未判断
    private void populateBean(Object instance, BeanDefinition beanDefinition, Class<?> clazz) throws Exception{
        Object autowiredBean;
        //加入正在创建的bean集合中，防止循环引用
        singletonsCurrentlyInCreation.add(beanDefinition.getBeanClassName());
        //获得所有的字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //只对加了注解的字段进行依赖注入
            if(!field.isAnnotationPresent(Autowired.class)){ continue;}

            Autowired autowired = field.getAnnotation(Autowired.class);

            String autowiredBeanName =  autowired.value().trim();
            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }

            //访问私有变量
            field.setAccessible(true);

            try {
                //获取需要注入的实例
                autowiredBean = this.singletonObjects.get(autowiredBeanName);
                //如果需要注入的实例还未创建,则创建
                if(null == autowiredBean){
                    //递归创建bean
                    getBean(autowiredBeanName);
                    /*autowiredBean = this.singletonObjects.get(autowiredBeanName);
                    if(null == autowiredBean){
                        throw new RuntimeException(autowiredBeanName+"不存在");
                    }*/
                }
                field.set(instance,autowiredBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        //注入完成后删除
        singletonsCurrentlyInCreation.remove(beanDefinition.getBeanClassName());
    }
}
