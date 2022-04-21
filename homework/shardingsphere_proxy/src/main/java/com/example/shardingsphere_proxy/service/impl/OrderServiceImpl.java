package com.example.shardingsphere_proxy.service.impl;


import com.example.shardingsphere_proxy.bean.Order;
import com.example.shardingsphere_proxy.mapper.OrdersMapper;
import com.example.shardingsphere_proxy.service.OrderService;
import lombok.extern.slf4j.Slf4j;
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
    @Transactional(rollbackFor = Exception.class)
    public void addOrderList(List<Order> data) {
        if (!ObjectUtils.isEmpty(data)) {
            ordersMapper.insertOrderList(data);
        }
        log.warn("==========> {}", dataSource.getClass());

        /// 打开下面注释测试自定义切面和@Transcational事物的优先级如果不进行控制会使事务失效
        // throw new RuntimeException("抛出异常事务回滚！");
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
