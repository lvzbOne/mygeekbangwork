package com.example.dynamic_data_source.mapper;

import com.example.dynamic_data_source.bean.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
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
