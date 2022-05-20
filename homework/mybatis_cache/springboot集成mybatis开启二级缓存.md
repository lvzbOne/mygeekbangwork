## Spring boot 整合mybatis 开启二级缓存初体验

## 前言

> 下面大部分内容来源于网上的相关帖子和官网，自己简单写了个demo体验了下，个人感觉mybatis的缓存并不是很合适
> 查询做缓存时，遇到更新操作就会刷新缓存，尤其是多表查询时，就会很难控制。对于那些需要缓存的热数据应该抽出来放到redis上做。

## mybatis 一级缓存和二级缓存的概念

> 之所以称之为“二级缓存”，是相对于“一级缓存”而言的。既然有了一级缓存，那么为什么要提供二级缓存呢？我们知道，在一级缓存中，不同session进行相同SQL查询的时候，是查询两次数据库的。显然这是一种浪费，既然SQL查询相同，就没有必要再次查库了，直接利用缓存数据即可，这种思想就是MyBatis二级缓存的初衷。

> 另外，Spring和MyBatis整合时，每次查询之后都要进行关闭sqlsession，关闭之后数据被清空。所以MyBatis和Spring整合之后，一级缓存是没有意义的。如果开启二级缓存，关闭sqlsession后，会把该sqlsession一级缓存中的数据添加到mapper namespace的二级缓存中。这样，缓存在sqlsession关闭之后依然存在。
>

## pom引入依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.2.2</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependencies>    
```

## application.properties 文件配置

```properties
spring.datasource.type=com.zaxxer.hikari.HikariDataSource
#Spring jdbc 数据源配置 需要mysql-connector-java驱动依赖
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/gefrm?useUnicode=true&characterEncoding=utf-8&useSSL=false&useInformationSchema=true&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false
spring.datasource.username=root
spring.datasource.password=123456
#mybatis配置
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.example.mybatis_cache.bean
mybatis.configuration.map-underscore-to-camel-case=true
#mybatis缓存
mybatis.configuration.cache-enabled=true
#mybatisSQL执行打印
logging.level.com.example.mybatis_cache.mapper=debug
```

## mapper.xml 文件配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatis_cache.mapper.BscDictInfoTestMapper">

    <!-- 这个cache 是关键 -->
    <cache eviction="LRU" flushInterval="100000" readOnly="true" size="1024"/>

    <!--可以通过设置useCache来规定这个sql是否开启缓存，ture是开启，false是关闭,刷新缓存：flushCache="true"  useCache="true"-->
    <select id="selectAll" resultType="com.example.mybatis_cache.bean.BscDictInfoTestDO">
        select *
        from bsc_dict_info_test
    </select>

    <!-- 多个命名空间缓存共享 级联标签 cache-ref 指定namespace即可 -->
    <!--<cache-ref namespace=""/> -->
</mapper>
```

> 默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中(XXXMapper.xml)添加一行：`<cache/>`
这个简单语句的效果如下:

- 映射语句文件中的所有 select 语句的结果将会被缓存。
- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。
- 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。
- 缓存不会定时进行刷新（也就是说，没有刷新间隔）。
- 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。
- 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。

```xml

<cache
        eviction="FIFO"
        flushInterval="60000"
        size="512"
        readOnly="true"/>
```

这个更高级的配置创建了一个 FIFO 缓存，每隔 60 秒刷新，最多可以存储结果对象或列表的 512 个引用，而且返回的对象被认为是只读的，因此对它们进行修改可能会在不同线程中的调用者产生冲突。

可用的清除策略有：

LRU – 最近最少使用：移除最长时间不被使用的对象。 FIFO – 先进先出：按对象进入缓存的顺序来移除它们。 SOFT – 软引用：基于垃圾回收器状态和软引用规则移除对象。 WEAK –
弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。 默认的清除策略是 LRU。

flushInterval（刷新间隔）属性可以被设置为任意的正整数，设置的值应该是一个以毫秒为单位的合理时间量。 默认情况是不设置，也就是没有刷新间隔，缓存仅仅会在调用语句时刷新。

size（引用数目）属性可以被设置为任意正整数，要注意欲缓存对象的大小和运行环境中可用的内存资源。默认值是 1024。

readOnly（只读）属性可以被设置为 true 或 false。只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升。而可读写的缓存会（通过序列化）返回缓存对象的拷贝。
速度上会慢一些，但是更安全，因此默认值是 false。

提示：二级缓存是事务性的。这意味着，当 SqlSession 完成并提交时，或是完成并回滚，但没有执行 flushCache=true 的 insert/delete/update 语句时，缓存会获得更新。

### cache-ref

回想一下上一节的内容，对某一命名空间的语句，只会使用该命名空间的缓存进行缓存或刷新。 但你可能会想要在多个命名空间中共享相同的缓存配置和实例。要实现这种需求，你可以使用 cache-ref 元素来引用另一个缓存。

`<cache-ref namespace="com.someone.application.data.SomeMapper"/>`

## 完整示例代码

- 数据库表结构

```sql
/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80015
 Source Host           : localhost:3306
 Source Schema         : gefrm

 Target Server Type    : MySQL
 Target Server Version : 80015
 File Encoding         : 65001

 Date: 17/05/2022 16:02:21
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bsc_dict_info_test
-- ----------------------------
DROP TABLE IF EXISTS `bsc_dict_info_test`;
CREATE TABLE `bsc_dict_info_test`
(
    `dict_id`        varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL DEFAULT ' ' COMMENT '字典标识',
    `dict_name`      varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT ' ' COMMENT '字典名称',
    `dict_sitm_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL DEFAULT ' ' COMMENT '字典子项标识',
    `dict_sitm_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT ' ' COMMENT '字典子项名称',
    `suse_flag`      char(1) CHARACTER SET utf8 COLLATE utf8_general_ci       NOT NULL DEFAULT '0' COMMENT '启用标志',
    `disp_orde`      int(11) NOT NULL DEFAULT 0 COMMENT '展示顺序',
    `memo`           varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bsc_dict_info_test
-- ----------------------------
INSERT INTO `bsc_dict_info_test`
VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test`
VALUES ('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test`
VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test`
VALUES ('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test`
VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '\'0\'');

SET
FOREIGN_KEY_CHECKS = 1;
```

- bean 类

```java
package com.example.mybatis_cache.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Data
@Builder
public class BscDictInfoTestDO implements Serializable {
    private String dictId;
    private String dictName;
    private String dictSitmId;
    private String dictSitmName;
    private String suseFlag;
    private String dispOrde;
    private String memo;

}
```

- mapper

```java
package com.example.mybatis_cache.mapper;

import com.example.mybatis_cache.bean.BscDictInfoTestDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Mapper
public interface BscDictInfoTestMapper {
    /**
     * 查询所有
     *
     * @return
     */
    List<BscDictInfoTestDO> selectAll();
}
```

- service

```java
package com.example.mybatis_cache.service;

/**
 * 测试服务
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
public interface BscDictService {
    void info(String name, int age);
}
```

- impl

```java
package com.example.mybatis_cache.service.impl;


import com.example.mybatis_cache.bean.BscDictInfoTestDO;
import com.example.mybatis_cache.mapper.BscDictInfoTestMapper;
import com.example.mybatis_cache.service.BscDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Slf4j
@Component
public class BscDictServiceImpl implements BscDictService {

    @Resource
    BscDictInfoTestMapper mapper;

    @Override
    public void info(String name, int age) {
        log.warn("name:{}, age:{}", name, age);
        List<BscDictInfoTestDO> list = mapper.selectAll();
        list.forEach(item -> {
            log.error("{}", item);
        });
    }
}
```

- 测试类

```java
package com.example.mybatis_cache.service.impl;

import com.example.mybatis_cache.service.BscDictService;
import com.mysql.cj.xdevapi.SessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/16
 */
@Slf4j
@SpringBootTest
class BscDictServiceImplTest {

    @Autowired
    private BscDictService dictService;

    @Test
    void levelOneCacheTest() {
        dictService.info("a", 1);
        log.warn("======================== 分割线  ======================");
        dictService.info("a", 2);

    }

    @Test
    void testRawLevelOne() {
    }
}
```

## 测试结果

> 执行2次查询，只执行了一次，第二次命中的是缓存

![在这里插入图片描述](https://img-blog.csdnimg.cn/baf862cb6fce41ec9f3fb72f3302eeb5.png)

## 踩坑

- 启动抛异常：
```
Error creating bean with name 'cacheAutoConfigurationValidator' defined in class path resource [org/springframework/boot/autoconfigure/cache/CacheAutoConfiguration.class]: Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: No cache manager could be auto-configured, check your configuration (caching type is 'EHCACHE')
  ```
- 解决 添加依赖 `spring-boot-starter-cache`

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

- 启动抛异常：
```java
Error creating bean with name 'cacheManager' defined in class path resource [org/springframework/boot/autoconfigure/cache/EhCacheCacheConfiguration.class]: Unsatisfied dependency expressed through method 'cacheManager' parameter 1; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'ehCacheCacheManager' defined in class path resource [org/springframework/boot/autoconfigure/cache/EhCacheCacheConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [net.sf.ehcache.CacheManager]: Factory method 'ehCacheCacheManager' threw exception; nested exception is java.lang.IllegalArgumentException: Cache configuration does not exist 'ServletContext resource [/ehcache.xml]'
  ```
- 解决：注意`classpath:`不能少

```properties
spring.cache.ehcache.config=classpath:ehcache.xml
```

## 参考资料

- [MyBatis二级缓存介绍](http://www.mybatis.cn/archives/746.html)
- [mybatis官网中文](https://mybatis.net.cn/sqlmap-xml.html#cache)
- [MyBatis 二级缓存全详解](https://www.cnblogs.com/cxuanBlog/p/11333021.html)
- [Spring Cache和EhCache实现缓存管理](https://blog.csdn.net/pan_junbiao/article/details/107999734)
- [第四篇：SpringBoot中Cache缓存的使用](https://blog.csdn.net/weixin_36279318/article/details/82820880?spm=1001.2101.3001.6650.16&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-16-82820880-blog-75005231.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-16-82820880-blog-75005231.pc_relevant_default&utm_relevant_index=23)