package com.example.dynamic_data_source.config;

import com.example.dynamic_data_source.enums.DataSourceKey;
import com.example.dynamic_data_source.support.DynamicDataSourceContextHolder;
import com.example.dynamic_data_source.support.DynamicRoutingDataSource;
import lombok.SneakyThrows;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置博客参考：https://hellowoodes.blog.csdn.net/article/details/78861442?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2.pc_relevant_paycolumn_v3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2.pc_relevant_paycolumn_v3&utm_relevant_index=5
 *
 * @author 起凤
 * @ConfigurationProperties 的作用参考：https://www.cnblogs.com/tian874540961/p/12146467.html
 * @description: TODO
 * @date 2022/4/15
 */
@Configuration
public class DataSourceConfigurer {
    /**
     * master DataSource
     *
     * @return data source
     */
    @Bean("master")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    /**
     * slave1 DataSource
     *
     * @return data source
     */
    @Bean("slave1")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave1")
    public DataSource slave1() {
        return DataSourceBuilder.create().build();
    }

    /**
     * slave2 DataSource
     *
     * @return data source
     */
    @Bean("slave2")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave2")
    public DataSource slave2() {
        return DataSourceBuilder.create().build();
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceKey.master.name(), master());
        dataSourceMap.put(DataSourceKey.slave1.name(), slave1());
        dataSourceMap.put(DataSourceKey.slave2.name(), slave2());

        // Set master datasource as default
        dynamicRoutingDataSource.setDefaultTargetDataSource(master());
        // Set master and slave datasource as target datasource
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        // To put datasource keys into DataSourceContextHolder to judge if the datasource is exist
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());

        // To put slave datasource keys into DataSourceContextHolder to load balance
        DynamicDataSourceContextHolder.slaveDataSourceKeys.addAll(dataSourceMap.keySet());
        DynamicDataSourceContextHolder.slaveDataSourceKeys.remove(DataSourceKey.master.name());
        return dynamicRoutingDataSource;
    }

    /**
     * 不能和 Properties 文件MapperLocations内配置的重复,重复的话这里会失效
     *
     * @return
     */
    @SneakyThrows
    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTypeAliasesPackage("com.example.dynamic_data_source.bean");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean;
    }

    /**
     * 配置后自定义切面的优先级就会比@Transcational高，可以不用设置@Order(最高优先级)
     * Transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }


}
