package top.jinjinz.spring.test.controller;

import top.jinjinz.spring.beans.factory.annotation.Autowired;
import top.jinjinz.spring.beans.factory.annotation.Controller;
import top.jinjinz.spring.beans.factory.annotation.RequestMapping;
import top.jinjinz.spring.beans.factory.annotation.RequestParam;
import top.jinjinz.spring.test.service.DemoService;

import javax.servlet.http.HttpServletResponse;

/**
 * Demo控制器
 * @author jinjin
 * @date 2019-03-25
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/hello")
    public void hello(@RequestParam("name") String name, HttpServletResponse resp) throws Exception{
        resp.getWriter().write(demoService.hello(name));
    }
}
