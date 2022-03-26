package week4.question_1.conc0303.tool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo2 {

    private final static int threadCount = 20;

    public static void main(String[] args) throws Exception {
        /**
         * semaphore.acquire(2),semaphore.release(2) 日志每次显示2个task 进行处理
         * semaphore.acquire(3),semaphore.release(3) 日志每次显示1个task 进行处理
         * semaphore.acquire(4),semaphore.release(3) 日志打印2个后直接阻塞死了
         * 这是为何？？？？？
         *
         * 信号量就是资源，假设默认设置初始时5个信号量，20个线程抢，如果acquire(4),release(3)。
         * 第一个线程消耗4个信号量，开始执行，此时总信号量个数就变成1，其它线程获取不到4个信号量，就等待阻塞了，
         * 当第一个线程执行完后，放回3个信号量，此时总信号量个数就变成4，阻塞的线程里就会有一个线程可以消费这四个信号量开始执行，
         * 此时总信号量个数就是0，其它18个线程就又阻塞等待了，当这个线程执行完后，放回3个信号量，此时总信号量就变成3，
         * 余下18个线程再也获取不到4个信号量就永久阻塞了。
         */
        ExecutorService exec = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
//                    System.out.println("当前线程前：" + Thread.currentThread().getName() + "，信号量个数：" + semaphore.availablePermits());
                    semaphore.acquire(2); // 获取全部许可，退化成串行执行
                    test(threadNum, semaphore);
//                    System.out.println("当前线程中：" + Thread.currentThread().getName() + "，信号量个数：" + semaphore.availablePermits());
                    semaphore.release(3); // 释放多个许可
//                    System.out.println("当前线程后：" + Thread.currentThread().getName() + "，信号量个数：" + semaphore.availablePermits());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
    }

    private static void test(int threadNum, Semaphore semaphore) throws Exception {
        System.out.println("id:" + threadNum + "," + Thread.currentThread().getName() + "，信号量个数：" + semaphore.availablePermits());
        Thread.sleep(2000);
    }
}
