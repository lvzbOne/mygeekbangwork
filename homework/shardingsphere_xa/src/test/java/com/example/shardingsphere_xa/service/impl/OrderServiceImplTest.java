package com.example.shardingsphere_xa.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.example.shardingsphere_xa.bean.Order;
import com.example.shardingsphere_xa.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/23
 */
@Slf4j
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Resource
    private Snowflake snowflake;
    @Autowired
    private DataSource dataSource;

    private static final Integer NUMBER = 16;
    private static final Integer TEN = 10;
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
        deleteOrders();
        List<Order> ordersList = createOrdersList(NUMBER);
        orderService.addOrderList(ordersList);
        getOrders();
    }

    /**
     * X-A 事务测试类
     */
    @Test
    void addOrderListXA() {
        deleteOrders();
        List<Order> tenOrders = createOrdersList(TEN);
        List<Order> ordersList = createOrdersList1(TEN);
        try {
            orderService.addOrderListXA(tenOrders, ordersList);
        } catch (Exception e) {
            log.error("error:[{}]", e.getMessage());
        }
        getOrders();
    }

    @Test
    void getOrders() {
        List<Order> orders = orderService.getOrders(null);
        if (!ObjectUtils.isEmpty(orders)) {
            List<Order> collect = orders.stream().sorted(Comparator.comparing(Order::getOrderId)).collect(Collectors.toList());
            for (Order order : collect) {
                log.warn("=========> {}", order.toString());
            }
        }
    }

    @Test
    void deleteOrders() {
        orderService.deleteOrders(null);
    }

    private List<Order> createOrdersList(int num) {
        List<Order> ordersList = new ArrayList<>(num);
        // 左闭右开[1,10)
        Random random = new Random();
        for (int i = 1; i <= num; i++) {
            Order orders = Order.builder()
                    .userId(i)
                    .orderId(Long.valueOf(i))
                    .status(POEM[random.nextInt(POEM.length)])
                    .build();
            ordersList.add(orders);
        }
        return ordersList;
    }

    private List<Order> createOrdersList1(int num) {
        List<Order> ordersList = new ArrayList<>(num);
        // 左闭右开[1,10)
        Random random = new Random();
        for (int i = 1; i <= num; i++) {
            Order orders = Order.builder()
                    .userId(i)
                    .orderId(Long.valueOf(i + 6))
                    .status(POEM[random.nextInt(POEM.length)])
                    .build();
            ordersList.add(orders);
        }
        return ordersList;
    }

    @Test
    void intToLong() {
        int i = 1;
        Integer it = 1;
        // long 接受 int 值是可以的，但是 Long 无法接收 int 需要使用 Long.valueOf(long l)
        // long l = i;
        Long l = Long.valueOf(i);
        System.out.println(l);
    }

}