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
