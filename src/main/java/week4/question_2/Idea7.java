package week4.question_2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方法7：主线程内生命 CountDownLatch 子线程 countDown，然后主线程调用
 * count.await() 阻塞等待子线程执行完毕返回
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/27
 */
public class Idea7 {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger result = new AtomicInteger(-99);
        CountDownLatch count = new CountDownLatch(1);

        Thread t = new Thread(() -> {
            result.set(Homework.sum());
            count.countDown();
        }, "线程一");
        t.start();
        // 主线程阻塞等待，子线程返回
        count.await();
        // 24157817
        System.out.println(result.get());
    }
}
