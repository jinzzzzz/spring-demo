package top.jinjinz.spring.beans.factory;

/**
 * 用于产生其他bean实例
 * author:jinjin
 * Date:2019/4/13 23:06
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
