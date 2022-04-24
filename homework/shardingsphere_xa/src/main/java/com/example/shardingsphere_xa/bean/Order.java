package com.example.shardingsphere_xa.bean;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/21
 */
@Data
@Builder
public class Order {
    /// private Long orderId;
    private Long orderId;
    private Integer userId;
    private String status;
}
