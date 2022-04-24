package com.example.shardingsphere_xa.service;


import com.example.shardingsphere_xa.bean.Order;

import java.util.List;

/**
 * 订单服务
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
public interface OrderService {
    /**
     * 批量插入
     *
     * @param data
     * @return
     */
    void addOrderList(List<Order> data);

    /**
     * 批量插入-XA
     *
     * @param data
     * @return
     */
    void addOrderListXA(List<Order> data, List<Order> data1);

    /**
     * 跟具条件查找所有订单
     *
     * @param req
     * @return
     */
    List<Order> getOrders(Order req);

    /**
     * 更具条件删除订单
     *
     * @param req
     */
    void deleteOrders(Order req);
}
