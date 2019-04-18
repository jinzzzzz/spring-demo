package top.jinjinz.spring.aop.intercept;


/**
 * 连接点
 * @author jinjin
 * @date 2019-04-18
 */
public interface Joinpoint {

    Object proceed() throws Throwable;

    Object getThis();
}
