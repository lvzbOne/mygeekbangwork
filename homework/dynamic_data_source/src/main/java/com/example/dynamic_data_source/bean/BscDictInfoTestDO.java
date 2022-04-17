package com.example.dynamic_data_source.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Data
@Builder
public class BscDictInfoTestDO {
    private String dictId;
    private String dictName;
    private String dictSitmId;
    private String dictSitmName;
    private String suseFlag;
    private String dispOrde;
    private String memo;

}
