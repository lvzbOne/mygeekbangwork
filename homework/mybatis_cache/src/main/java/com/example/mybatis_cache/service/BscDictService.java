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
