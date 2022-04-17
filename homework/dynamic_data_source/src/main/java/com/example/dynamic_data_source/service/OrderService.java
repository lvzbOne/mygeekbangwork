package com.example.dynamic_data_source.service;

import com.example.dynamic_data_source.bean.Orders;

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
    void addOrderList(List<Orders> data);

    /**
     * 跟具条件查找所有订单
     *
     * @param req
     * @return
     */
    List<Orders> getOrders(Orders req);
}
