package week4.question_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方法8： 线程池+信号量
 * 主线程先阻塞，等待线程池里的线程执行，线程池里的线程先获取所有信号量，进行获取值，主线程获取不到信号量开始阻塞
 * 等待子线程执行完毕后释放信号量开始继续执行
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/27
 */
public class Idea8 {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger result = new AtomicInteger();
        final ExecutorService pool = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(3);

        pool.execute(() -> {
            try {
                semaphore.acquire(3);
                Thread.sleep(2000L);
                result.set(Homework.sum());
                semaphore.release(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(1000L);
        System.out.println(result.get());
        semaphore.acquire(3);
        semaphore.release(3);
        System.out.println(result.get());
        pool.shutdown();

    }
}
