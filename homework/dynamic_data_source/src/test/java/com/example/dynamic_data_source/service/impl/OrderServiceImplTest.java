package com.example.dynamic_data_source.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.example.dynamic_data_source.bean.Orders;
import com.example.dynamic_data_source.service.OrderService;
import com.example.dynamic_data_source.util.DateUtils;
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
 * @date 2022/4/16
 */
@Slf4j
@SpringBootTest
class OrderServiceImplTest {

    private static final Integer NUMBER = 10;
    private static final Integer MILLION = 100_0000;
    private static final Integer TEN_MILLION = 1000_0000;


    @Autowired
    private OrderService orderService;
    @Autowired
    private Snowflake snowflake;

    @Test
    void addOrderList() {
        List<Orders> ordersList = createOrdersList(NUMBER);
        try {
            long start = System.currentTimeMillis();
            orderService.addOrderList(ordersList);
            log.warn("10条订单批量插入耗时：{} ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("error:[{}]", e.getMessage());
        }
    }

    @Test
    void getOrders() {
        List<Orders> orders = orderService.getOrders(null);
        if (!ObjectUtils.isEmpty(orders)) {
            orders.forEach(order -> log.error("[{}]", order));
        }
    }

    @Test
    void millionMultiInsertPerformanceTest() {
        List<Orders> ordersList = createOrdersList(MILLION);
        long start = System.currentTimeMillis();
        orderService.addOrderList(ordersList);
        // 百万条订单批量一次性插入耗时：{} ms
        log.warn("百万条订单批量一次性插入耗时：{} ms", System.currentTimeMillis() - start);
    }

    @Test
    void insertOneByOnePerformanceTest() {
        List<Orders> ordersList = createOrdersList(MILLION);
        long start = System.currentTimeMillis();
        orderService.addOrderList(ordersList);
        log.warn("百万条订单一条条插入耗时：{} ms", System.currentTimeMillis() - start);
    }

    @Test
    void testAssemble() {
        long start = System.currentTimeMillis();
        List<Orders> ordersList = createOrdersList(MILLION);
        log.warn("百万条订单装配耗时：{} ms", System.currentTimeMillis() - start);
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