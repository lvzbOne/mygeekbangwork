package com.example.mybatis_cache.controller;

import com.example.mybatis_cache.bean.BscDictInfoTestDO;
import com.example.mybatis_cache.service.BscDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/18
 */
@RequestMapping("/bsc/")
@Controller
public class BscDictInfoTestController {
    @Autowired
    private BscDictService dictService;

    @ResponseBody
    @RequestMapping("/getDictInfo")
    public List<BscDictInfoTestDO> getDictInfo(String dictId) {
        List<BscDictInfoTestDO> dicInfoList = dictService.getDicInfoList(dictId);
        return dicInfoList;
    }

}
