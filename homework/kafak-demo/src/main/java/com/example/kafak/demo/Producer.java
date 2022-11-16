package com.example.kafak.demo;


/**
 * 生产者
 *
 * @author lvzb3
 */
public interface Producer {

    /**
     * 发送订单
     *
     * @param order
     */
    void send(Order order);

    /**
     * 关闭
     */
    void close();

}
