package com.example.kafak.demo;

/**
 * 消费者
 *
 * @author lvzb3
 */
public interface Consumer {

    /**
     * 消费订单
     */
    void consumeOrder();

    /**
     * 关闭
     */
    void close();

}
