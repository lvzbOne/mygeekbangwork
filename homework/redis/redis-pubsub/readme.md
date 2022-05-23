# Redis 的发布订阅模式
就是Jedis命令来写2个类 一个用于发布，一个用于订阅，订阅的类开启订阅时需要另起一个线程来避免阻塞。这块主要还是跟着萧大佬的作业体验了下

```java
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

```

```java
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
```
## 参考资料
- [基于Redis的PubSub实现订单异步处理](https://github.com/lw1243925457/JAVA-000/tree/main/homework/redis/redis-pubsub)
- [NoSQL之Redis---PUB/SUB(订阅与发布)---JAVA实现](https://blog.csdn.net/abcd898989/article/details/51697596)