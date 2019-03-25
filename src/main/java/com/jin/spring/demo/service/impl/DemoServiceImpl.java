package com.jin.spring.demo.service.impl;

import com.jin.spring.annotation.Service;
import com.jin.spring.demo.service.DemoService;

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
