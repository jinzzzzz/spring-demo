package top.jinjinz.spring.web.servlet;

import top.jinjinz.spring.web.servlet.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 执行Handler的方法,动态匹配参数
 * @author jinjin
 * @date 2019-04-17
 */
public interface HandlerAdapter {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception;
}
