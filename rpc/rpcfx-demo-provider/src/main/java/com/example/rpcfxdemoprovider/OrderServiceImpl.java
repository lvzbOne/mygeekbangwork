package com.example.rpcfxdemoprovider;


import com.example.rpcfxdemoapi.api.Order;
import com.example.rpcfxdemoapi.api.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
