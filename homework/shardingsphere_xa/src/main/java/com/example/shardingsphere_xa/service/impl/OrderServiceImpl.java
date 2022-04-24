package com.example.shardingsphere_xa.service.impl;


import com.example.shardingsphere_xa.bean.Order;
import com.example.shardingsphere_xa.mapper.OrdersMapper;
import com.example.shardingsphere_xa.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * 订单服务实现类
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrdersMapper ordersMapper;
    @Autowired
    private DataSource dataSource;


    @Override
    @ShardingTransactionType(TransactionType.XA)
    @Transactional(rollbackFor = Exception.class)
    public void addOrderList(List<Order> data) {
        if (!ObjectUtils.isEmpty(data)) {
            ordersMapper.insertOrderList(data);
        }
        log.warn("==========> {}", dataSource.getClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.XA)
    public void addOrderListXA(List<Order> data, List<Order> data1) {
        if (!ObjectUtils.isEmpty(data) && !ObjectUtils.isEmpty(data1)) {
            ordersMapper.insertOrderListContainOrderId(data);
            ordersMapper.insertOrderListContainOrderId(data1);
        }
    }

    @Override
    public List<Order> getOrders(Order req) {
        List<Order> orders = ordersMapper.selectAllOrders(req);
        log.warn("==========> {}", dataSource.getClass());
        return orders;
    }

    @Override
    public void deleteOrders(Order req) {
        ordersMapper.deleteOrders(req);
    }
}
