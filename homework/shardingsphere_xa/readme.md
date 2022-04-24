# 作业概览

> 如下代码,开启xa事务执行,和异常时事务回滚，预设第一个分片批量插入是成功的,第二个分片批量插入抛异常 预期结果若是 2个批量插入都回滚！即完成本作业

```java
@Component
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrdersMapper ordersMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.XA)
    public void addOrderListXA(List<Order> data, List<Order> data1) {
        if (!ObjectUtils.isEmpty(data) && !ObjectUtils.isEmpty(data1)) {
            ordersMapper.insertOrderListContainOrderId(data);
            ordersMapper.insertOrderListContainOrderId(data1);
        }
    }
}
```
## 具体实现
- 使用shardingSphere数据源
- mybatis 使用 shardingSphere数据源
- 定义事务管理器管理shardingSphere数据源


## 一、pom 文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.7</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>shardingsphere_xa</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>shardingsphere_xa</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
        <hutool.version>5.3.2</hutool.version>
    </properties>
    <dependencies>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>shardingsphere-jdbc-core</artifactId>
            <version>5.0.0-alpha</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>shardingsphere-transaction-xa-core</artifactId>
            <version>5.0.0-alpha</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--糊涂工具包-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>*</groupId>
                    <artifactId>*</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

## 二、shardingSphere数据库配置ymal 文件
> 创建2个逻辑数据库

![在这里插入图片描述](https://img-blog.csdnimg.cn/2a94638a77e8495cb967a48ccc744452.png)
> 2个逻辑数据里分表创建2张表 t_order_0,t_order_1
```mysql
CREATE TABLE `t_order_0` (
`order_id` bigint NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`status` varchar(50) DEFAULT NULL,
PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=724640827049488449 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_order_1` (
`order_id` bigint NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`status` varchar(50) DEFAULT NULL,
PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=724640722485489730 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
### yaml文件配置sharingSphere数据源和分片规则

```yaml
dataSources:
  ds_0: !!com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:33061/demo_ds_0?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 123456
  ds_1: !!com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:33061/demo_ds_1?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 123456

rules:
  - !SHARDING
    tables:
      t_order:
        actualDataNodes: ds_${0..1}.t_order_${0..1}
        databaseStrategy:
          standard:
            shardingColumn: user_id
            shardingAlgorithmName: database_inline
        tableStrategy:
          standard:
            shardingColumn: order_id
            shardingAlgorithmName: t_order_inline
    bindingTables:
      - t_order

    shardingAlgorithms:
      database_inline:
        type: INLINE
        props:
          algorithm-expression: ds_${user_id % 2}
      t_order_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_${order_id % 2}

props:
  sql-show: true
```

## 三、config配置类

```java
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
     * 不能和 Properties 文件MapperLocations内配置的重复,重复的话启动直接报错
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
     * 如果没有定义事务管理器，x-a事务用@Transcation 注解不会回滚
     * Transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException, IOException {
        return new DataSourceTransactionManager(shardingXaDataSource());
    }
}

```

## 四、测试类代码
```java
package com.example.shardingsphere_xa.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.example.shardingsphere_xa.bean.Order;
import com.example.shardingsphere_xa.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/23
 */
@Slf4j
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Resource
    private Snowflake snowflake;
    @Autowired
    private DataSource dataSource;

    private static final Integer NUMBER = 16;
    private static final Integer TEN = 10;
    private static final String[] POEM = {"锦瑟无端五十弦", "一弦一柱思华年",
            "庄生晓梦迷蝴蝶", "望帝春心托杜鹃",
            "沧海月明珠有泪", "蓝田日暖玉生烟",
            "此情可待成追忆", "只是当时已惘然"};

    /**
     * 单元测试里添加了 @Transactional 并不会真正提交数据，大赞！
     */
    @Test
    @Transactional
    void addOrderList() {
        deleteOrders();
        List<Order> ordersList = createOrdersList(NUMBER);
        orderService.addOrderList(ordersList);
        getOrders();
    }

    /**
     * X-A 事务测试类
     */
    @Test
    void addOrderListXA() {
        deleteOrders();
        List<Order> tenOrders = createOrdersList(TEN);
        List<Order> ordersList = createOrdersList1(TEN);
        try {
            orderService.addOrderListXA(tenOrders, ordersList);
        } catch (Exception e) {
            log.error("error:[{}]", e.getMessage());
        }
        getOrders();
    }

    @Test
    void getOrders() {
        List<Order> orders = orderService.getOrders(null);
        if (!ObjectUtils.isEmpty(orders)) {
            List<Order> collect = orders.stream().sorted(Comparator.comparing(Order::getOrderId)).collect(Collectors.toList());
            for (Order order : collect) {
                log.warn("=========> {}", order.toString());
            }
        }
    }

    @Test
    void deleteOrders() {
        orderService.deleteOrders(null);
    }

    private List<Order> createOrdersList(int num) {
        List<Order> ordersList = new ArrayList<>(num);
        // 左闭右开[1,10)
        Random random = new Random();
        for (int i = 1; i <= num; i++) {
            Order orders = Order.builder()
                    .userId(i)
                    .orderId(Long.valueOf(i))
                    .status(POEM[random.nextInt(POEM.length)])
                    .build();
            ordersList.add(orders);
        }
        return ordersList;
    }

    private List<Order> createOrdersList1(int num) {
        List<Order> ordersList = new ArrayList<>(num);
        // 左闭右开[1,10)
        Random random = new Random();
        for (int i = 1; i <= num; i++) {
            Order orders = Order.builder()
                    .userId(i)
                    .orderId(Long.valueOf(i + 6))
                    .status(POEM[random.nextInt(POEM.length)])
                    .build();
            ordersList.add(orders);
        }
        return ordersList;
    }

    @Test
    void intToLong() {
        int i = 1;
        Integer it = 1;
        // long 接受 int 值是可以的，但是 Long 无法接收 int 需要使用 Long.valueOf(long l)
        // long l = i;
        Long l = Long.valueOf(i);
        System.out.println(l);
    }

}
```
## 五、踩坑
- 数据库里的bigint实际上对应java的long类型，如果使用BigInt 操作时会报错，本作业就遇到了 [详情地址](https://github.com/apache/shardingsphere/issues/13857)

## 参考资料
- [使用 ShardingSphereDataSource](https://shardingsphere.apache.org/document/5.0.0-alpha/cn/user-manual/shardingsphere-jdbc/usage/sharding/yaml/)
- [分片配置ShardingSphere官网参考](https://shardingsphere.apache.org/document/5.0.0-alpha/cn/user-manual/shardingsphere-jdbc/configuration/yaml/sharding/)
- [ShardingSphere的分布式事务](https://www.cnblogs.com/dalianpai/p/14001823.html)
- [ShardingSphere RAW JDBC 分布式事务 Atomikos XA 代码示例](https://xie.infoq.cn/article/75196b1dfdc0c71b8d66391b2)