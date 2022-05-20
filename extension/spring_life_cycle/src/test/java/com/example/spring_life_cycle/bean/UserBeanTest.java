package com.example.spring_life_cycle.bean;

import com.example.spring_life_cycle.config.LifeCycleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
@SpringBootTest
class UserBeanTest {

    @Test
    void beanLifeCycleTest() {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
//        UserBean userBean = (UserBean) context.getBean("userBean");
//        ((AbstractApplicationContext) context).close();
    }
}