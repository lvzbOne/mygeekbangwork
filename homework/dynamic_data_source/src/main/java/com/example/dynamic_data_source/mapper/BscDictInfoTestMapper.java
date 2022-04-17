package com.example.dynamic_data_source.mapper;

import com.example.dynamic_data_source.bean.BscDictInfoTestDO;
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
