# Spring boot集成redis做方法级别的缓存初体验

## 前言

> spring cache 集成 redis 做方法层面的缓存示例，查询时缓存key和结果到redis，更新时缓存，和缓存清除。示例总结：
> - 做缓存的要求是热数据的读远大于写的数据，才适合用缓存来优化
> - spring cache提供的三个注解@Cacheable,@CachePut,@CacheEvict(用来清除缓存)
> - redis的key资源很宝贵，spring cache提供的这三个注解针仅仅对的是对单个key的缓存操作，无法做到对key是redis set这种集合的类型操作， 这种需求可以通过自定义aop切面完成,本示例没有给出，后面有时间可以继续完善下。

## 环境

- windows 系统
- 基于 spring boot

## 下载安装redis(windows环境)连接

- [下载地址 找zip包下载解压就能用](https://github.com/tporadowski/redis/releases)
- 下载后解压进入可执行命令的目录里打开cmd 或 powershell窗口
- 执行命令启动redis服务： `redis-server.exe redis.windows.conf` ; powershell窗口是`.\redis-server.exe redis.windows.conf`
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/8033fd07f29e4abd999d4f5643965d66.png)

- 另外开启一个客户端窗口输入连接命令 cmd窗口是：`redis-cli.exe -h 127.0.0.1 -p 6379`； powershell窗口是：`.\redis-cli.exe -h 127.0.0.1 -p 6379`
  连接服务端。
- 也可以用第三方图像化工具来连接redis服务 推荐一款：[Redis Assistant](http://www.redisant.cn/)

## 一、添加依赖
> 加入下面的依赖后，要在启动类上添加 `@EnableCaching`注解开启 spring 缓存
```xml
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
        <!-- Ehcache缓存管理器 -->
        <dependency>
            <groupId>net.sf.ehcache</groupId>
            <artifactId>ehcache</artifactId>
        </dependency>
        <!-- spring boot cache-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```
## 二、示例数据库表的sql
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bsc_dict_info_test
-- ----------------------------
-- DROP TABLE IF EXISTS `bsc_dict_info_test`;
CREATE TABLE IF NOT EXISTS `bsc_dict_info_test`  (
  `dict_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典标识',
  `dict_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典名称',
  `dict_sitm_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典子项标识',
  `dict_sitm_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '字典子项名称',
  `suse_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '启用标志',
  `disp_orde` int(11) NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `memo` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bsc_dict_info_test
-- ----------------------------
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '更新操作！');
INSERT INTO `bsc_dict_info_test` VALUES ('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '更新操作！');
INSERT INTO `bsc_dict_info_test` VALUES ('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '1');
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, '更新操作！');
INSERT INTO `bsc_dict_info_test` VALUES ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '99911', '无关联关系', '0', 190, '更新操作！');

SET FOREIGN_KEY_CHECKS = 1;
```
## 三、`properties`文件
> 这里我既用了 `application.yaml`,又使用了`application.properties`,也可以合并。参考:[ymal中引入其他的yml或properties](https://blog.csdn.net/qq_31584291/article/details/82871232?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-5-82871232-blog-114254143.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-5-82871232-blog-114254143.pc_relevant_default&utm_relevant_index=10)
- application.properties
```properties
spring.datasource.type=com.zaxxer.hikari.HikariDataSource

#Spring jdbc 数据源配置 需要mysql-connector-java驱动依赖
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/gefrm?useUnicode=true&characterEncoding=utf-8&useSSL=false&useInformationSchema=true&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false
spring.datasource.username=root
spring.datasource.password=123456

# 缓存方式一: ehcache.xml
#配置文件中配置目标缓存管理器，支持 Ehcache、Generic、Redis、Jcache
#spring.cache.type=ehcache
#spring.cache.ehcache.config=classpath:ehcache.xml

#mybatis
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.example.mybatis_cache.bean
mybatis.configuration.map-underscore-to-camel-case=true
#mybatis缓存
mybatis.configuration.cache-enabled=true
#mybatisSQL执行打印
logging.level.com.example.mybatis_cache.mapper=debug
```
- application.yaml
```yaml
#spring.redis.port默认6379; spring.redis.host默认127.0.0.1
spring:
  cache:
    type: redis
  redis:
    port: 6379
    host: 127.0.0.1
```
## 四、`ehcache.xml`文件
> 配置详解说明参考博客:[ehcache.xml简介](https://www.cnblogs.com/crazylqy/p/4238148.html)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">

    <!-- 磁盘缓存位置 -->
    <diskStore path="java.io.tmpdir/ehcache"/>
<!--
    <diskStore path='/cn/mybatis/cache'/>
-->

    <!-- 默认缓存 -->
    <defaultCache
            maxEntriesLocalHeap="10000"
            eternal="false"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            maxEntriesLocalDisk="10000000"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU">
        <persistence strategy="localTempSwap"/>

    </defaultCache>

    <!--自定义缓存：MyCache -->
    <cache name="MyCache"
           maxElementsInMemory="1000"
           eternal="false"
           timeToIdleSeconds="5"
           timeToLiveSeconds="5"
           overflowToDisk="false"
           memoryStoreEvictionPolicy="LRU"/>
</ehcache>
```
## 五、 `CacheConfig`配置类
> 自定义默认key生成策略：类签名+方法名+参数类型，自定义CacheManage的序列化采用`GenericJackson2JsonRedisSerializer`
```java
package com.example.mybatis_cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * @author 起凤
 */
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Resource
    private RedisConnectionFactory factory;

    /**
     * 自定义生成redis-key,没有指定key时自动生效
     *
     * @return
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName()).append(".");
            sb.append(method.getName()).append(".");
            for (Object obj : objects) {
                sb.append(obj.toString()).append(".");
            }
            //System.out.println("keyGenerator=" + sb.toString());
            return sb.toString();
        };
    }

    /**
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 序列化器
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setKeySerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return new SimpleCacheErrorHandler();
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration cacheConfiguration =
                defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
}
```
## 六、示例代码
### （一）bean 实体
- BscDictInfoTestDO
```java
package com.example.mybatis_cache.bean;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Getter
@Setter
@Accessors(chain = true)
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
### （二）mapper 和 mapper.xml
- BscDictInfoTestMapper
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

    /**
     * 根据dictId查询所有
     *
     * @return List<BscDictInfoTestDO>
     */
    List<BscDictInfoTestDO> selectByDictId(String dictId);

    /**
     * 更新数据信息
     *
     * @param data
     * @return
     */
    int updateDataInfo(BscDictInfoTestDO data);

    /**
     * 根据dictId删除字典记录
     *
     * @param data
     * @return
     */
    int deleteByDictIds(List<String> data);

}
```
- BscDictInfoTestMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mybatis_cache.mapper.BscDictInfoTestMapper">

    <!-- 这个cache 是关键 mybatis自带的缓存 -->
    <!--
        <cache eviction="LRU" flushInterval="100000" readOnly="true" size="1024"/>
    -->

    <!--用mybatis 整合第三方框架 ehcache 做二级缓存用法-->
    <!--    <cache type='org.mybatis.caches.ehcache.EhcacheCache'>
            <property name='timeToIdleSeconds' value='3600'/>

            <property name='timeToLiveSeconds' value='3600'/>

            &lt;!&ndash; 同ehcache参数maxElementsInMemory&ndash;&gt;

            <property name='maxEntriesLocalHeap' value='1000'/>

            &lt;!&ndash; 同ehcache参数maxElementsOnDisk &ndash;&gt;

            <property name='maxEntriesLocalDisk' value='100000'/>

            <property name='memoryStoreEvictionPolicy' value='LRU'/>
        </cache>-->

    <!-- 多个命名空间缓存共享 级联标签 cache-ref 指定namespace即可 -->
    <!--<cache-ref namespace=""/> -->

    <sql id="BseColumn">
        dict_id
        ,
        dict_name,
        dict_sitm_id,
        dict_sitm_name,
        suse_flag,
        disp_orde,
        memo
    </sql>

    <!--可以通过设置useCache来规定这个sql是否开启缓存，ture是开启，false是关闭,刷新缓存：flushCache="true"  useCache="true"-->
    <select id="selectAll" resultType="com.example.mybatis_cache.bean.BscDictInfoTestDO">
        select *
        from bsc_dict_info_test
    </select>

    <select id="selectByDictId" resultType="com.example.mybatis_cache.bean.BscDictInfoTestDO">
        select
        <include refid="BseColumn"/>
        from bsc_dict_info_test
        where dict_id = #{dictId}
    </select>

    <update id="updateDataInfo" parameterType="com.example.mybatis_cache.bean.BscDictInfoTestDO">
        update bsc_dict_info_test
        set memo = #{memo}
        where dict_id = #{dictId}
    </update>


    <delete id="deleteByDictIds">
        delete
        from bsc_dict_info_test
        <where>
            dictId in
            <if test="data!=null and data.size>0">
                <foreach collection="data" item="item" separator="," open="(" close=")">
                    #{item}
                </foreach>
            </if>
        </where>
    </delete>
</mapper>
```
### （三）service 和 iml
- BscDictService
```java
package com.example.mybatis_cache.service;

import com.example.mybatis_cache.bean.BscDictInfoTestDO;

import java.util.List;

/**
 * 测试服务
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
public interface BscDictService {
    void info(String name, int age);

    /**
     * 获取 Dic 信息列表
     *
     * @param dictId 字典号
     * @return
     */
    List<BscDictInfoTestDO> getDicInfoList(String dictId);

    /**
     * 更新字典项
     *
     * @param dictInfo
     * @return
     */
    BscDictInfoTestDO updateDictInfo(BscDictInfoTestDO dictInfo);

    /**
     * 删除所有字典数据
     *
     * @return
     */
    int deleteAll();

    /**
     * 批量添加字典项
     *
     * @param data
     * @return
     */
    int insertDictInfo(List<BscDictInfoTestDO> data);

    /**
     * 删除 字典数据
     *
     * @param dictList
     * @return
     */
    int deleteDictInfo(List<String> dictList);
    
}
```
- BscDictServiceImpl
```java
package com.example.mybatis_cache.service.impl;


import com.example.mybatis_cache.bean.BscDictInfoTestDO;
import com.example.mybatis_cache.mapper.BscDictInfoTestMapper;
import com.example.mybatis_cache.service.BscDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
// 注意：必须对应配置文件ehcache.xml中cache节点的name属性值,
// 这里开启@CacheConfig后，下面的Cacheable的value默认就使用这里的cacheNames指定的值
@CacheConfig(cacheNames = "otherCache")
@Slf4j
@Service
public class BscDictServiceImpl implements BscDictService {

    /**
     * 注意：必须对应配置文件ehcache.xml中cache节点的name属性值
     */
    private static final String CACHE_NAME = "MyCache";
    private static final String OTHER_CACHE = "otherCache";


    @Resource
    private BscDictInfoTestMapper mapper;

    @Override
    public void info(String name, int age) {
        log.warn("name:{}, age:{}", name, age);
        List<BscDictInfoTestDO> list = mapper.selectAll();
        list.forEach(item -> {
            log.error("{}", item);
        });
    }

    @Cacheable(value = CACHE_NAME, key = "#dictId")
    @Override
    public List<BscDictInfoTestDO> getDicInfoList(String dictId) {
        log.warn(">>>>>>>>> 执行查询字典dictId:{} <<<<<<<<<", dictId);
        return mapper.selectByDictId(dictId);
    }

    @CachePut(value = CACHE_NAME, key = "#dictInfo.dictId")
//    @CachePut
    @Override
    public BscDictInfoTestDO updateDictInfo(BscDictInfoTestDO dictInfo) {
        mapper.updateDataInfo(dictInfo);
        return dictInfo;
    }

    @CacheEvict(value = {CACHE_NAME, OTHER_CACHE}, allEntries = true)
    @Override
    public int deleteAll() {
        log.info("删除所有信息，清空所有缓存");
        return 0;
    }

    @Override
    public int insertDictInfo(List<BscDictInfoTestDO> data) {
        return 0;
    }

    @Override
    public int deleteDictInfo(List<String> dictList) {
        log.info(">>>>>>>>> 执行删除字典dictId:{} <<<<<<<<<", dictList.stream().collect(Collectors.joining(",")));
        return 0;
        // return mapper.deleteByDictIds(dictList);
    }

}
```
### （四）Test代码
```java
package com.example.mybatis_cache.service.impl;

import com.example.mybatis_cache.bean.BscDictInfoTestDO;
import com.example.mybatis_cache.service.BscDictService;
import com.example.mybatis_cache.support.ApplicationContextSupport;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

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

    /**
     * 原生开启mybatis二级缓存，不经过spring
     */
    @Test
    void levelOneCacheTest() {
        dictService.info("a", 1);
        log.warn("======================== 分割线  ======================");
        dictService.info("a", 2);
    }

    /**
     * redis 方法查询缓存
     */
    @Test
    void levelTwoCacheTest() {
        dictService.getDicInfoList("bbbbbtest");
        dictService.getDicInfoList("bbbbbtest");
        dictService.getDicInfoList("aaaaatest");
    }

    /**
     * redis 方法更新缓存
     */
    @Test
    void levelTwoCacheUpdateTest() {
        BscDictInfoTestDO dictInfo = dictService.getDicInfoList("bbbbbtest").stream().limit(1).findFirst().orElse(new BscDictInfoTestDO());
        log.warn("======================== 分割线  ======================");
        dictService.updateDictInfo(dictInfo.setMemo("更新操作！"));
    }

    /**
     * spring 缓存删除
     */
    @Test
    void cacheEviTest() {
        dictService.deleteAll();
    }


    @Test
    void testRawLevelOne() {
        System.out.println("====");
        ///dictService.getDicInfoList("bbbbbtest");
        ApplicationContext context = ApplicationContextSupport.context;
        ///dictService.getDicInfoList("bbbbbtest");
        System.out.println(context);
    }

    @Test
    void cacheTest() {
        final String path = "D:\\ideaProjects\\jikeshijian\\mygeekbangwork\\homework\\mybatis_cache\\src\\main\\resources\\ehcache.xml";
        // 1. 创建缓存管理器
        CacheManager cacheManager = CacheManager.create(path);

        // 2. 获取缓存对象 MyCache HelloWorldCache
        Cache cache = cacheManager.getCache("MyCache");

        // 3. 创建元素
        Element element = new Element("key1", "value1");

        // 4. 将元素添加到缓存
        cache.put(element);

        // 5. 获取缓存
        Element value = cache.get("key1");
        System.out.println("value: " + value);
        System.out.println(value.getObjectValue());

        // 6. 删除元素
        cache.remove("key1");

        // 7. 刷新缓存
        cache.flush();

        // 8. 关闭缓存管理器
        cacheManager.shutdown();
    }
}
```


## 七、结果演示
- 使用`@Cacheable`修饰`dictService.getDicInfoList("bbbbbtest")`方法执行2次
  ，第一次执行会缓存到redis，第二次执行时缓存该入参key已存在就不会再执行方法体里的内容,测试后查看redis缓存，都已缓存
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/76a0861b6c514f23a44ac314bb417bcf.png)
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/580afe6bc5c648af9135f4e93ccf492e.png)

- 使用`@CachePut`修饰。执行方法，并更新redis的相应key缓存。调用 `getDicInfoList`会缓存一个key`MyCache::bbbbtest`,
  调用`updateDictInfo` 会执行更新缓存，其返回值是单个对象。结果入下图。这也印证了那句话，redis的key资源很宝贵，实际上这里就是key冲突了,2个方法key一直会相互影响需设计的适合要极其注意。
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/df2ef74ec4314f6681f6c3b31ade41c5.png)

- 未指定key会使用自定义的生成key策略缓存。方法`updateDictInfo` 未指定key和value会使用默认的策略，详情看上面的impl代码
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/7844e99b4e7145b6a267d9b3fe4e796e.png)
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/5ae03ce4a0ea4625ab2f1d0d208157f0.png)

- 使用`@CachePut`修饰清除所有缓存。执行测试代码后，redis的cache缓存清空
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/da61ab1589ec4a85939ed2535dbdbd41.png)![在这里插入图片描述](https://img-blog.csdnimg.cn/e32adbeb32f34e0ab9d574c0322cbded.png)
## 报错问题解决

- 启动异常：
```
No cache could be resolved for 'Builder[public com.example.bean.Employeecom.example.controller.EmployeeController.getEmpById(java.lang.Integer)]caches=[] | key='' |keyGenerator='' | cacheManager='' | cacheResolver='' | condition='' | unless='' | sync='false''using resolver'org.springframework.cache.interceptor.SimpleCacheResolver@540d3d5d'. At least one cache should be provided per cache operation.
  ```

- 解决： @Cacheable 注解的value要指定ehcache.xml的cache

- 启动异常：
```
Could not read JSON: Cannot construct instance of `com.example.mybatis_cache.bean.BscDictInfoTestDO` (no
  Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based
  Creator)
  ```
- 解决： 构造函数被重写带多个入参是，实体类的无参构造函数一定要保留即可解决。

## 参考资料

- [Redis 安装](https://www.runoob.com/redis/redis-install.html)
- [RedisAssistant：一款Redis可视化管理工具](https://blog.csdn.net/qq_32779119/article/details/121043208?spm=1001.2014.3001.5502)
- [spring cache 学习 —— @Cacheable 使用详解](https://www.cnblogs.com/coding-one/p/12401630.html)
- [Spring Boot2.X 自定义Redis的cacheManager，保存Json格式到Redis](https://blog.csdn.net/caojidasabi/article/details/83059642?spm=1001.2101.3001.6650.19&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-19-83059642-blog-113842645.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-19-83059642-blog-113842645.pc_relevant_default&utm_relevant_index=20)
- [SpringBoot中使用Redis实现缓存](https://www.jianshu.com/p/345b188c7ef8)