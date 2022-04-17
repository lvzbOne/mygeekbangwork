package com.example.dynamic_data_source.service.impl;

import com.example.dynamic_data_source.annotation.S1;
import com.example.dynamic_data_source.bean.BscDictInfoTestDO;
import com.example.dynamic_data_source.mapper.BscDictInfoTestMapper;
import com.example.dynamic_data_source.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TestServiceImpl implements TestService {

    @Resource
    BscDictInfoTestMapper mapper;

    @S1
    @Override
    public void info(String name, int age) {
        log.warn("name:{}, age:{}", name, age);
        List<BscDictInfoTestDO> list = mapper.selectAll();
        list.forEach(item->{
            log.error("{}", item);
        });
    }
}
