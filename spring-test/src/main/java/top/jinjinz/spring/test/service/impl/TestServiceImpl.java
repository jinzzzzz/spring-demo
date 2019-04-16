package top.jinjinz.spring.test.service.impl;

import top.jinjinz.spring.beans.factory.annotation.Autowired;
import top.jinjinz.spring.beans.factory.annotation.Service;
import top.jinjinz.spring.test.service.DemoService;
import top.jinjinz.spring.test.service.TestService;

/**
 * 测试service实现
 * @author jinjin
 * @date 2019-04-16
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private DemoService demoService;

    @Override
    public String test(String name) {
        return demoService.hello(name);
    }
}
