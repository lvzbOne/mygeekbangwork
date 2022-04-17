package com.example.dynamic_data_source.support;

import com.example.dynamic_data_source.enums.DataSourceKey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源上下文持有者
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    private static int counter = 0;

    /**
     * Maintain variable for every thread, to avoid effect other thread
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.master::name);

    /**
     * The constant dataSourceKeys.
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * The constant slaveDataSourceKeys.
     */
    public static List<Object> slaveDataSourceKeys = new ArrayList<>();

    /**
     * Use master data source.
     */
    public static void useMasterDataSource() {
        CONTEXT_HOLDER.set(DataSourceKey.master.name());
    }


    public static void useSlaveDataSource() {
        try {
            // 默认轮询
            int datasourceKeyIndex = counter % slaveDataSourceKeys.size();
            CONTEXT_HOLDER.set(String.valueOf(slaveDataSourceKeys.get(datasourceKeyIndex)));
            counter++;
        } catch (Exception e) {
            log.error("Switch slave datasource failed, error message is {}", e.getMessage());
            useMasterDataSource();
            e.printStackTrace();
        }
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

}
