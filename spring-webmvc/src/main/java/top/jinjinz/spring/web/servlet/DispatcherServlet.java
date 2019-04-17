package top.jinjinz.spring.web.servlet;


import top.jinjinz.spring.context.ApplicationContext;
import top.jinjinz.spring.context.support.PropertiesApplicationContext;
import top.jinjinz.spring.web.servlet.method.HandlerMethod;
import top.jinjinz.spring.web.servlet.method.HandlerMethodAdapter;
import top.jinjinz.spring.web.servlet.method.HandlerMethodMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 启动入口
 * @author jinjin
 * @date 2019-04-13
 */
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;

    private HandlerAdapter handlerAdapter;

    public DispatcherServlet(){ super(); }

    /**
     * 初始化，加载配置文件
     */
    public void init(ServletConfig config) throws ServletException {
        try {
            PropertiesApplicationContext context =
                    new PropertiesApplicationContext("application.properties");
            initStrategies(context);
        }catch (Exception e){
            e.printStackTrace();
        }

        //提示信息
        System.out.println("mvc is init");
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }


    /**
     * 执行业务处理
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            //匹配对应方法
            doDispatch(req,resp);
        }catch(Exception e){
            resp.getWriter().write("500 Exception,Details:\r\n"
                    + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "")
                    .replaceAll(",\\s", "\r\n"));
        }
    }

    private void doDispatch(HttpServletRequest req,HttpServletResponse resp) throws Exception{
        HandlerMethod handler = handlerMapping.getHandler(req);
        if(null == handler){
            resp.getWriter().write("404 Not Found");
            return;
        }
        ModelAndView mv = handlerAdapter.handle(req,resp,handler);
        //resp.getWriter().write(mv.getModel().values().toString());
    }

    //初始化策略
    private void initStrategies(ApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);
        //初始化Handler映射处理器
        initHandlerMappings(context);
        //初始化参数适配器
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        //FlashMap管理器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(ApplicationContext context) {
    }

    private void initViewResolvers(ApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapter = new HandlerMethodAdapter();
    }

    private void initHandlerMappings(ApplicationContext context) {
        this.handlerMapping = new HandlerMethodMapping(context);
    }

    private void initThemeResolver(ApplicationContext context) {
    }

    private void initLocaleResolver(ApplicationContext context) {
    }

    private void initMultipartResolver(ApplicationContext context) {
    }
}
