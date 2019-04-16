package top.jinjinz.spring.test.service.impl;

import top.jinjinz.spring.beans.factory.annotation.Service;
import top.jinjinz.spring.test.service.DemoService;

/**
 * Demo Service实现
 * @author jinjin
 * @date 2019-03-25
 */
@Service
public class DemoServiceImpl implements DemoService {


    @Override
    public String hello(String name) {
        return "hello "+name;
    }
}
