package com.example.redislock.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/21
 */
public enum EnumRedisLock {
    /**
     * 分布式锁单例实例
     */
    INSTANCE;

    EnumRedisLock() {
    }

    private JedisPool jedisPool = new JedisPool();

    /**
     * 进行加锁
     *
     * @param lockValue lock value
     * @param seconds   expire time
     * @return get lock
     */
    public boolean lock(String lockValue, long seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            SetParams params = new SetParams().nx().ex(seconds);
            return "OK".equals(jedis.set(lockValue, lockValue, params));
//            return "OK".equals(jedis.set(lockValue, lockValue, "NX", "EX", seconds));
        }
    }

    /**
     * 释放锁
     *
     * @param lock lock value
     * @return release lock
     */
    public boolean release(String lock) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " + "return redis.call('del',KEYS[1]) else return 0 end";
        try (Jedis jedis = jedisPool.getResource()) {
            // 执行删除成功会返回1 失败会返回 0 。后面2个参数是代表 keys,args
            return jedis.eval(luaScript, Collections.singletonList(lock), Collections.singletonList(lock)).equals(1L);
        }
    }

}
