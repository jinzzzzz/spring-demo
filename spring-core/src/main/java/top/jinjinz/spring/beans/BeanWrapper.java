package top.jinjinz.spring.beans;

/**
 * 对Bean的包装类
 * author:jinjin
 * Date:2019/4/13 23:39
 */
public interface BeanWrapper {

    Object getWrappedInstance();

    Class<?> getWrappedClass();
}
