package week4.question_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 方法9：LockSupport.park()
 * 主线程执行 LockSupport.park() 进入阻塞
 * 子线程获取值后时 执行LockSupport.park(main) 唤醒主线程
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/27
 */
public class Idea9 {
    public static void main(String[] args) {

        final Thread main = Thread.currentThread();
        AtomicInteger result = new AtomicInteger();
        final ExecutorService pool = Executors.newCachedThreadPool();

        pool.submit(() -> {
            try {
                Thread.sleep(1000L);
                result.set(Homework.sum());
                LockSupport.unpark(main);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 0
        System.out.println(result.get());
        LockSupport.park();
        // 24157817
        System.out.println(result.get());
        pool.shutdown();
    }
}
