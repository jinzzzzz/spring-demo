package top.jinjinz.spring.test.servlet;


import top.jinjinz.spring.beans.factory.annotation.*;

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
    private static final String LOCATION = "contextConfigLocation";

    private Properties p = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String,Object> ioc = new HashMap<String,Object>();

    //保存Url和方法的映射关系
    private List<Handler> handlerMapping = new ArrayList<Handler>();

    public DispatcherServlet(){ super(); }

    /**
     * 初始化，加载配置文件
     */
    public void init(ServletConfig config) throws ServletException {

        //1、加载配置文件
        doLoadConfig(config.getInitParameter(LOCATION));

        //2、扫描所有相关的类
        doScanner(p.getProperty("scanPackage"));

        //3、初始化所有相关类的实例，并保存到IOC容器中
        doInstance();

        //4、依赖注入
        doAutowired();

        //5、构造HandlerMapping
        initHandlerMapping();

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


    /**
     * 递归扫描出所有Class文件
     * @param packageName 扫描路径
     */
    private void doScanner(String packageName){
        //将所有的包路径转换为文件路径
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        if(null==url){
            return;
        }
        File dir = new File(url.getFile());
        File[] dirs=dir.listFiles();
        if(null == dirs){
            return;
        }
        //找出所有的文件名称,去除.class后缀
        for (File file :dirs) {
            //如果是文件夹则递归
            if(file.isDirectory()){
                doScanner(packageName + "." + file.getName());
            }else{
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }


    private void doLoadConfig(String location){
        InputStream fis = null;
        try {
            //加载配置文件
            fis = this.getClass().getClassLoader().getResourceAsStream(location);
            //读取配置文件
            p.load(fis);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(null != fis){fis.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void doInstance(){
        //扫描路径中没有找到任何类则直接返回
        if(classNames.size() == 0){ return; }

        try{
            //循环查找到的所有类,对加了注解的类进行处理
            for (String className : classNames) {
                //根据类名查找
                Class<?> clazz = Class.forName(className);
                //判断是否加了Controller注解
                if(clazz.isAnnotationPresent(Controller.class)){
                    //默认将首字母小写作为beanName
                    String beanName = lowerFirst(clazz.getSimpleName());
                    //将beanName作为key放入到ioc容器中
                    ioc.put(beanName, clazz.newInstance());
                }else if(clazz.isAnnotationPresent(Service.class)){
                    //判断是否加了Service注解
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();
                    //如果注解中value不为空，则用此value
                    if(!"".equals(beanName.trim())){
                        ioc.put(beanName, clazz.newInstance());
                        continue;
                    }
                    //注解没有设置value，则使用接口的类型作为key
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        //将接口的类型作为key放入到ioc容器中
                        ioc.put(i.getName(), clazz.newInstance());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    private void doAutowired(){
        //ioc容器为空则直接返回
        if(ioc.isEmpty()){ return; }

        //为ioc中所有的实例进行依赖注入
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //拿到实例对象中的所有属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            //拿到实例的所有属性判断是否需要依赖注入
            for (Field field : fields) {
                //如果不需要依赖注入则直接返回
                if(!field.isAnnotationPresent(Autowired.class)){ continue; }

                Autowired autowired = field.getAnnotation(Autowired.class);
                //如果设置了value,则根据value进行装配
                String beanName = autowired.value().trim();
                //若没有设置value则根据类型进行装配
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                }
                //设置私有属性的访问权限
                field.setAccessible(true);
                try {
                    //注入属性值
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void initHandlerMapping(){
        if(ioc.isEmpty()){ return; }

        //从ioc的实例中取出需要做url映射的类
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            //如果此类不需要映射则直接返回
            if(!clazz.isAnnotationPresent(Controller.class)){ continue; }

            String url = "";
            //获取Controller的url配置
            if(clazz.isAnnotationPresent(RequestMapping.class)){
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                url = requestMapping.value();
            }

            //获取Method的url配置
            Method[] methods = clazz.getMethods();
            //获取类中的所有方法,将需要映射的方法加入handlerMapping中
            for (Method method : methods) {

                //没有加RequestMapping注解的直接忽略
                if(!method.isAnnotationPresent(RequestMapping.class)){ continue; }

                //映射URL
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String regex = ("/" + url + requestMapping.value()).replaceAll("/+", "/");
                //正则匹配
                Pattern pattern = Pattern.compile(regex);
                //加入到handlerMapping中
                handlerMapping.add(new Handler(pattern,entry.getValue(),method));
                System.out.println("mapping " + regex + "," + method);
            }
        }

    }


    /**
     * 首字母小母
     * @param str 字符串
     * @return 小写
     */
    private String lowerFirst(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 匹配URL
     * @param req req
     * @param resp resp
     */
    private void doDispatch(HttpServletRequest req,HttpServletResponse resp) {

        try{
            //获取匹配的Handler
            Handler handler = getHandler(req);

            if(handler == null){
                //如果没有匹配上，返回404错误
                resp.getWriter().write("404 Not Found");
                return;
            }

            //获取方法的参数列表
            Class<?> [] paramTypes = handler.method.getParameterTypes();

            //保存所有需要自动赋值的参数值
            Object [] paramValues = new Object[paramTypes.length];

            Map<String,String[]> params = req.getParameterMap();
            //获取请求参数列表,将方法对应的参数进行赋值
            for (Map.Entry<String, String[]> param : params.entrySet()) {
                String value = Arrays.toString(param.getValue())
                        .replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");

                //如果找到匹配的对象，则开始填充参数值
                if(!handler.paramIndexMapping.containsKey(param.getKey())){continue;}
                int index = handler.paramIndexMapping.get(param.getKey());
                paramValues[index] = convert(paramTypes[index],value);
            }

            //设置方法中的request和response对象
            Integer reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            if(null!=reqIndex) {
                paramValues[reqIndex] = req;
            }
            Integer respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            if(null!=respIndex) {
                paramValues[respIndex] = resp;
            }
            //反射调用方法
            handler.method.invoke(handler.controller, paramValues);

        }catch(Exception e){
           e.printStackTrace();
        }
    }

    private Handler getHandler(HttpServletRequest req) throws Exception{
        if(handlerMapping.isEmpty()){ return null; }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        //循环handlerMapping匹配Handler
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.pattern.matcher(url);
            //如果没有匹配上继续下一个匹配
            if(!matcher.matches()){ continue; }
            return handler;
        }
        return null;
    }


    private Object convert(Class<?> type,String value){
        if(Integer.class == type){
            return Integer.valueOf(value);
        }
        return value;
    }

    /**
     * Handler记录Controller中的RequestMapping和Method的对应关系
     * 内部类
     */
    private class Handler{

        Object controller;	//保存方法对应的实例
        Method method;		//保存映射的方法
        Pattern pattern;   //正则
        Map<String,Integer> paramIndexMapping;	//参数顺序

        /**
         * 构造一个Handler基本的参数
         * @param controller 方法对应的实例
         * @param method 映射的方法
         */
        Handler(Pattern pattern,Object controller,Method method){
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;

            paramIndexMapping = new HashMap<String,Integer>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method){

            //提取方法中加了注解的参数
            Annotation[] [] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length ; i ++) {
                //获取注解
                for(Annotation a : pa[i]){
                    //如果是需要赋值的属性则将属性名称和位置加入到paramIndexMapping中
                    if(a instanceof RequestParam){
                        String paramName = ((RequestParam) a).value();
                        if(!"".equals(paramName.trim())){
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }

            //提取方法中的request和response参数
            Class<?> [] paramsTypes = method.getParameterTypes();
            for (int i = 0; i < paramsTypes.length ; i ++) {
                Class<?> type = paramsTypes[i];
                if(type == HttpServletRequest.class ||
                        type == HttpServletResponse.class){
                    paramIndexMapping.put(type.getName(),i);
                }
            }
        }
    }

}
