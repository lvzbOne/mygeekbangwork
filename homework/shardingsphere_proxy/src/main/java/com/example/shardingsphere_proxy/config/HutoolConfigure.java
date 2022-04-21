package com.example.shardingsphere_proxy.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 糊涂工具包用 SpringIOC管理 Bean
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
@Configuration
public class HutoolConfigure {
    @Value("${id.workerId:12}")
    private long workerId;

    @Value("${id.datacenterId:12}")
    private long datacenterId;

    @Bean
    public Snowflake snowflake() {
        return IdUtil.createSnowflake(workerId, datacenterId);
    }
}
