package com.example.shardingsphere_work.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.example.shardingsphere_work.bean.Orders;
import com.example.shardingsphere_work.service.OrderShardingService;
import com.example.shardingsphere_work.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/17
 */
@Slf4j
@SpringBootTest
class OrderShardingServiceImplTest {

    @Autowired
    private OrderShardingService orderShardingService;
    @Autowired
    private Snowflake snowflake;

    private static final Integer MILLION = 1000000;
    private static final Integer NUMBER = 10;


    @Test
    void addOrderList() {
        List<Orders> ordersList = createOrdersList(NUMBER);
        long start = System.currentTimeMillis();
        log.warn("开始时间：{}", DateUtils.getDataNumber());
        orderShardingService.addOrderList(ordersList);
        // 百万条订单批量一次性插入耗时：{} ms
        log.warn("10条订单批量一次性插入耗时：{} ms", System.currentTimeMillis() - start);
    }

    @Test
    void getOrders() {
        List<Orders> orders = orderShardingService.getOrders(null);
        if (!ObjectUtils.isEmpty(orders)) {
            orders.forEach(order -> log.warn("[{}]", order));
        }
    }

    private List<Orders> createOrdersList(int num) {
        List<Orders> ordersList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            Orders orders = Orders.builder()
                    .orderCode(snowflake.nextIdStr())
                    .shopCode(snowflake.nextIdStr())
                    .goodsCode(snowflake.nextIdStr())
                    .identifyCard(snowflake.nextIdStr())
                    .phone("19970044187")
                    .price(BigDecimal.TEN)
                    .goodsNum(15L)
                    .remark("批量插入" + i)
                    .isEffective("1")
                    .storeDate(DateUtils.getDataNumber())
                    .build();
            ordersList.add(orders);
        }
        return ordersList;
    }
}