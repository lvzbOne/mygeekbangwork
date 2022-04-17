package com.example.dynamic_data_source.aspect;

import com.example.dynamic_data_source.support.DynamicDataSourceContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 数据源切面
 * 自定义切面和spring @Transactional事务的切面优先级别没想到自定义切面的级别更低
 * 这样就使得
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */

//@Order(-100)
//@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    //@SneakyThrows
    @Before("@annotation(com.example.dynamic_data_source.annotation.Master)")
    private void executeInsert(JoinPoint point) {
        log.warn("==========> executeInsert @Before useMasterDataSource<==================");
        DynamicDataSourceContextHolder.useMasterDataSource();
    }

    @SneakyThrows
    @Around("@annotation(com.example.dynamic_data_source.annotation.Slave)")
    private Object executeQuery(ProceedingJoinPoint point) {
        log.warn("==========> executeQuery @Around useSlaveDataSource<==================");
        log.warn("==========>标签：[{}],参数：[{}]", point.getSignature(), point.getArgs().length);
        DynamicDataSourceContextHolder.useSlaveDataSource();
        Object proceed = point.proceed(point.getArgs());
        return proceed;
    }
}
