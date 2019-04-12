package com.jin.spring.demo.controller;

import com.jin.spring.annotation.Autowired;
import com.jin.spring.annotation.Controller;
import com.jin.spring.annotation.RequestMapping;
import com.jin.spring.annotation.RequestParam;
import com.jin.spring.demo.service.DemoService;

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
