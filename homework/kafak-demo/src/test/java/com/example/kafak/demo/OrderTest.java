package com.example.kafak.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: lvzb31988
 * @date: 2022/11/16 21:22
 */
@Slf4j
//@SpringBootTest
class OrderTest {

    @Test
    void consumer() {
        ConsumerImpl consumer = new ConsumerImpl();
        consumer.consumeOrder();
    }

    @Test
    void producer() {
        ProducerImpl producer = new ProducerImpl();
        for (int i = 0; i < 1000; i++) {
            producer.send(new Order(1000L + i, System.currentTimeMillis(), "USD2CNY", 6.5d));
            producer.send(new Order(2000L + i, System.currentTimeMillis(), "USD2CNY", 6.51d));
        }
    }
}