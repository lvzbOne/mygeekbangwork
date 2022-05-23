package com.example.redispubsub.pubsub;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/21
 */
class SubscribeOrderTest {
    @Test
    void pubSubTest() {
        final JedisPool jedisPool = new JedisPool();
        String channel = "pubSub";
        SubscribeOrder subscribeOrder = new SubscribeOrder(jedisPool, channel);
        PublishOrder publishOrder = new PublishOrder(jedisPool, channel);
    }
}