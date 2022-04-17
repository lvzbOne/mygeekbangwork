package com.example.dynamic_data_source.service.impl;

import com.example.dynamic_data_source.service.TestService;
import org.aspectj.lang.annotation.Around;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@SpringBootTest
class TestServiceImplTest {

    @Autowired
    private TestService testService;

    @Test
    void testAspect() {
        testService.info("黄昇平", 25);
    }
}