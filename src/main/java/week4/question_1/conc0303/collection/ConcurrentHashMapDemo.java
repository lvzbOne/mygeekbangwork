package week4.question_1.conc0303.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        /**
         * 本示例展示了一个 多线程聚合处理
         * 声明一个 CountDownLatch 倒计数为2
         * 创建2个线程，对原子计数对象分别加5次，原子计数对象放在 ConcurrentHashMap<"a", AtomicInteger>内</>
         * 每个线程都是有a则获取a对应的原子计数对象进行加1，因为 ConcurrentHashMap 是线程安全的集合，因此
         * 当主线程调用 endLatch.await()时，主线程阻塞等待2个子线程执行完毕。
         * 最终结果是返回 10
         *
         */
        demo1();
    }

    public static void demo1() {
        final Map<String, AtomicInteger> count = new ConcurrentHashMap<>();
        final CountDownLatch endLatch = new CountDownLatch(2);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                AtomicInteger oldValue;
                for (int i = 0; i < 5; i++) {
                    oldValue = count.get("a");
                    if (null == oldValue) {
                        AtomicInteger zeroValue = new AtomicInteger(0);
                        // putIfAbsent有则返回a对应的value值，无则添加
                        oldValue = count.putIfAbsent("a", zeroValue);
                        if (null == oldValue) {
                            oldValue = zeroValue;
                        }
                    }
                    oldValue.incrementAndGet();
                }
                endLatch.countDown();
            }
        };
        new Thread(task).start();
        new Thread(task).start();

        try {
            endLatch.await();
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
