package week4.question_1.conc0302.lock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class TestFair {
    public static volatile int race = 0;
    public static ReentrantLock lock = new ReentrantLock(true); // 改成false会好100倍

    public static void increase() {
        lock.lock();
        race++;    //变量自增操作
        lock.unlock();
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) {
        /**
         * 本示例：展示公平锁和不公平锁的策略时性能差异，不公平锁会比公平锁快近100背
         * 公平锁 ： new ReentrantLock(true) ，表示 谁等待的越久就先执行
         * 不公平锁 ： new ReentrantLock(false) 默认是这个。 表示随机选择执行
         */
        int count = Thread.activeCount();
        long now = System.currentTimeMillis();
        System.out.println(count);
        AtomicReference<Thread> sign = new AtomicReference<>();
        Thread[] threads = new Thread[THREADS_COUNT];  //定义20个线程
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100000; i++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }//等待所有累加线程都结束
        while (Thread.activeCount() > count) {
            //TODO： 当线程执行体大于初始时，主线程一直让步，让出CPU给其它线程（虽然不是阻塞，但是不会往下执行）
            Thread.yield();
        }
        System.out.println(lock.getClass().getName() + " ts = " + (System.currentTimeMillis() - now));//公平锁:12734 , 不公平锁:59
    }
}

