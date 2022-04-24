package com.example.shardingsphere_xa.config;


import lombok.SneakyThrows;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


/**
 * 数据源和事务管理器配置
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/20
 */
@Configuration
public class DataSourceConfigurer {

    @Bean
    public DataSource shardingXaDataSource() throws SQLException, IOException {
        String fileName = "D:\\ideaProjects\\jikeshijian\\mygeekbangwork\\homework\\shardingsphere_xa\\src\\main\\resources\\ShardingSphere-JDBC-autoCommit.yaml";
        File yamlFile = new File(fileName);
        return YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
    }

    /**
     * 不能和 Properties 文件的myBatis 的 MapperLocations 的配置项重复存在,重复存在的话启动直接报错，找不到xml文件
     *
     * @return
     */
    @SneakyThrows
    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTypeAliasesPackage("com.example.shardingsphere_xa.bean");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setDataSource(shardingXaDataSource());
        return sqlSessionFactoryBean;
    }

    /**
     * 如果没有定义这个事务管理器，则事务@Transation 不会起作用，会出现部分提交的情况
     * Transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException, IOException {
        return new DataSourceTransactionManager(shardingXaDataSource());
    }


}
