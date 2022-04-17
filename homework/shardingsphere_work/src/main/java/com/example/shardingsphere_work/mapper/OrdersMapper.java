package com.example.shardingsphere_work.mapper;

import com.example.shardingsphere_work.bean.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单Mapper
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
@Mapper
public interface OrdersMapper {
    /**
     * 批量插入
     *
     * @param data
     * @return
     */
    int insertOrderList(List<Orders> data);

    /**
     * 跟具条件查找所有订单
     *
     * @param req
     * @return
     */
    List<Orders> selectAllOrders(Orders req);
}
