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
