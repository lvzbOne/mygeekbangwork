package com.example.redisscene.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局ID生成
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/5/22
 */
public enum GenerateId {
    /**
     * 单例生成ID对象
     */
    INSTANCE;

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";
    private JedisPool jedisPool = new JedisPool();

    private GenerateId() {
    }

    public static GenerateId getInstance() {
        return INSTANCE;
    }

    private String generateOrderId() {
        LocalDateTime now = LocalDateTime.now();
        String orderIdPrefix = this.getOrderIdPrefix(now);
        // %1$ 表示第一个参数，06d表示 6位数不足补0
        return orderIdPrefix + String.format("%1$06d", generate(orderIdPrefix, 5));
    }

    private String getOrderIdPrefix(LocalDateTime now) {
        return now.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    private Long generate(String key, long timeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            // 每次加一操作
            Long id = jedis.incr(key);
            if (timeout > 0) {
                // 设置key超时避免占用内存
                jedis.expire(key, timeout);
            }
            /// System.out.println("id: " + id);
            return id;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenerateId.getInstance().generateOrderId());
        }

        // 第一个参数占6位，不足补上0
        System.out.println(String.format("%1$06d", 1234));
        // 第二个参数占6位，不足补上0
        System.out.println(String.format("%2$06d",1234,5678));
    }
}
