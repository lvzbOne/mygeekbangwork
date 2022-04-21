package com.example.shardingsphere_proxy.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.example.shardingsphere_proxy.bean.Order;
import com.example.shardingsphere_proxy.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/21
 */
@Slf4j
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Resource
    private Snowflake snowflake;

    private static final Integer NUMBER = 16;
    private static final String[] POEM = {"锦瑟无端五十弦", "一弦一柱思华年",
            "庄生晓梦迷蝴蝶", "望帝春心托杜鹃",
            "沧海月明珠有泪", "蓝田日暖玉生烟",
            "此情可待成追忆", "只是当时已惘然"};

    /**
     * 单元测试里添加了 @Transactional 并不会真正提交数据，大赞！
     */
    @Test
    @Transactional
    void addOrderList() {
        List<Order> ordersList = createOrdersList(NUMBER);
        for (Order order : ordersList) {
            log.warn("=======> {}", order.getStatus());
        }
        orderService.addOrderList(ordersList);
    }

    @Test
    void getOrders() {
        List<Order> orders = orderService.getOrders(null);
        if (!ObjectUtils.isEmpty(orders)) {
            for (Order order : orders) {
                log.warn("=========> {}", order.toString());
            }
        }
    }

    @Test
    @Transactional
    void deleteOrders() {
        orderService.deleteOrders(Order.builder().userId(1).build());
    }

    private List<Order> createOrdersList(int num) {
        List<Order> ordersList = new ArrayList<>(num);
        // 左闭右开[1,10)
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            Order orders = Order.builder()
                    .userId(i)
                    .status(POEM[random.nextInt(POEM.length)])
                    .build();
            ordersList.add(orders);
        }
        return ordersList;
    }
}