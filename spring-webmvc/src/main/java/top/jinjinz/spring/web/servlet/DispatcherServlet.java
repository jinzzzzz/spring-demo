package top.jinjinz.spring.web.servlet;


import top.jinjinz.spring.beans.factory.annotation.*;
import top.jinjinz.spring.context.support.PropertiesApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 启动入口
 * @author jinjin
 * @date 2019-04-13
 */
public class DispatcherServlet extends HttpServlet {


    public DispatcherServlet(){ super(); }

    /**
     * 初始化，加载配置文件
     */
    public void init(ServletConfig config) throws ServletException {
        try {
            PropertiesApplicationContext context = new PropertiesApplicationContext("application.properties");
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

    private void doDispatch(HttpServletRequest req,HttpServletResponse resp) {

    }
}
