package com.example.redispubsub.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 * 订阅
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/5/21
 */
public class SubscribeOrder {
    public SubscribeOrder(final JedisPool jedisPool, final String channelName) {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                // jedis.subscribe 是阻塞的
                jedis.subscribe(createSubscriber(), channelName);
            }
        }, "").start();
    }

    private JedisPubSub createSubscriber() {
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (message.isEmpty()) {
                    System.out.println("SubPub end");
                    System.exit(0);
                }
                System.out.printf("Receiver message from %s :: %s\n", channel, message);
            }
        };
    }
}
