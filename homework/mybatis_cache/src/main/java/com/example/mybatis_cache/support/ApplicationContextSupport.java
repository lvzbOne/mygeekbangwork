package com.example.mybatis_cache.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/19
 */
@Component
public class ApplicationContextSupport implements ApplicationContextAware {

    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextSupport.context = applicationContext;
    }
}
