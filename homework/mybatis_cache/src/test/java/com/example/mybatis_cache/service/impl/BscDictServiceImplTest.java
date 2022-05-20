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