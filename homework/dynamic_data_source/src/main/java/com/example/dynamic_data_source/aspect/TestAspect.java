package com.example.dynamic_data_source.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Aspect
@Component
@Slf4j
public class TestAspect {

    @Pointcut("@annotation(com.example.dynamic_data_source.annotation.M1)")
    private void pointcut() {
    }

    @Before("pointcut()")
    private void executeParam1() {
    }

    @Around("@annotation(com.example.dynamic_data_source.annotation.M1)")
    private void executeInsert(JoinPoint point) {

    }

    @Before("@annotation(com.example.dynamic_data_source.annotation.S1)")
    private void executeSelect(JoinPoint point) {
        Object[] args = point.getArgs();
        log.warn("=======> @Before 标识 参数类型:{},参数个数{}", args.toString(), args.length);
    }
}
