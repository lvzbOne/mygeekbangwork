package com.example.redispubsub.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Random;

/**
 * 发布者
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/5/21
 */
public class PublishOrder {
    public PublishOrder(JedisPool jedisPool, String channelName) {
        try (final Jedis jedis = jedisPool.getResource()) {
            int num = 10;
            final Random random = new Random();
            for (int i = 0; i < num; i++) {
                int sleepTime = random.nextInt(num) + 1;
                Thread.sleep(sleepTime);
                jedis.publish(channelName, "order processing: " + sleepTime + " millis");
            }
            // 用于中止
            jedis.publish(channelName, "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
