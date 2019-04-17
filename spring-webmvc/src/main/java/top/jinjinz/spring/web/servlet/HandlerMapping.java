package top.jinjinz.spring.web.servlet;

import top.jinjinz.spring.web.servlet.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Handler映射处理器
 * @author jinjin
 * @date 2019-04-17
 */
public interface HandlerMapping {

    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
