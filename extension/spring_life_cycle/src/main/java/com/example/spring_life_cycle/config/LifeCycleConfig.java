package com.example.spring_life_cycle.config;

import com.example.spring_life_cycle.bean.MyBeanFactoryPostProcessor;
import com.example.spring_life_cycle.bean.MyBeanPostProcessor;
import com.example.spring_life_cycle.bean.MyInstantiationAwareBeanPostProcessor;
import com.example.spring_life_cycle.bean.UserBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/9
 */
@Configuration
public class LifeCycleConfig {

    @Bean
    public MyInstantiationAwareBeanPostProcessor myInstantiationAwareBeanPostProcessor() {
        return new MyInstantiationAwareBeanPostProcessor();
    }

    /**
     * TODO: initMethod 和 destroyMethod
     * @return
     */
    @Bean(initMethod = "myInit", destroyMethod = "myDestroy")
    public UserBean userBean() {
        UserBean userBean = new UserBean(1, "spring生命周期验证的一个bean");
        //userBean.setId(2);
        //userBean.setName("重新设置的bean");
        return userBean;
    }

    @Bean
    public MyBeanPostProcessor myBeanPostProcessor() {
        return new MyBeanPostProcessor();
    }

    @Bean
    public MyBeanFactoryPostProcessor myBeanFactoryPostProcessor() {
        return new MyBeanFactoryPostProcessor();
    }

}
