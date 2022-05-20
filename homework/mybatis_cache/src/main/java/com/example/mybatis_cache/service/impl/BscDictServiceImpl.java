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
