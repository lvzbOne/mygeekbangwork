package com.example.shardingsphere_xa.mapper;

import com.example.shardingsphere_xa.bean.Order;
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
    int insertOrderList(List<Order> data);

    /**
     * 批量插入 含有OrderId
     *
     * @param data
     * @return
     */
    int insertOrderListContainOrderId(List<Order> data);

    /**
     * 跟具条件查找所有订单
     *
     * @param req
     * @return
     */
    List<Order> selectAllOrders(Order req);

    /**
     * 按条件删除订单
     *
     * @param req
     * @return
     */
    int deleteOrders(Order req);
}
