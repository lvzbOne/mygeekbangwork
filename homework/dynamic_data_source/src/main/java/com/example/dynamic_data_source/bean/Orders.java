package com.example.dynamic_data_source.bean;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
@Data
@Builder
public class Orders {

    private BigInteger id;
    private String orderCode;
    private String shopCode;
    private String goodsCode;
    private String identifyCard;
    private String phone;
    private BigDecimal price;
    private Long goodsNum;
    private String remark;
    private String isEffective;
    private Integer storeDate;
    private String gmtCreate;
    private String gmtModified;

}
