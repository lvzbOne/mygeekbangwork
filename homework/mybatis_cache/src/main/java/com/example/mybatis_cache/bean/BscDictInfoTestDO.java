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
