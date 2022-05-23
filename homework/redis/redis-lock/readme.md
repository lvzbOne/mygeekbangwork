## 基于 Redis 封装分布式数据操作

> 在 Java 中实现一个简单的分布式锁
> 在 Java 中实现一个分布式计数器，模拟减库存

> 开始我自己看到这个题目时是懵的，无从下手，网上搜了下，才知道要做个什么东西。[如何基于Redis实现分布式锁，详细教程拿走不送~](https://blog.csdn.net/smilehappiness/article/details/107592896)
> 打开了思路。
> 背景：java中的各种锁和并发机制是对当前JVM进程内的资源进行控制，当跨JVM的时候就失效了。分布式集群模式下，要保证对某一块共享资源的线程安全操作，就可以通过设置分布式的锁来保证。
> 那么如何保证呢？方式有很多种，这里是通过redis来实现。
> - 加锁：往redis写入一个key不存在才写入成功，存在就不操作， 并且设置key的超时时间
> - 加锁：在Redis的2.6.12及以后中,使用 set key value [EX] [NX]  命令，达到lua脚本的原子性操作 NX指的是Key不存在则执行,EX 指的是key的超时自动失效秒数
> - 释放锁：执行完后删除加锁时写入的key
> - 释放锁：使用lua脚本进行解锁 具体命令查看[Redis 命令参考](http://redisdoc.com/index.html)

## 依赖

```xml

<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

## 代码分布式锁

```java
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
public class RedisLock {

    /**
     * 构造器私有化，保证单个JVM内单例模式
     */
    private RedisLock() {
    }

    private enum EnumSingleton {
        /**
         * 懒汉枚举单例
         */
        INSTANCE;
        private RedisLock instance;

        EnumSingleton() {
            instance = new RedisLock();
        }

        public RedisLock getSingleton() {
            return instance;
        }
    }

    public static RedisLock getInstance() {
        return EnumSingleton.INSTANCE.getSingleton();
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
            // 执行删除成功会返回1 失败会返回 0
            return jedis.eval(luaScript, Collections.singletonList(lock), Collections.singletonList(lock)).equals(1L);
        }
    }
}

```

## 参考资料

- [基于Redis封装分布式数据操作-萧大佬](https://github.com/lw1243925457/JAVA-000/tree/main/homework/redis/redis-lock)
- [枚举实现单例模式](https://www.jianshu.com/p/d35f244f3770)
- [Redis 命令参考](http://redisdoc.com/index.html)
- [Java中如何实现分布式锁，详细教程来啦~](https://blog.csdn.net/smilehappiness/article/details/107122381)
- [如何基于Redis实现分布式锁，详细教程拿走不送~](https://blog.csdn.net/smilehappiness/article/details/107592896)