package com.example.dynamic_data_source.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态路由数据源
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.error("[DynamicDataSourceContextHolder.getDataSourceKey: {}]", DynamicDataSourceContextHolder.getDataSourceKey());
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}
