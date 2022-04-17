package com.example.shardingsphere_work.service.impl;

import cn.hutool.core.lang.Snowflake;

import com.example.shardingsphere_work.bean.Orders;
import com.example.shardingsphere_work.mapper.OrdersMapper;
import com.example.shardingsphere_work.service.OrderShardingService;
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
public class OrderShardingServiceImpl implements OrderShardingService {

    @Resource
    private OrdersMapper ordersMapper;

    @Autowired
    private Snowflake snowflake;
    @Autowired
    private DataSource dataSource;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrderList(List<Orders> data) {
        if (!ObjectUtils.isEmpty(data)) {
            ordersMapper.insertOrderList(data);
        }
        log.warn("==========> {}", dataSource.getClass());
        /// 打开下面注释测试自定义切面和@Transcational事物的优先级如果不进行控制会使事务失效
        // throw new RuntimeException("抛出异常事务回滚！");
    }

    @Override
    public List<Orders> getOrders(Orders req) {
        List<Orders> orders = ordersMapper.selectAllOrders(req);
        log.warn("==========> {}", dataSource.getClass());
        return orders;
    }
}
